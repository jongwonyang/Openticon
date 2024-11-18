<template>
  <div
    class="grid grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 p-6 sm:gap-6"
    v-if="emoticon?.emoticons.length && emoticon?.emoticons.length > 0"
  >
    <div
      class="col-span-1"
      v-for="emoticon in emoticon?.emoticons"
      :key="emoticon"
    >
      <img
        :src="emoticon"
        alt="이모티콘 개별 이미지"
        class="w-full hover:scale-150 hover:shadow-2xl hover:rounded-lg hover:border hover:border-gray-400 transition-all duration-200 ease-in-out aspect-square object-cover"
        draggable="false"
        style="user-select: none"
        loading="lazy"
      />
    </div>
  </div>
  <div v-else class="px-8">
    <div class="text-xl text-center font-nnsqneo-heavy p-8 text-gray-600 bg-gray-200 rounded-lg">
      이 이모티콘 팩에는 이모티콘이 없습니다.
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";

defineProps<{
  emoticon: EmoticonPack | null;
}>();
</script>
