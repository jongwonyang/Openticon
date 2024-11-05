<template>
  <div class="max-w-screen-lg mx-auto pt-4 px-4">
    <div class="w-full flex flex-row justify-between px-4 border border-gray-400 rounded-lg shadow-md">
      <div class="">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="검색어를 입력하세요..."
          class="h-11 focus:outline-none placeholder:text-gray-400 text-gray-700"
        />
      </div>
      <button class="flex items-center justify-center">
        <span class="material-symbols-rounded text-xl">search</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.material-symbols-rounded {
  font-variation-settings: "FILL" 0, "wght" 400, "GRAD" 0, "opsz" 24;
  font-size: 20px;
}
</style>

<script setup>
import { ref } from "vue";

const searchQuery = ref("");

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    // 여기에 검색 로직 구현
    console.log("검색어:", searchQuery.value);
  }
};
</script>
