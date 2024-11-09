<template>
  <MypageBanner />
  <div class="max-w-screen-lg mx-auto pt-6">
    <div class="flex flex-col sm:flex-row gap-2 px-4 flex-wrap justify-center">
      <div
        class="flex flex-col justify-center items-center sm:items-end"
      >
        <div class="w-48 relative mx-4 flex-shrink-0">
          <img
            :src="userStore.userInfo?.profile_image"
            alt="프로필 이미지"
            class="w-full object-cover rounded-full"
          />
          <button
            @click="showProfileImageModal = true"
            class="absolute top-0 right-0 bg-blue-500 text-white p-2 h-12 w-12 rounded-full flex justify-center items-center hover:bg-blue-600 hover:shadow-lg"
          >
            <span class="material-icons text-3xl"> edit </span>
          </button>
        </div>
      </div>
      <div
        class="flex flex-col gap-1 items-center justify-center sm:items-start mx-4 flex-shrink-0"
      >
        <div class="flex">
          <span class="text-3xl font-nnsqneo-heavy truncate max-w-96">{{
            userStore.userInfo?.nickname
          }}</span>
          <button class="text-sm text-gray-500">
            <span class="material-symbols-outlined"> edit_square </span>
          </button>
        </div>
        <div class="text-md font-nnsqneo text-gray-500 truncate max-w-96">
          {{ userStore.userInfo?.email }}
        </div>
        <div
          v-if="userStore.userInfo?.manager"
          class="text-md font-nnsqneo text-gray-500"
        >
          관리자
        </div>
        <div class="text-md font-nnsqneo text-gray-500">
          가입일시 : {{ userStore.userInfo?.createdAt }}
        </div>
        <div class="text-2xl text-blue-700 font-nnsqneo-extra-bold">
          {{ userStore.userInfo?.point }} 포인트
        </div>
      </div>
      <div class="flex justify-center items-center flex-1 min-w-72">
        <div class="w-full h-full flex justify-center items-center rounded-lg bg-gray-100 p-4">
          알림
        </div>
      </div>
    </div>
  </div>
  <ProfileImageModal 
    v-if="showProfileImageModal" 
    :show="showProfileImageModal"
    @close="showProfileImageModal = false"
    @update="handleProfileUpdate"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from "@/stores/user";
import MypageBanner from "./MypageBanner.vue";
import ProfileImageModal from "./ProfileImageModal.vue";

const userStore = useUserStore();
const showProfileImageModal = ref(false);

const handleProfileUpdate = async (imageFile: File) => {
  try {
    // TODO: API 호출하여 프로필 이미지 업데이트
    await userStore.updateProfileImage(imageFile);
    showProfileImageModal.value = false;
  } catch (error) {
    console.error('프로필 이미지 업데이트 실패:', error);
  }
};
</script>
