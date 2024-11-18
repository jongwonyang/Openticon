<template>
  <div class="overflow-y-auto">
    <div class="grid grid-cols-4 gap-4 sm:grid-cols-5 md:grid-cols-6">
      <img
        v-for="emoticon in emoticons"
        :key="emoticon"
        :src="emoticon"
        alt="이모티콘"
        class="w-full aspect-square object-cover"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  emoticons: string[];
}>();
</script> 