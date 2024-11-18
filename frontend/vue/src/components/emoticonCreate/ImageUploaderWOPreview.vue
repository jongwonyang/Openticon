<script setup lang="ts">
import { ref } from "vue";
import ImageCreator from "./ImageCreator.vue";

const emit = defineEmits<{
  (e: "update:file", file: File): void;
}>();

const imageData = ref<File[]>([]);

const ALLOWED_EXTENSIONS = ["jpg", "jpeg", "png", "gif"];
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
const REQUIRED_WIDTH = 200; // 정확히 200px
const REQUIRED_HEIGHT = 200; // 정확히 200px

const isImageCreatorOpen = ref(false);

function handleFileInput(event: Event) {
  const input = event.target as HTMLInputElement;
  const files = input.files;
  if (files && files.length > 0) {
    Array.from(files).forEach((file) => handleImageFile(file));
  }
  input.value = "";
}


function resizeImage(file: File): Promise<File> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.src = URL.createObjectURL(file);

    img.onload = () => {
      URL.revokeObjectURL(img.src);

      if (file.type.startsWith("image/gif")) {
        if (img.width !== REQUIRED_WIDTH || img.height !== REQUIRED_WIDTH) {
          reject(
            new Error(
              "GIF 파일은 리사이징을 지원하지 않습니다. 정확히 200x200 크기여야 합니다."
            )
          );
          return;
        }
        resolve(file);
        return;
      }

      const size = Math.min(img.width, img.height);
      const startX = (img.width - size) / 2;
      const startY = (img.height - size) / 2;

      const canvas = document.createElement("canvas");
      canvas.width = REQUIRED_WIDTH;
      canvas.height = REQUIRED_HEIGHT;

      const ctx = canvas.getContext("2d");
      if (!ctx) {
        reject(new Error("Canvas 컨텍스트를 생성할 수 없습니다."));
        return;
      }

      ctx.imageSmoothingEnabled = true;
      ctx.imageSmoothingQuality = "high";

      ctx.drawImage(
        img,
        startX,
        startY,
        size,
        size,
        0,
        0,
        REQUIRED_WIDTH,
        REQUIRED_HEIGHT
      );

      canvas.toBlob(
        (blob) => {
          if (!blob) {
            reject(new Error("이미지 변환 중 오류가 발생했습니다."));
            return;
          }

          const resizedFile = new File([blob], file.name, {
            type: file.type,
            lastModified: Date.now(),
          });

          resolve(resizedFile);
        },
        file.type,
        1.0
      );
    };

    img.onerror = () => {
      URL.revokeObjectURL(img.src);
      reject(new Error("이미지 파일을 읽는 중 오류가 발생했습니다."));
    };
  });
}

async function handleImageFile(file: File) {
  const extension = file.name.split(".").pop()?.toLowerCase();

  if (file.size > MAX_FILE_SIZE) {
    alert("파일 크기는 5MB 이하여야 합니다.");
    return;
  }

  if (!extension || !ALLOWED_EXTENSIONS.includes(extension)) {
    alert("허용된 파일 형식이 아닙니다. (jpg, jpeg, png, gif만 가능)");
    return;
  }

  if (file.type.startsWith("image/")) {
    try {
      const processedFile = await resizeImage(file);
      imageData.value.push(processedFile);
      emit("update:file", processedFile);
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      } else {
        alert("이미지 처리 중 오류가 발생했습니다.");
      }
    }
  } else {
    alert("이미지 파일만 업로드 가능합니다.");
  }
}

function handleDragOver(event: DragEvent) {
  event.preventDefault();
}
</script>

<template>
  <div class="flex flex-col items-center justify-center aspect-square">
    <div
      @click="isImageCreatorOpen = true"
      @dragover="handleDragOver"
      :class="[
        'w-full h-full bg-gray-100 rounded-md border-2 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-200',
        'border-gray-400 border-dashed',
      ]"
    >
      <span class="material-icons text-gray-400 text-6xl"
        >create</span
      >
      <span class="text-gray-400 text-sm">이미  지 생성하기</span>
      <input
        ref="fileInput"
        type="file"
        class="hidden"
        accept=".jpg,.jpeg,.png,.gif"
        multiple
        @change="handleFileInput"
      />
    </div>
  </div>
  
  <ImageCreator :isOpen="isImageCreatorOpen" @update:isOpen="isImageCreatorOpen = $event" @applyImage="handleImageFile($event)" />
</template>
