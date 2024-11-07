<template>
  <div class="container mx-auto max-w-screen-lg">
    <!-- 이모티콘 리스트 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 pt-4 px-4">
      <RouterLink
        :to="{ name: 'packDetail', params: { id: emoticon.id } }"
        class="border p-4 emoticon-item flex flex-row"
        v-for="emoticon in popularEmoticonList"
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
        </div>
      </RouterLink>
    </div>
    <div v-if="loading" class="flex justify-center items-center">
      <Loading />
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

const popularEmoticonList = ref<EmoticonPackInList[]>([]);
const currentPage = ref(0);
const loading = ref(false);
const hasMore = ref(true);

const emoticonPackStore = useEmoticonPackStore();

const loadMoreEmoticons = async () => {
  if (loading.value || !hasMore.value) return;

  loading.value = true;
  try {
    const popularEmoticonsResult =
      await emoticonPackStore.getPopularEmoticonPackList(
        currentPage.value++,
        10
      );
    if (popularEmoticonsResult.last) {
      hasMore.value = false;
    }
    popularEmoticonList.value.push(...popularEmoticonsResult.content);
  } catch (error) {
    console.error("이모티콘을 불러오는 중 오류가 발생했습니다:", error);
  } finally {
    loading.value = false;
  }
};

const { y } = useWindowScroll();

useInfiniteScroll(
  window,
  async () => {
    const scrollHeight = document.documentElement.scrollHeight;
    const scrollTop = y.value;
    const clientHeight = document.documentElement.clientHeight;

    if (scrollTop + clientHeight >= scrollHeight - 100) {
      // 하단에서 100px 남았을 때 로드
      await loadMoreEmoticons();
    }
  },
  { distance: 10 }
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
  @apply hover:font-bold hover:underline hover:rounded-lg hover:shadow-lg hover:scale-105 active:scale-95 active:bg-gray-50;
}

.emoticon-item-no-hover {
  cursor: pointer;
  @apply rounded-lg shadow-lg;
}
</style>
