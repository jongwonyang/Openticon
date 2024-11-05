<template>
  <div>
    <h2 class="text-2xl font-nnsqneo-bold">내가 올린 이모티콘</h2>
    <div class="flex flex-col gap-2" v-if="myEmoticonList.length > 0">
      <div
        v-for="(emoticon, index) in myEmoticonList"
        :key="index"
        class="flex flex-col"
      >
        <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-16 h-16" />
      </div>
    </div>
    <div
      class="flex flex-col justify-center items-center mt-4 px-8 py-28 text-gray-500 bg-gray-200 rounded-lg"
      v-else
    >
      <span class="text-2xl font-nnsqneo-heavy">올린 이모티콘이 없습니다.</span>
      <RouterLink
        :to="{ name: 'uploadEmoticon' }"
        class="text-blue-700 font-nnsqneo-extra-bold text-xl hover:underline mt-2"
        >올리러 가기</RouterLink
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "@/stores/user";
import type { EmoticonPack } from "@/types/emoticonPack";
import { onMounted, ref } from "vue";

const myEmoticonList = ref<EmoticonPack[]>([]);

const userStore = useUserStore();

onMounted(async () => {});
</script>
