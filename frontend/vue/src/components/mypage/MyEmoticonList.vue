<template>
  <div>
    <h2 class="text-2xl font-nnsqneo-bold">내가 올린 이모티콘</h2>
    <div
      class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 pt-4"
      v-if="myEmoticonList.length > 0"
    >
      <div
        v-for="(emoticon, index) in myEmoticonList"
        :key="index"
        class="flex flex-row p-4 border gap-2 emoticon-item"
        @click="handleEmoticonClick(emoticon)"
      >
        <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-16 h-16" />
        <div class="flex flex-col">
          <div class="text-xl">{{ emoticon.title }}</div>
          <div>
          <span
            v-if="emoticon.price == 0"
            class="text-md text-blue-500 font-nnsqneo-bold"
            >무료</span
          >
          <span v-else class="text-md text-red-500 font-nnsqneo-bold"
            >{{ emoticon.price }} 포인트</span
          >
          <span
              class="text-sm text-white ml-1 px-2 rounded-full w-fit whitespace-nowrap"
              :class="emoticon.public ? 'bg-green-500' : 'bg-red-500'"
              >{{ emoticon.public ? "공개" : "비공개" }}</span
            >
          </div>
        </div>
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
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
import { useRouter } from "vue-router";

const props = defineProps<{
  myEmoticonList: EmoticonPackInList[];
}>();

const router = useRouter();

const handleEmoticonClick = (emoticon: EmoticonPackInList) => {
  if (emoticon.public) {
    router.push({ name: "packDetail", params: { id: emoticon.id } });
  } else {
    router.push({ name: "packDetailPrivate", params: { id: emoticon.shareLink } });
  }
};
</script>

<style scoped>
.emoticon-item {
  transition: transform 0.05s ease, box-shadow 0.05s ease,
    border-color 0.05s ease, border-radius 0.05s ease;
  cursor: pointer;
  @apply hover:rounded-lg hover:shadow-lg hover:scale-105 active:scale-95 active:bg-gray-100;
}
</style>