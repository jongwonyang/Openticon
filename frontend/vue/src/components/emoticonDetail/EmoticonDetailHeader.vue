<template>
  <div
    class="grid sm:grid-cols-[40%_60%] grid-cols-[100%] py-4 px-8 sm:px-16 sm:pt-8 lg:pb-4 lg:px-32"
  >
    <div class="w-full flex justify-center p-4">
      <div class="w-[70%] flex justify-center items-center">
        <img
          :src="emoticon?.thumbnailImg"
          alt="이모티콘 대표 이미지"
          class="w-full h-auto object-cover"
          draggable="false"
          style="user-select: none"
        />
      </div>
    </div>

    <div class="w-full flex flex-col justify-between">
      <div>
        <div class="flex justify-center sm:justify-start items-center">
          <div class="px-2 text-center sm:text-left">
            <span class="text-2xl font-bold font-nnsqneo">{{
              emoticon?.title
            }}</span>
            <span
              v-if="emoticon?.aigenerated"
              class="mx-2 py-1 px-2 text-xs rounded-full bg-blue-500 text-white font-nnsqneo whitespace-nowrap"
              >AI 생성
            </span>
          </div>
        </div>
        <div class="flex items-center px-2 pt-2">
          <RouterLink
            class="text-md text-gray-500 text-center sm:text-left hover:underline"
            :to="{
              name: 'searchResult',
              query: { type: 'author', query: emoticon?.author.nickname },
            }"
          >
            {{ emoticon?.author.nickname }}
          </RouterLink>
        </div>
        <div
          class="relative hover:bg-gray-100 transition duration-200 cursor-pointer rounded p-2"
        >
          <div
            class="text-sm text-gray-500 text-left"
            :class="isExpanded ? '' : 'line-clamp-2'"
            @click="isExpanded = !isExpanded"
            style="user-select: none"
          >
            <p class="whitespace-pre-wrap">{{ emoticon?.description }}</p>
            <p class="text-right text-xs pt-2 text-gray-500">
              등록날짜 : {{ formattedDate }}
            </p>
          </div>
        </div>
      </div>

      <div class="pt-1 px-2 flex justify-between">
        <template v-if="emoticon?.price !== 0">
          <span class="font-bold text-2xl text-red-500">{{
            emoticon?.price
          }}</span>
          <span class="text-sm text-gray-500 pl-1">포인트</span>
        </template>
        <template v-else>
          <span class="font-bold text-2xl text-blue-500"> 무료 </span>
        </template>
        <div class="flex items-center">
          <button
            class="text-sm text-gray-500 hover:bg-gray-200 rounded-full p-1 flex items-center justify-center transition duration-200"
            @click="shareEmoticon"
          >
            <i class="material-symbols-outlined">share</i>
          </button>
        </div>
      </div>
      <div class="px-2 pt-2">
        <div
          class="w-full p-4 text-center border text-white border-blue-400 bg-blue-500 hover:bg-blue-600 active:bg-blue-700 transition duration-200 rounded-md"
          @click="handleOpenApp"
        >
          앱에서 보기
        </div>
      </div>
    </div>

    <ShareModal
      :is-open="isShareModalOpen"
      :emoticon="emoticon"
      @close="isShareModalOpen = false"
    />
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";
import { computed, ref } from "vue";
import { RouterLink } from "vue-router";
import ShareModal from "./ShareModal.vue";

const props = defineProps<{
  emoticon: EmoticonPack | null;
}>();

const formattedDate = computed(() => {
  if (!props.emoticon?.createdAt) return "";
  const date = new Date(props.emoticon.createdAt);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}년 ${month}월 ${day}일`;
});

const isExpanded = ref(false);

const isShareModalOpen = ref(false);

const shareEmoticon = () => {
  isShareModalOpen.value = true;
};

const handleOpenApp = () => {
  window.open(
    `${import.meta.env.VITE_SHARE_URL}/${props.emoticon?.sharedLink}`,
    "_blank"
  );
};
</script>

<style>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
