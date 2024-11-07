<template>
  <div class="container mx-auto max-w-screen-lg px-4">
    <!-- 이모티콘 리스트 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 pt-4">
      <RouterLink
        :to="{ name: 'packDetail', params: { id: emoticon.id } }"
        class="border p-4 emoticon-item flex flex-row"
        v-for="emoticon in searchEmoticonList"
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
      v-if="searchEmoticonList.length === 0"
      class="flex justify-center items-center h-24 bg-gray-200 rounded-lg mt-4"
    >
      <p class="text-gray-500">검색 결과가 없습니다.</p>
    </div>

    <div
      v-if="!hasMore && searchEmoticonList.length > 0"
      class="flex justify-center items-center h-24 bg-gray-200 rounded-lg mt-4"
    >
      <p class="text-gray-500">더 이상 불러올 이모티콘이 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
import { onMounted, ref, watch } from "vue";
import { RouterLink } from "vue-router";
import { useWindowScroll, useInfiniteScroll } from "@vueuse/core";
import Loading from "../common/loading/Loading.vue";

const props = defineProps<{
  query: string;
  type: string;
}>();

const searchEmoticonList = ref<EmoticonPackInList[]>([]);
const currentPage = ref(0);
const loading = ref(false);
const hasMore = ref(true);

const emoticonPackStore = useEmoticonPackStore();

const loadMoreEmoticons = async () => {
  if (loading.value || !hasMore.value) return;

  loading.value = true;
  try {
    const searchEmoticonsResult = await emoticonPackStore.searchEmoticonPack(
      props.query,
      props.type,
      currentPage.value++,
      10
    );
    if (searchEmoticonsResult.last) {
      hasMore.value = false;
    }
    searchEmoticonList.value.push(...searchEmoticonsResult.content);
  } catch (error) {
    console.error("이모티콘을 불러오는 중 오류가 발생했습니다:", error);
  } finally {
    loading.value = false;
  }
};

useInfiniteScroll(
  window,
  async () => {
    await loadMoreEmoticons();
  },
  { distance: 400 }
);

onMounted(async () => {
  await loadMoreEmoticons();
});

watch(props, (newProps) => {
  searchEmoticonList.value = [];
  currentPage.value = 0;
  hasMore.value = true;
  loading.value = false;
  loadMoreEmoticons();
  console.log(props.query, props.type);
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
