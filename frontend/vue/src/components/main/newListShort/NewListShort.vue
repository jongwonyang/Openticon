<template>
  <div class="container mt-4 p-4 mx-auto max-w-screen-lg">
    <h2>
      <span class="text-2xl font-nnsqneo font-bold pr-2">신규 이모티콘</span>
      <RouterLink
        :to="{ name: 'newList' }"
        class="text-md text-gray-500 hover:font-bold font-nnsqneo"
        >더보기 >
      </RouterLink>
    </h2>
    <!-- 이모티콘 리스트 -->
    <div class="md:grid md:grid-cols-5 md:gap-4 pt-4">
      <!-- md 크기 이하일 때는 가로 스크롤 -->
      <div class="flex gap-4 overflow-x-auto md:hidden pb-4">
        <RouterLink
          :to="{ name: 'packDetail', params: { id: emoticon.id } }"
          class="border p-4 emoticon-item-no-hover w-40 flex-shrink-0"
          v-for="emoticon in newEmoticonList"
          :key="emoticon.id"
        >
          <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-32 h-32" />
          <p class="text-md text-center mt-2 truncate">{{ emoticon.title }}</p>
        </RouterLink>
      </div>
      <!-- md 크기 이상일 때는 그리드 -->
      <RouterLink
        :to="{ name: 'packDetail', params: { id: emoticon.id } }"
        class="hidden md:block border p-4 emoticon-item"
        v-for="emoticon in newEmoticonList"
        :key="emoticon.id"
      >
        <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-full" />
        <p class="text-md text-center mt-2 truncate">{{ emoticon.title }}</p>
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
import type { EmoticonPackSearchList } from "@/types/emoticonPackSearchList";
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";
const newEmoticonList = ref<EmoticonPackInList[]>([]);

const emoticonPackStore = useEmoticonPackStore();
onMounted(() => {
  emoticonPackStore.getNewEmoticonPackList(0, 10).then((res: EmoticonPackSearchList) => {
    newEmoticonList.value = res.content;
  });
});
</script>

<style scoped>
/* 스크롤바 숨기기 */
.overflow-x-auto {
  -ms-overflow-style: none;
  /* IE, Edge */
  scrollbar-width: none;
  /* Firefox */
}

.overflow-x-auto::-webkit-scrollbar {
  display: none;
  /* Chrome, Safari, Opera */
}

/* 호버 효과 추가 */
.emoticon-item {
  transition: transform 0.05s ease, box-shadow 0.05s ease,
    border-color 0.05s ease, border-radius 0.05s ease;
  cursor: pointer;
  @apply hover:font-bold hover:underline hover:rounded-lg hover:shadow-lg hover:scale-105;
}

.emoticon-item-no-hover {
  cursor: pointer;
  @apply rounded-lg shadow-lg;
}
</style>
