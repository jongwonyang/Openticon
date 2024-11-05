<template>
  <div
    class="grid grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-6 p-6 sm:p-8 sm:gap-8"
  >
    <div
      class="col-span-1"
      v-for="emoticon in emoticon?.emoticons"
      :key="emoticon"
    >
      <img
        :src="emoticon"
        alt="이모티콘 개별 이미지"
        class="w-full hover:scale-150 hover:shadow-2xl hover:rounded-lg hover:border hover:border-gray-400 transition-all duration-200 ease-in-out"
        draggable="false"
        style="user-select: none"
        loading="lazy"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";

defineProps<{
  emoticon: EmoticonPack | null;
}>();
</script>
