<template>
  <div class="container mx-auto max-w-screen-lg">
    <div class="px-4 mt-4">
      <div class="text-gray-600 px-4 py-4 bg-gray-200 rounded-lg">
        업로드한 이모티콘 팩 중 AI 심사 통과실패 및 다수의 신고를 받은 이모티콘
        팩 목록입니다.<br />
        &nbsp;&nbsp;&nbsp;&nbsp;이모티콘 팩 중 표시중단 목록에 포함된 이모티콘
        팩은 다른 사용자에게 표시되지 않습니다.
      </div>
    </div>
    <!-- 이모티콘 리스트 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 pt-4 px-4">
      <RouterLink
        :to="{ name: 'packDetail', params: { id: emoticon.id } }"
        class="border p-4 emoticon-item flex flex-row"
        v-for="emoticon in blockedEmoticonList"
        :key="emoticon.id"
      >
        <div class="flex justify-center items-center">
          <img
            :src="emoticon.thumbnailImg"
            alt="이모티콘"
            class="w-28 aspect-square object-cover"
          />
        </div>
        <div class="flex flex-col justify-center items-start ml-4">
          <p class="text-md text-center line-clamp-2">{{ emoticon.title }}</p>
          <p class="text-sm text-center text-gray-500">
            {{ emoticon.member.nickname }}
          </p>
          <div>
            <span
              v-if="emoticon.price == 0"
              class="text-md text-blue-500 font-nnsqneo-bold"
              >무료</span
            >
            <span v-else class="text-md text-red-500 font-nnsqneo-bold"
              >{{ emoticon.price }} 포인트</span
            >
          </div>
        </div>
      </RouterLink>
    </div>
    <div v-if="loading" class="flex justify-center items-center">
      <Loading />
    </div>
    <div
      v-if="!hasMore"
      class="flex justify-center items-center h-24 bg-gray-200 rounded-lg mt-4"
    >
      <p class="text-gray-500">더 이상 불러올 이모티콘이 없습니다.</p>
    </div>
    <div
      v-if="errorOccurred"
      class="flex flex-col justify-center items-center bg-gray-200 rounded-lg mt-4 gap-4 p-4"
    >
      <p class="text-gray-500">이모티콘을 불러오는 중 오류가 발생했습니다.</p>
      <button
        @click="loadMoreEmoticons"
        class="rounded-full border border-gray-300 px-4 py-2 bg-white hover:bg-gray-100 active:bg-gray-300"
      >
        다시시도
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";
import { useWindowScroll, useInfiniteScroll } from "@vueuse/core";
import Loading from "../common/loading/Loading.vue";

const blockedEmoticonList = ref<EmoticonPackInList[]>([]);
const currentPage = ref(0);
const loading = ref(false);
const hasMore = ref(true);
const errorOccurred = ref(false);

const emoticonPackStore = useEmoticonPackStore();

const loadMoreEmoticons = async () => {
  if (loading.value || !hasMore.value) return;

  loading.value = true;
  try {
    const newEmoticonsResult = await emoticonPackStore.getNewEmoticonPackList(
      currentPage.value++,
      10
    );
    if (newEmoticonsResult.last) {
      hasMore.value = false;
    }
    blockedEmoticonList.value.push(...newEmoticonsResult.content);
    errorOccurred.value = false;
  } catch (error) {
    errorOccurred.value = true;
    console.error("이모티콘을 불러오는 중 오류가 발생했습니다:", error);
  } finally {
    loading.value = false;
  }
};

useInfiniteScroll(
  window,
  async () => {
    if (!errorOccurred.value) {
      await loadMoreEmoticons();
    }
  },
  { distance: 400 }
);

onMounted(async () => {
  await loadMoreEmoticons();
});
</script>

<style scoped>
/* 호버 효과 추가 */
.emoticon-item {
  transition: transform 0.05s ease, box-shadow 0.05s ease,
    border-color 0.05s ease, border-radius 0.05s ease;
  cursor: pointer;
  @apply hover:rounded-lg hover:shadow-lg hover:scale-105 active:scale-95 active:bg-gray-50;
}

.emoticon-item-no-hover {
  cursor: pointer;
  @apply rounded-lg shadow-lg;
}
</style>
