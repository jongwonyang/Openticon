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
              class="ml-2 py-1 px-2 text-xs rounded-full bg-blue-500 text-white font-nnsqneo whitespace-nowrap"
            >
              AI 생성
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
            <p class="whitespace-pre">{{ emoticon?.description }}</p>
            <p class="text-right text-xs text-gray-500">
              등록날짜 : {{ formattedDate }}
            </p>
          </div>
        </div>
      </div>

      <div class="pt-1 px-2">
        <template v-if="emoticon?.price !== 0">
          <span class="font-bold text-2xl text-red-500">{{
            emoticon?.price
          }}</span>
          <span class="text-sm text-gray-500 pl-1">포인트</span>
        </template>
        <template v-else>
          <span class="font-bold text-2xl text-blue-500"> 무료 </span>
        </template>
      </div>
      <div class="px-2 pt-2">
        <div
          class="w-full p-4 text-center border text-gray-500 border-gray-300 bg-gray-200 rounded-sm"
        >
          앱에서 구매할 수 있습니다.
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";
import { computed, ref } from "vue";
import { RouterLink } from "vue-router";

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
</script>

<style>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
