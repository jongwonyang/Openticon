<template>
  <div class="px-6 grid grid-cols-2 gap-6">
    <div
      class="flex items-center justify-center w-full aspect-square border rounded-lg overflow-hidden"
    >
      <canvas ref="canvas" class="w-full"></canvas>
    </div>
    <div class="flex flex-col gap-4">
      <div class="flex-grow">
        <p class="font-medium mb-2">스티커</p>
        <div class="grid grid-cols-4 gap-2">
          <button
            v-for="sticker in stickers"
            :key="sticker"
            @click="addSticker(sticker)"
            class="p-2 border rounded-lg hover:bg-gray-100"
          >
            <img :src="sticker" class="w-full" />
          </button>
        </div>
      </div>
      <div class="flex-shrink-0">
        <p class="font-medium mb-2">
          텍스트
          <input type="color" v-model="textColor" />
          <input type="color" v-model="textOutlineColor" />
        </p>
        <div class="flex items-center gap-2">
          <div class="flex-grow">
            <input
              v-model="textInput"
              type="text"
              class="border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500 w-full"
              @keyup.enter="addText"
            />
          </div>
          <div class="flex-shrink-0">
            <button
              @click="addText"
              class="border px-2 py-1 rounded-md bg-blue-500 text-white hover:bg-blue-600"
            >
              추가
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, watch, markRaw } from "vue";
import * as fabric from "fabric";

const props = defineProps<{
  image: File | null;
}>();

const canvas = ref<HTMLCanvasElement | null>(null);
const fabricCanvas = ref<fabric.Canvas | null>(null);
const textInput = ref("");

// 스티커 목록 (예시)
const stickers = [
  "/stickers/sticker1.png",
  "/stickers/sticker2.png",
  "/stickers/sticker3.png",
  "/stickers/sticker4.png",
];

const imageUrl = computed(() => {
  if (!props.image) return null;
  return URL.createObjectURL(props.image);
});

// 캔버스 초기화
onMounted(() => {
  if (canvas.value) {
    fabricCanvas.value = new fabric.Canvas(canvas.value, {
      width: canvas.value.clientWidth,
      height: canvas.value.clientWidth,
    });
  }
});

// 이미지가 변경될 때 캔버스에 로드
watch(imageUrl, (newUrl) => {
  console.log(newUrl);
  if (newUrl && fabricCanvas.value) {
    fabric.FabricImage.fromURL(newUrl).then((img) => {
      fabricCanvas.value?.clear();

      // 이미지 크기를 캔버스에 맞게 조정
      const scale = Math.min(canvas.value?.clientWidth! / img.width!);
      console.log(scale);
      img.scale(scale);
      img.set({
        left: canvas.value?.clientWidth! / 2 - (img.width! * scale) / 2,
        top: canvas.value?.clientWidth! / 2 - (img.height! * scale) / 2,
      });
      fabricCanvas.value?.add(markRaw(img));
      img.selectable = false;
      img.evented = false;
      fabricCanvas.value?.renderAll();
    });
  }
});

// 텍스트 추가
const addText = () => {
  if (!textInput.value || !fabricCanvas.value) return;

  const text = new fabric.Textbox(textInput.value, {
    left: 100,
    top: 100,
    fontSize: 20,
    fontWeight: "bold",
    fill: textColor.value,
    stroke: textOutlineColor.value,      // 테두리 색상 (흰색)
    strokeWidth: 1,         // 테두리 두께
    strokeUniform: true,    // 테두리 두께를 균일하게 유지
  });

  fabricCanvas.value.add(markRaw(text));
  fabricCanvas.value.setActiveObject(text);
  textInput.value = "";
};

// 스티커 추가
const addSticker = (stickerUrl: string) => {
  if (!fabricCanvas.value) return;

  fabric.FabricImage.fromURL(stickerUrl).then((img) => {
    img.scale(0.5);
    img.set({
      left: 100,
      top: 100,
    });

    fabricCanvas.value?.add(markRaw(img));
    fabricCanvas.value?.setActiveObject(img);
  });
};

const textColor = ref("#FFFFFF");
const textOutlineColor = ref("#000000");
const emit = defineEmits<{
  (e: 'update:editedImage', image: File): void
}>();

// canvas를 이미지 파일로 변환하는 함수 추가
const exportToImage = () => {
  if (!fabricCanvas.value) return;
  
  // 캔버스를 DataURL로 변환
  const dataUrl = fabricCanvas.value.toDataURL({
    format: "png",
    quality: 1,
    multiplier: 2,
  });
  
  // DataURL을 Blob으로 변환
  fetch(dataUrl)
    .then(res => res.blob())
    .then(blob => {
      // Blob을 File 객체로 변환
      const file = new File([blob], 'edited-image.png', { type: 'image/png' });
      emit('update:editedImage', file);
    });
};

// exportToImage 함수를 외부에서 호출할 수 있도록 expose
defineExpose({
  exportToImage
});

</script>
