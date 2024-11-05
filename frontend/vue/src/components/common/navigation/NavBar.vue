<!-- Start Generation Here -->
<template>
  <div class="flex justify-between items-center px-2 sm:px-4 md:px-6 pt-3">
    <div class="w-12">
    </div>
    <h1 class="text-2xl font-bold">
      <RouterLink to="/">
        <span class="font-nnsqneo-heavy">OPEN</span><span class="font-nnsqneo">TICON</span>
      </RouterLink>
    </h1>
    <div class="relative">
      <img 
        v-if="userStore.isLogined" 
        :src="userStore.userInfo?.profile_image" 
        alt="프로필 사진" 
        class="w-12 h-12 relative z-20 rounded-full border bg-white cursor-pointer hover:scale-110  hover:shadow-lg hover:shadow-gray-300 transition-all duration-300" 
        @click="toggleUserMenu"
        :class="{'scale-110 shadow-lg shadow-gray-300': isUserMenuOpen}"
        ref="profileImageElement"
      />
      <button v-else @click="openLoginModal" class="w-12 h-12">로그인</button>
      <UserMenu :is-open="isUserMenuOpen" :profile-image-element="profileImageElement" @close="isUserMenuOpen = false" />
    </div>
  </div>
  <nav class="pt-4 border-b-2 border-gray-200">
    <div class="grid grid-cols-4 max-w-screen-md mx-auto">
        <RouterLink :to="{ name: 'main' }" :class="['nav-link', { active: isActive('main') }]">
          메인
        </RouterLink>
        <RouterLink :to="{ name: 'newList' }" :class="['nav-link', { active: isActive('newList') }]">
          신규
        </RouterLink>
        <RouterLink :to="{ name: 'popularList' }" :class="['nav-link', { active: isActive('popularList') }]">
          인기
        </RouterLink>
        <RouterLink :to="{ name: 'uploadEmoticon' }" :class="['nav-link', { active: isActive('uploadEmoticon') }]">
          업로드
        </RouterLink>
    </div>
  </nav>
  <LoginModal :is-open="isLoginModalOpen" @close="closeLoginModal" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from "@/stores/user";
import { RouterLink, useRoute } from "vue-router";
import LoginModal from './LoginModal.vue';
import UserMenu from './UserMenu.vue'; // UserMenu 컴포넌트 임포트

const route = useRoute();

function isActive(routeName: string) {
  return route.name === routeName;
}

const userStore = useUserStore();

const isLoginModalOpen = ref(false);
const isUserMenuOpen = ref(false);
const profileImageElement = ref(null);

function openLoginModal() {
  isLoginModalOpen.value = true;
}

function closeLoginModal() {
  isLoginModalOpen.value = false;
}

function toggleUserMenu(e: MouseEvent) {
  isUserMenuOpen.value = !isUserMenuOpen.value;
  e.stopPropagation();
}
</script>

<style scoped>
.nav-link {
  font-family: 'NanumSquareNeo', sans-serif;

  @apply text-black font-light text-lg;
  @apply flex justify-center items-center py-2 border-b-2 border-transparent;
  @apply hover:border-b-2 hover:border-black;
  @apply hover:font-bold hover:bg-gray-100;

  @apply cursor-pointer;
}

.nav-link.active {
  @apply border-b-2 border-black font-bold;
  /* 현재 경로일 때 강조 효과 */
}

.user-menu {
  /* 유저 메뉴 스타일 추가 */
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;
  width: 150px; /* 메뉴 너비 조정 */
}
</style>
<!-- End Generation Here -->
