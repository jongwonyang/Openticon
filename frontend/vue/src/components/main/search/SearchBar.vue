<template>
  <div class="relative">
    <!-- 오버레이 배경 -->
    <transition name="fade">
      <div
        v-show="isFocused"
        class="fixed inset-0 bg-black bg-opacity-40 transition-opacity duration-200 z-20"
        @click="isFocused = false"
      ></div>
    </transition>

    <!-- 검색바 -->
    <div class="max-w-screen-lg mx-auto pt-6 pb-4 px-4 relative z-30">
      <div
        class="w-full flex flex-row justify-between border border-gray-300 rounded-lg shadow-md bg-white transition-all duration-200 overflow-hidden"
        :class="[isFocused ? 'scale-[1.02]' : 'scale-100']"
      >
        <div class="flex items-center w-24 hover:bg-gray-200 active:bg-gray-300">
          <select
            v-model="searchType"
            class="focus:outline-none text-center w-full h-full"
          >
            <option value="title">제목</option>
          <option value="author">게시자</option>
            <option value="tag">태그</option>
          </select>
        </div>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="검색어를 입력하세요."
          class="h-11 focus:outline-none placeholder:text-gray-400 text-gray-700 border-l px-2 flex-grow"
          @focus="isFocused = true"
          @keyup.enter="handleSearch"
        />
        <button
          @click="handleSearch"
          class="flex items-center justify-center w-20 hover:bg-gray-200 active:bg-gray-300 border-l"
        >
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

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

const props = defineProps<{
  query: string | null;
  type: string | null;
}>();

const searchQuery = ref(props.query || "");
const searchType = ref(props.type || "title");
const isFocused = ref(false);

const router = useRouter();

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({
      path: "/search",
      query: { query: searchQuery.value, type: searchType.value },
    });
    isFocused.value = false;
  } else {
    alert("검색어를 입력하세요.");
  }
};
</script>
