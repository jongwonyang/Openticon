<template>
  <div class="max-w-screen-lg mx-auto p-4">
    <button
      @click="emit('update:displayedState', 'PENDING')"
      :class="props.displayedState === 'PENDING' ? 'bg-gray-200' : ''"
      class="rounded-full p-2 min-w-24"
    >
      접수 전
    </button>
    <button
      @click="emit('update:displayedState', 'RECEIVED')"
      :class="props.displayedState === 'RECEIVED' ? 'bg-gray-200' : ''"
      class="rounded-full p-2 min-w-24"
    >
      접수 완료
    </button>
    <button
      @click="emit('update:displayedState', 'APPROVED')"
      :class="props.displayedState === 'APPROVED' ? 'bg-gray-200' : ''"
      class="rounded-full p-2 min-w-24"
    >
      게시허용
    </button>
    <button
      @click="emit('update:displayedState', 'REJECTED')"
      :class="props.displayedState === 'REJECTED' ? 'bg-gray-200' : ''"
      class="rounded-full p-2 min-w-24"
    >
      게시거절
    </button>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  displayedState: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED";
}>();

const emit = defineEmits<{
  (
    e: "update:displayedState",
    state: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED"
  ): void;
}>();
</script>
