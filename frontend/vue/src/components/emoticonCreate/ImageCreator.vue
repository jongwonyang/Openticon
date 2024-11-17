<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center"
  >
    <div class="fixed inset-0 bg-black opacity-50"></div>
    <div
      class="relative bg-white rounded-lg max-w-2xl w-full mx-4 max-h-[90vh]"
    >
      <div class="flex justify-between items-center p-6">
      <h2
        class="text-2xl font-nnsqneo-heavy whitespace-nowrap"
      >
        이미지 생성
        </h2>
        <button @click="closeModal" class="rounded-full p-2 hover:bg-gray-300 transition-all duration-200 ease-in-out active:bg-gray-400 flex items-center justify-center">
          <span class="material-icons">close</span>
        </button>
      </div>

      <ImageEditor :image="image || null" @update:editedImage="handleImage($event)" ref="imageEditor" />
      <ImageOptions @update:image="image = $event" @update:cancel="closeModal" @update:apply="handleApplyImage" />

      <!-- <div v-if="isUploaded" class="flex justify-end gap-2 p-6">
        <button
          @click="applyImage"
          class="border border-slate-500 bg-slate-500 text-white rounded-md px-4 py-2 hover:bg-slate-600 transition-all duration-200 ease-in-out active:bg-slate-700"
        >
          업로드 완료! 마이페이지로 가기
        </button>
      </div> -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import ImageEditor from './Creator/ImageEditor.vue';
import ImageOptions from './Creator/ImageOptions.vue';

const props = defineProps<{
  isOpen: boolean;
}>();

const imageEditor = ref<InstanceType<typeof ImageEditor> | null>(null);
const image = ref<File | null>(null);

const emit = defineEmits<{
  (e: "update:isOpen", value: boolean): void;
  (e: "applyImage", image: File): void;
}>();

const closeModal = () => {
  emit("update:isOpen", false);
};

const handleApplyImage = () => {
  if (imageEditor.value) {
    imageEditor.value.exportToImage();
  }
};

const handleImage = (image: File) => {
  emit("applyImage", image);
  closeModal();
};
</script>
