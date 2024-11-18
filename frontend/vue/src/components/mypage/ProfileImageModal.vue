<template>
  <div
    v-if="show"
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
  >
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-bold">프로필 이미지 변경</h2>
        <button
          @click="$emit('close')"
          class="text-gray-500 hover:text-gray-700"
        >
          <span class="material-icons">close</span>
        </button>
      </div>

      <div class="mb-4">
        <div
          class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500"
          @click="triggerFileInput"
          @dragover.prevent
          @drop.prevent="handleDrop"
        >
          <div v-if="previewImage" class="mb-4">
            <img
              :src="previewImage"
              alt="미리보기"
              class="mx-auto w-48 h-48 object-cover rounded-full"
            />
          </div>
          <div v-else class="py-8">
            <span class="material-icons text-4xl text-gray-400"
              >add_photo_alternate</span
            >
            <p class="mt-2 text-gray-500">
              이미지를 드래그하거나 클릭하여 업로드하세요
            </p>
          </div>
        </div>
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          class="hidden"
          @change="handleFileSelect"
        />
      </div>

      <div class="flex justify-end gap-2">
        <button
          @click="$emit('close')"
          class="px-4 py-2 text-gray-600 hover:text-gray-800"
        >
          취소
        </button>
        <button
          @click="handleUpload"
          v-if="!errorOccured"
          :disabled="!selectedFile"
          class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          변경하기
        </button>
        <button
          v-if="errorOccured"
          class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
        >
          변경 실패!
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useUserStore } from "@/stores/user";

const props = defineProps<{
  show: boolean;
}>();

const userStore = useUserStore();

const emit = defineEmits<{
  (e: "close"): void;
}>();

const fileInput = ref<HTMLInputElement | null>(null);
const selectedFile = ref<File | null>(null);
const previewImage = ref<string | null>(null);
const errorOccured = ref(false);
const triggerFileInput = () => {
  fileInput.value?.click();
};

const handleFileSelect = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    handleFile(file);
  }
};

const handleDrop = (event: DragEvent) => {
  const file = event.dataTransfer?.files[0];
  if (file) {
    handleFile(file);
  }
};

const handleFile = (file: File) => {
  if (!file.type.startsWith("image/")) {
    alert("이미지 파일만 업로드 가능합니다.");
    return;
  }
  selectedFile.value = file;
  const reader = new FileReader();
  reader.onload = (e) => {
    previewImage.value = e.target?.result as string;
  };
  reader.readAsDataURL(file);
};

const handleUpload = () => {
  // TODO: API 호출하여 프로필 이미지 업데이트
  userStore
    .updateProfileImage(selectedFile.value!)
    .then(() => {
      userStore.retrieveUserInfo();
      emit("close");
    })
    .catch(() => {
      errorOccured.value = true;
    });
};
</script>
