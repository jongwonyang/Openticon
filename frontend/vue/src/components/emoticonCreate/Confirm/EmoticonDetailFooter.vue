<template>
  <div class="flex flex-col px-8 py-4">
    <p class="text-xl font-bold font-nnsqneo">이 이모티콘 팩의 태그</p>
    <div class="flex flex-row gap-2 pt-2 flex-wrap">
      <div
        v-for="tag in props.emoticon?.tags"
        :key="tag"
        class="rounded-full border border-gray-300 p-2"
        :to="{
          path: '/search',
          query: { type: 'tag', query: tag },
        }"
      >
        #{{ tag }}
      </div>
      <!-- TODO : API 서버에서 태그가 없는 경우 없애기 -->
      <div
        v-if="
          props.emoticon?.tags == undefined || props.emoticon?.tags.length === 0
        "
      >
        <p class="text-gray-500">태그가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";
import { RouterLink } from "vue-router";

const props = defineProps<{
  emoticon: EmoticonPack | null;
}>();
</script>
