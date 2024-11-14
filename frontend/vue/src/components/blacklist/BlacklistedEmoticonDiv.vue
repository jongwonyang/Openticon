<template>
  <div
    :to="{ name: 'packDetail', params: { id: emoticon.id } }"
    class="border p-4 emoticon-item flex flex-col"
  >
    <div class="flex flex-row">
      <div class="flex justify-center items-center">
        <img
          :src="emoticon.emoticonPack.thumbnailImg"
          alt="이모티콘"
          class="w-20 aspect-square object-cover"
        />
      </div>
      <div class="flex flex-col justify-center items-start ml-4 flex-grow">
        <p class="text-md text-center line-clamp-2">
          {{ emoticon.emoticonPack.title }}
        </p>
        <div>
          <span
            v-if="emoticon.emoticonPack.price == 0"
            class="text-md text-blue-500 font-nnsqneo-bold"
            >무료</span
          >
          <span v-else class="text-md text-red-500 font-nnsqneo-bold"
            >{{ emoticon.emoticonPack.price }} 포인트</span
          >
        </div>
      </div>
      <div class="flex flex-col justify-center items-end">
        <button @click="isOpen = !isOpen">
          <span class="material-icons text-3xl mr-1">keyboard_arrow_down</span>
        </button>
      </div>
    </div>
    <div v-if="isOpen" class="flex flex-col">
      <div class="grid grid-cols-4 gap-2 sm:grid-cols-5 md:grid-cols-6">
        <img
          v-for="emoticon in emoticon.emoticonPack.emoticons"
          :src="emoticon"
          alt="이모티콘"
          class="w-full aspect-square object-cover"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { BlacklistedEmoticon } from '@/types/blacklistResult';
import { ref } from 'vue';

const isOpen = ref(false);

defineProps<{
  emoticon: BlacklistedEmoticon;
}>();
</script>
