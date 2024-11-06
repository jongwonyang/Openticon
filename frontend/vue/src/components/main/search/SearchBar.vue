<template>
  <div class="relative">
    <!-- 오버레이 배경 -->
    <transition name="fade">
      <div
        v-show="isFocused"
        class="fixed inset-0 bg-black bg-opacity-50 transition-opacity duration-200 z-20"
        @click="isFocused = false"
      ></div>
    </transition>

    <!-- 검색바 -->
    <div class="max-w-screen-lg mx-auto pt-4 px-4 relative z-30">
      <div
        class="w-full flex flex-row justify-between border rounded-lg shadow-md bg-white transition-all duration-200 overflow-hidden"
        :class="[isFocused ? 'scale-105' : 'scale-100']"
      >
        <select class="w-2/12 focus:outline-none text-center hover:bg-gray-200 active:bg-gray-300">
          <option value="title">제목</option>
          <option value="author">게시자</option>
          <option value="tag">태그</option>
        </select>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="검색어를 입력하세요..."
          class="h-11 focus:outline-none placeholder:text-gray-400 text-gray-700 w-9/12 border-l px-2"
          @focus="isFocused = true"
        />
        <button class="flex items-center justify-center w-1/12 hover:bg-gray-200 active:bg-gray-300 border-l">
          <span class="material-symbols-rounded text-xl">search</span>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.material-symbols-rounded {
  font-variation-settings: "FILL" 0, "wght" 400, "GRAD" 0, "opsz" 24;
  font-size: 20px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

<script setup>
import { ref } from "vue";

const searchQuery = ref("");
const isFocused = ref(false);

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    // 여기에 검색 로직 구현
    console.log("검색어:", searchQuery.value);
  }
};
</script>
