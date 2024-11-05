<template>
  <div
    class="mx-1 mt-1 border rounded-md flex items-center p-4 overflow-hidden shadow-md"
    :class="[
      backgroundColor[alert.type],
      borderColor[alert.type],
      textColor[alert.type],
    ]"
  >
    <i class="mdi text-xl mr-2" :class="alertIcon[alert.type]"></i>
    <span class="text-lg font-bold mr-2 min-w-fit">
      {{ alert.title }}
    </span>
    <span class="truncate">{{ alert.message }}</span>
    <span class="flex-grow"></span>
    <button class="ml-2 text-nowrap" @click="closeAlert">
      <!-- <i class="mdi mdi-close text-2xl"></i> -->
      닫기
    </button>
  </div>
</template>

<script setup lang="ts">
import type { Alert } from "@/stores/alert";

const alertIcon = {
  normal: "mdi-information-outline",
  special: "mdi-information-outline",
  error: "mdi-close",
  success: "mdi-check",
  warning: "mdi-alert-outline",
};

const backgroundColor = {
  normal: "bg-slate-200",
  special: "bg-gradient-to-r from-pink-300 via-sky-300 to-green-300",
  error: "bg-red-200",
  success: "bg-green-200",
  warning: "bg-yellow-200",
};

const textColor = {
  normal: "text-slate-900",
  special: "text-gray-900",
  error: "text-red-900",
  success: "text-green-900",
  warning: "text-yellow-900",
};

const borderColor = {
  normal: "border-slate-400",
  special: "border-sky-400",
  error: "border-red-400",
  success: "border-green-400",
  warning: "border-yellow-400",
};

const progressBarColor = {
  normal: "bg-slate-400",
  special: "bg-pink-400",
  error: "bg-red-400",
  success: "bg-green-400",
  warning: "bg-yellow-400",
};

const props = defineProps<{
  alert: Alert;
}>();

const emit = defineEmits<{
  (e: "close"): void; // ID를 전달하는 이벤트 정의
}>();

const closeAlert = () => {
  emit("close"); // ID를 부모에게 전달
};
</script>

<style scoped></style>
