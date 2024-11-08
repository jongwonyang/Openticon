<template>
  <div class="flex flex-col max-w-screen-lg mx-auto">
    <div class="mt-10 px-4 grid grid-cols-1 gap-4">
      <div class="flex flex-row gap-2">
        <div class="flex-grow">
          <p class="flex flex-row items-center">
            <span class="text-lg text-black font-nnsqneo-bold">이모티콘 팩 이름</span>
            <span class="text-red-500">*</span>
          </p>
          <input
            type="text"
            placeholder="이모티콘 이름"
            class="w-full h-10 border-2 border-gray-400 rounded-md px-2 focus:outline-none focus:border-slate-500 mt-2"
            v-model="packTitle"
          />
        </div>
        <div class="flex-grow-0 min-w-40">
            <p class="flex flex-row items-center">
              <span class="text-lg text-black font-nnsqneo-bold">카테고리</span>
            <span class="text-red-500">*</span>
          </p>
            <p class="flex flex-row items-center">
              <span class="text-lg text-black font-nnsqneo-bold">카테고리</span>
            <span class="text-red-500">*</span>
          </p>
          <select
            v-model="selectedCategory"
            class="w-full h-10 border-2 border-gray-400 rounded-md focus:outline-none focus:border-slate-500 mt-2"
          >
            <option value="" disabled>카테고리 선택</option>
            <option
              v-for="category in categories"
              :key="category.value"
              :value="category.value"
            >
              {{ category.label }}
            </option>
          </select>
        </div>
      </div>

      <div class="flex flex-col md:flex-row items-center justify-center gap-4">
        <div
          class="flex flex-row items-center justify-around flex-grow-0 gap-4 w-full md:w-auto"
        >
          <div class="flex flex-col items-center justify-center md:items-start gap-2">
            <div
              class="flex flex-row items-center"
            >
              <span class="text-lg text-black font-nnsqneo-bold">대표 이미지</span>
              <span class="text-red-500">*</span>
            </div>
            <ImageUploader @update:file="handleThumbnailUpdate" />
          </div>
          <div class="flex flex-col items-center justify-center md:items-start gap-2">
            <div
              class="flex flex-row items-center"
            >
              <span class="text-lg text-black font-nnsqneo-bold">목록 이미지</span>
              <span class="text-red-500">*</span>
            </div>
            <ImageUploader @update:file="handleListImageUpdate" />
          </div>
        </div>
        <div class="flex-grow flex flex-col h-full w-full">
          <div class="text-lg text-black font-nnsqneo-bold">설명</div>
          <textarea
            v-model="description"
            placeholder="이모티콘 설명"
            class="w-full flex-grow h-full border-2 border-gray-400 rounded-md p-2 resize-none focus:outline-none focus:border-slate-500 mt-2"
          >
          </textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import ImageUploader from "./ImageUploader.vue";

const emit = defineEmits([
  "update:thumbnailFile",
  "update:listFile",
  "update:selectedCategory",
  "update:packTitle",
  "update:description",
]);

const selectedCategory = ref<string>("");
const description = ref<string>("");
watch(selectedCategory, (newValue) => {
  emit("update:selectedCategory", newValue);
});

const categories = [
  { value: "REAL", label: "실사" },
  { value: "ENTERTAINMENT", label: "방송연예" },
  { value: "CHARACTER", label: "캐릭터" },
  { value: "LETTER", label: "텍스트" },
];

function handleThumbnailUpdate(file: File) {
  emit("update:thumbnailFile", file);
}

function handleListImageUpdate(file: File) {
  emit("update:listFile", file);
}

const packTitle = ref<string>("");

watch(packTitle, (newValue) => {
  emit("update:packTitle", newValue);
});

watch(description, (newValue) => {
  emit("update:description", newValue);
});
</script>
