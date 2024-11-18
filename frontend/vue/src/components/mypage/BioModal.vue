<template>
  <div
    v-if="show"
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
  >
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-bold">상태메시지 변경</h2>
        <button
          @click="$emit('close')"
          class="text-gray-500 hover:text-gray-700"
        >
          <span class="material-icons">close</span>
        </button>
      </div>

      <div class="mb-6">
        <label class="block text-gray-700 text-sm font-bold mb-2">
          새로운 상태메시지
        </label>
        <input
          v-model="newBio"
          type="text"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
          :placeholder="currentBio"
          @keyup.enter="handleUpdate"
        />
        <p v-if="errorMessage" class="text-red-500 text-sm mt-1">
          {{ errorMessage }}
        </p>
      </div>

      <div class="flex justify-end gap-2">
        <button
          @click="$emit('close')"
          class="px-4 py-2 text-gray-600 hover:text-gray-800"
        >
          취소
        </button>
        <button
          @click="handleUpdate"
          :disabled="!isValid || isUpdating"
          class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          {{ isUpdating ? "변경 중..." : "변경하기" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { useUserStore } from "@/stores/user";

const props = defineProps<{
  show: boolean;
  currentBio?: string;
}>();

const emit = defineEmits<{
  (e: "close"): void;
}>();

const userStore = useUserStore();
const newBio = ref("");
const errorMessage = ref("");
const isUpdating = ref(false);

const isValid = computed(() => {
  const bio = newBio.value.trim();
  if (bio === props.currentBio) {
    return false;
  }
  if (bio.length > 255) {
    return false;
  }
  return true;
});

const handleUpdate = async () => {
  if (!isValid.value) return;

  try {
    isUpdating.value = true;
    errorMessage.value = "";

    await userStore.updateBio(newBio.value.trim());
    userStore.retrieveUserInfo();
    emit("close");
  } catch (error) {
    if (error instanceof Error) {
      errorMessage.value = error.message;
    } else {
      errorMessage.value = "상태메시지 변경 중 오류가 발생했습니다.";
    }
  } finally {
    isUpdating.value = false;
  }
};
</script>
