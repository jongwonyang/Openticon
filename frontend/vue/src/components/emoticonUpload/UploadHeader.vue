<template>
  <div class="flex flex-col max-w-screen-lg mx-auto">
    <div class="mt-10 px-4 grid grid-cols-1 gap-4 md:grid-cols-2 grid-flow-col">
      <div class="flex flex-row items-center justify-around">
        <ImageUploader
          label="대표 이미지"
          @update:file="handleThumbnailUpdate"
        />
        <ImageUploader
          label="목록 이미지"
          @update:file="handleListImageUpdate"
        />
      </div>
      <div class="flex flex-col items-center justify-center gap-2">
        <input
          type="text"
          placeholder="이모티콘 이름"
          class="w-full h-10 border-2 border-gray-400 rounded-md"
          v-model="packTitle"
        />
        <select
          v-model="selectedCategory"
          class="w-full h-10 border-2 border-gray-400 rounded-md"
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
]);

const selectedCategory = ref<string>("");

watch(selectedCategory, (newValue) => {
  emit("update:selectedCategory", newValue);
});

const categories = [
  { value: "REAL", label: "실사" },
  { value: "ENTERTAINMENT", label: "방송연예" },
  { value: "CHARACTER", label: "캐릭터" },
  { value: "TEXT", label: "텍스트" },
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

</script>
