<template>
  <div
    :class="[
      'border p-4 flex flex-col transition-all duration-300 rounded-lg',
      isOpen ? 'shadow-lg border-blue-500 scale-[1.03]' : '',
    ]"
  >
    <div class="flex flex-row">
      <div class="flex justify-center items-center">
        <img
          :src="objection.emoticonPack.thumbnailImg"
          alt="이모티콘"
          class="w-20 aspect-square object-cover"
        />
      </div>
      <div class="flex flex-col justify-center items-start ml-4 flex-grow">
        <p class="text-lg text-center line-clamp-2 font-nnsqneo-bold">
          {{ objection.emoticonPack.title }}
          <span
            class="text-sm text-white ml-1 px-2 rounded-full w-fit whitespace-nowrap"
            :class="
              objection.emoticonPack.public ? 'bg-green-500' : 'bg-red-500'
            "
            >{{ objection.emoticonPack.public ? "공개" : "비공개" }}</span
          >
        </p>
        <div>
          <span
            v-if="objection.emoticonPack.price == 0"
            class="text-md text-blue-500 font-nnsqneo-bold"
            >무료</span
          >
          <span v-else class="text-md text-red-500 font-nnsqneo-bold"
            >{{ objection.emoticonPack.price }} 포인트</span
          >
        </div>
      </div>
      <div class="flex flex-col justify-center items-end pr-2">
        <div
          class="text-red-500 font-nnsqneo-bold"
          v-if="objection.type == 'EXAMINE'"
        >
          AI 검수 통과 실패
        </div>
        <div
          class="text-red-500 font-nnsqneo-bold"
          v-if="objection.type == 'REPORT'"
        >
          신고 다수 발생
        </div>
      </div>
      <div class="flex flex-col justify-center">
        <button
          @click="isOpen = !isOpen"
          class="hover:bg-gray-200 focus:outline-none active:bg-gray-300 rounded-full flex items-center justify-center aspect-square w-10"
          :class="isOpen ? 'rotate-180' : ''"
          style="transform-origin: center; transition: transform 0.3s ease, background-color 0.2s ease; user-select: none;"
        >
          <span class="material-icons text-3xl ">keyboard_arrow_down</span>
        </button>
      </div>
    </div>
    <Transition name="fade">
      <div
        v-if="isOpen"
        class="flex flex-col max-h-[500px] overflow-hidden"
      >
        <hr class="border-gray-300 my-4" />
        <ObjectionImageGrid
          :emoticons="objection.emoticonPack.emoticons"
          :itemsPerPage="12"
        />
        <ManagerAction :objection="objection" />
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import type { BlacklistedEmoticon } from "@/types/blacklistResult";
import { ref } from "vue";
import EmoticonImageGrid from "./EmoticonImageGrid.vue";
import UserAction from "./UserAction.vue";
import ObjectionImageGrid from "./ObjectionImageGrid.vue";
import ManagerAction from "./ManagerAction.vue";
import type { Objection } from "@/types/objectionlistResult";
const isOpen = ref(false);

defineProps<{
  objection: Objection;
}>();
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: max-height 0.5s ease, filter 0.5s ease, opacity 0.5s ease,
    margin 0.5s ease, padding 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  max-height: 0;
  filter: blur(10px);
  opacity: 0;
  margin: 0;
  padding: 0;
}
</style>
