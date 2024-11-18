<template>
  <div
    v-if="emoticon.state == 'PENDING'"
    class="flex flex-row justify-end items-center mt-4 gap-2"
  >
    <input
      v-model="objectionReason"
      type="text"
      class="w-full border border-gray-300 rounded-md p-2 focus:outline-none focus:border-slate-500"
      placeholder="이의제기 사유를 입력해주세요."
    />
    <button
      class="bg-blue-500 text-white px-4 py-2 rounded-md flex-shrink-0"
      @click="objection"
    >
      이의제기하기
    </button>
  </div>
  <div v-if="emoticon.state == 'RECEIVED'">
    <p class="text-gray-500 font-nnsqneo-bold pt-4">
      관리자가 신청한 이의제기에 대해 처리중입니다.
    </p>
  </div>
  <div v-if="emoticon.state == 'APPROVED'">
    <p class="text-green-500 font-nnsqneo-bold">
      관리자가 신청한 이의제기를 승인하고 이모티콘 팩을 게시허용했습니다.
    </p>
  </div>
  <div v-if="emoticon.state == 'REJECTED'">
    <p class="text-red-500 font-nnsqneo-bold">
      관리자가 신청한 이의제기를 거절하고 이모티콘 팩을 게시중단했습니다.
    </p>
  </div>
</template>

<script setup lang="ts">
import { useObjectionStore } from "@/stores/objection";
import type { BlacklistedEmoticon } from "@/types/blacklistResult";
import { onMounted, ref } from "vue";

const objectionReason = ref("");

const props = defineProps<{
  emoticon: BlacklistedEmoticon;
}>();

const objectionStore = useObjectionStore();

const objection = () => {
  objectionStore.makeObjection(props.emoticon.id, objectionReason.value);
  props.emoticon.state = "RECEIVED";
};

onMounted(() => {
  console.log(props.emoticon);
});
</script>