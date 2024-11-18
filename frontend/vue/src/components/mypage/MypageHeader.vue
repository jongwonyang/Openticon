<template>
  <MypageBanner />
  <div class="max-w-screen-lg mx-auto pt-6">
    <div class="flex flex-col sm:flex-row gap-2 px-4 flex-wrap justify-center">
      <div class="flex flex-col justify-center items-center sm:items-end">
        <div class="w-48 relative mx-4 flex-shrink-0">
          <img
            :src="userStore.userInfo?.profile_image"
            alt="프로필 이미지"
            class="w-full object-cover aspect-square rounded-full"
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
          <span class="text-3xl font-nnsqneo-heavy truncate">{{
            userStore.userInfo?.nickname
          }}</span>
          <button
            @click="showNicknameModal = true"
            class="text-sm text-gray-400 hover:text-gray-600"
          >
            <span class="material-icons"> edit_square </span>
          </button>
        </div>
        <div class="text-md font-nnsqneo text-gray-500 truncate">
          {{ userStore.userInfo?.email }}
        </div>
        <div
          v-if="userStore.userInfo?.manager"
          class="text-md font-nnsqneo text-gray-500"
        >
          관리자
        </div>
        <div class="text-md font-nnsqneo">
          <span v-if="userStore.userInfo?.bio == ''" class="text-gray-500"
            >상태메시지가 없습니다.</span
          >
          <span v-else>{{ userStore.userInfo?.bio }}</span>
          <button @click="showBioModal = true" class="">
            <span class="text-gray-400 material-icons hover:text-gray-600">
              edit_square
            </span>
          </button>
        </div>
        <div class="text-sm font-nnsqneo text-gray-500">
          가입일시 : {{ userStore.userInfo?.createdAt }}
        </div>
        <div class="text-2xl text-blue-700 font-nnsqneo-extra-bold">
          {{ userStore.userInfo?.point }} 포인트
        </div>
      </div>
    </div>
    <div
      v-if="blockedEmoticonCount > 0"
      class="flex justify-center items-center flex-1 mt-4 px-4"
    >
      <div
        class="w-full h-full flex justify-center items-center rounded-lg bg-rose-200 p-4 flex-col"
      >
        <span class="text-lg font-nnsqneo-extra-bold text-center text-rose-700"
          >표시중단된 이모티콘 :
          <span class="text-2xl font-nnsqneo-heavy"
            >{{ blockedEmoticonCount }}
            {{ moreBlockedEmoticon ? "+" : "" }}
          </span>
          개</span
        >
        <RouterLink :to="{ name: 'blacklist' }">
          <span
            class="material-icons text-xl hover:underline font-nnsqneo-heavy text-rose-700"
          >
            표시중단 목록 바로가기
          </span>
        </RouterLink>
      </div>
    </div>
    <div
      v-if="userStore.userInfo?.manager && objectionCount > 0"
      class="flex justify-center items-center flex-1 mt-4 px-4"
    >
      <div
        class="w-full h-full flex justify-center items-center rounded-lg bg-slate-200 p-4 flex-col"
      >
        <span class="text-lg font-nnsqneo-extra-bold text-center text-slate-700"
          >심사대기중인 이모티콘 :
          <span class="text-2xl font-nnsqneo-heavy"
            >{{ objectionCount }}
            {{ moreObjection ? "+" : "" }}
          </span>
          개</span
        >
        <RouterLink :to="{ name: 'objectionList' }">
          <span
            class="material-icons text-xl hover:underline font-nnsqneo-heavy text-slate-700"
          >
            심사대기 목록 바로가기
          </span>
        </RouterLink>
      </div>
    </div>
  </div>
  <ProfileImageModal
    v-if="showProfileImageModal"
    :show="showProfileImageModal"
    @close="showProfileImageModal = false"
  />
  <NicknameModal
    v-if="showNicknameModal"
    :show="showNicknameModal"
    :current-nickname="userStore.userInfo?.nickname"
    @close="showNicknameModal = false"
  />
  <BioModal
    v-if="showBioModal"
    :show="showBioModal"
    :current-bio="userStore.userInfo?.bio"
    @close="showBioModal = false"
  />
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useUserStore } from "@/stores/user";
import MypageBanner from "./MypageBanner.vue";
import ProfileImageModal from "./ProfileImageModal.vue";
import NicknameModal from "./NicknameModal.vue";
import BioModal from "./BioModal.vue";
import { useObjectionStore } from "@/stores/objection";
import type { BlacklistResult } from "@/types/blacklistResult";
import { useAdminStore } from "@/stores/admin";
import type { ObjectionListResult } from "@/types/objectionlistResult";

const userStore = useUserStore();
const objectionStore = useObjectionStore();

const blacklistResult = ref<BlacklistResult | null>(null);
const blockedEmoticonCount = ref(0);
const moreBlockedEmoticon = ref(false);

const objectionCount = ref(0);
const moreObjection = ref(false);

onMounted(async () => {
  blacklistResult.value = await objectionStore.getBlockedEmoticonPackList(
    0,
    100
  );
  blockedEmoticonCount.value = blacklistResult.value?.content.length ?? 0;
  moreBlockedEmoticon.value = !blacklistResult.value?.last;

  if (userStore.userInfo?.manager) {
    const adminStore = useAdminStore();
    const objectionList = await adminStore.getObjectionList(0, 100);
    objectionCount.value = objectionList.content.length;
    moreObjection.value = !objectionList.last;
  }
});

const showProfileImageModal = ref(false);
const showNicknameModal = ref(false);
const showBioModal = ref(false);
</script>
