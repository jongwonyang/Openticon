<template>
  <div class="flex flex-col px-8 py-4">
    <p class="text-xl font-bold font-nnsqneo">이 이모티콘 팩의 태그</p>
    <div class="flex flex-row gap-2 pt-2">
      <RouterLink
        v-for="tag in tags"
        :key="tag"
        class="rounded-full border border-gray-300 p-2 hover:bg-gray-200 transition-all duration-200 ease-in-out active:bg-gray-300"
        :to="{
          path: '/search',
          query: { type: 'tag', query: tag },
        }"
      >
        #{{ tag }}
      </RouterLink>
    </div>
  </div>
</template>


<script setup lang="ts">
import type { EmoticonPack } from "@/types/emoticonPack";
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";

const props = defineProps<{
  emoticon: EmoticonPack | null;
}>();

const tags = ref<string[]>([]);

onMounted(() => {
  tags.value = ["레니콘", "테스트2", "테스트3"];
});
</script>
