<!-- Start Generation Here -->
<template>
  <div class="flex justify-between items-center px-2 sm:px-4 md:px-6 pt-3">
    <div class="w-12">
    </div>
    <h1 class="text-2xl font-bold">
      <span class="font-nnsqneo-heavy">OPEN</span><span class="font-nnsqneo">TICON</span>
    </h1>
    <div>
      <img src="" alt="프로필 사진" class="w-12 h-12 rounded-full" />
    </div>
  </div>
  <nav class="px-2 pt-4  mx-auto border-b-2 border-gray-200">
    <ul class="flex justify-center">
      <li>
        <RouterLink :to="{ name: 'main' }" :class="['nav-link', { active: isActive('main') }]">
          메인
        </RouterLink>
      </li>
      <li>
        <RouterLink :to="{ name: 'newList' }" :class="['nav-link', { active: isActive('newList') }]">
          신규
        </RouterLink>
      </li>
      <li>
        <RouterLink :to="{ name: 'popularList' }" :class="['nav-link', { active: isActive('popularList') }]">
          인기
        </RouterLink>
      </li>
      <li>
        <RouterLink :to="{ name: 'uploadEmoticon' }" :class="['nav-link', { active: isActive('uploadEmoticon') }]">
          업로드
        </RouterLink>
      </li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
import { RouterLink, useRoute } from "vue-router";

const route = useRoute();

function isActive(routeName: string) {
  return route.name === routeName;
}
</script>

<style scoped>
.nav-link {
  font-family: 'NanumSquareNeo', sans-serif;

  @apply text-black font-light text-lg;
  @apply flex justify-center items-center px-8 sm:px-8 md:px-12 lg:px-16 py-2 border-b-2 border-transparent;
  @apply hover:border-b-2 hover:border-black;
  @apply hover:font-bold hover:bg-gray-100;

  @apply cursor-pointer;
}

.nav-link.active {
  @apply border-b-2 border-black font-bold;
  /* 현재 경로일 때 강조 효과 */
}
</style>
<!-- End Generation Here -->
