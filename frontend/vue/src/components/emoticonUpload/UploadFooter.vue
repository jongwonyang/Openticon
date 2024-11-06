<template>
  <div class="flex flex-col max-w-screen-lg mx-auto mt-10 px-4">
    <div class="flex flex-col justify-center mt-4 px-4">
      <div class="text-xl text-black font-nnsqneo-bold">설명</div>
      <textarea
        v-model="description"
        placeholder="이모티콘 설명"
        class="w-full border-2 border-gray-400 rounded-md p-2 mt-2"
        rows="5"
      >
      </textarea>
      <div class="flex flex-col sm:flex-row justify-end mt-2">
        <div class="w-full sm:w-1/4">
          <div class="text-xl text-black font-nnsqneo-bold">가격</div>
          <input
            type="number"
            v-model="price"
            placeholder="가격"
            class="w-full border-2 border-gray-400 rounded-md p-2 mt-2"
          />
        </div>
        <div class="w-full pl-0 sm:pl-6 sm:w-3/4 pt-2 sm:pt-0">
          <div class="text-xl text-black font-nnsqneo-bold">태그</div>
          <div
            class="flex flex-row flex-wrap gap-2"
            :class="{ 'mt-2': tags.length > 0 }"
          >
            <div
              v-for="tag in tags"
              :key="tag"
              class="border-2 border-gray-400 bg-gray-100 text-gray-700 rounded-md p-2 w-fit min-w-fit"
            >
              {{ tag }}
              <button @click="handleTagRemove(tag)" class="text-red-500">
                삭제
              </button>
            </div>
          </div>
          <div class="flex flex-row gap-1 mt-2 items-center">
            <input
              type="text"
              v-model="tag"
              placeholder="태그"
              class="w-full border-2 border-gray-400 rounded-md p-2"
            />
            <button
              @click="handleTagAdd"
              class="bg-blue-500 text-white px-4 py-2 rounded-md min-w-fit"
            >
              추가
            </button>
          </div>
        </div>
      </div>
      <button
        class="bg-blue-500 text-white px-4 py-2 rounded-md mt-4"
        @click="handleUpload"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const emit = defineEmits<{
  (event: "submit"): void;
}>();

const description = ref<string>("");

const price = ref<number>(0);
const tags = ref<string[]>([]);
const tag = ref<string>("");

function handleTagAdd() {
  const trimmedTag = tag.value.trim();
  if (trimmedTag && !tags.value.includes(trimmedTag)) {
    tags.value.push(trimmedTag);
    tag.value = "";
  }
}

function handleTagRemove(tagToRemove: string) {
  const index = tags.value.indexOf(tagToRemove);
  if (index !== -1) {
    tags.value.splice(index, 1);
  }
}

function handleUpload() {
  emit("submit");
}
</script>

<style scoped>
/* textarea 크기 조정 비활성화 */
textarea {
  resize: none;
}
</style>
