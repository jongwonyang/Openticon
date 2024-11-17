<template>
  <div class="mt-4">
    이의제기사유 : {{ props.objection.submitRequest }}
  </div>
  <div
    class="flex flex-row justify-end items-center mt-4 gap-2"
  >
    <input
      v-model="answerContent"
      type="text"
      class="w-full border border-gray-300 rounded-md p-2 focus:outline-none focus:border-slate-500"
      placeholder="승인 혹은 거절 사유를 입력해주세요."
    />
    <button
      class="bg-blue-500 text-white px-4 py-2 rounded-md flex-shrink-0"
      @click="handleObjection('APPROVED')"
    >
      승인
    </button>
    <button
      class="bg-red-500 text-white px-4 py-2 rounded-md flex-shrink-0"
      @click="handleObjection('REJECTED')"
    >
      거절
    </button>
  </div>
</template>

<script setup lang="ts">
import { useObjectionStore } from "@/stores/objection";
import type { Objection } from "@/types/objectionlistResult";
import { onMounted, ref } from "vue";

const answerContent = ref("");

const props = defineProps<{
  objection: Objection;
}>();

const objectionStore = useObjectionStore();

const handleObjection = (reportStateType: string) => {
  objectionStore.handleObjection(
    props.objection.id,
    reportStateType,
    answerContent.value
  );
  props.objection.state = reportStateType as
    | "PENDING"
    | "RECEIVED"
    | "APPROVED"
    | "REJECTED";
};

onMounted(() => {
  console.log(props.objection);
});
</script>