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
          <img :src="emoticon.image" alt="이모티콘" class="w-full" />
          <p class="text-md text-center">{{ emoticon.name }}</p>
        </RouterLink>
      </div>
      <!-- md 크기 이상일 때는 그리드 -->
      <RouterLink
        :to="{ name: 'packDetail', params: { id: emoticon.id } }"
        class="hidden md:block border p-4 emoticon-item"
        v-for="emoticon in newEmoticonList"
        :key="emoticon.id"
      >
        <img :src="emoticon.image" alt="이모티콘" class="w-full" />
        <p class="text-md text-center">{{ emoticon.name }}</p>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";

const newEmoticonList = ref([]);

onMounted(() => {
  newEmoticonList.value = [
    {
      id: 1,
      name: "이모티콘1",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 2,
      name: "이모티콘2",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 3,
      name: "이모티콘3",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 4,
      name: "이모티콘4",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 5,
      name: "이모티콘5",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 6,
      name: "이모티콘6",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 7,
      name: "이모티콘7",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 8,
      name: "이모티콘8",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 9,
      name: "이모티콘9",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
    {
      id: 10,
      name: "이모티콘10",
      image: "https://public.sgr.cspark.kr/OPENTICON/molu.gif",
    },
  ];
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
