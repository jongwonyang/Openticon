<template>
  <div class="flex flex-col max-w-screen-lg mx-auto mt-2">
    <div class="flex flex-col justify-center px-4">
      <div class="flex flex-col sm:flex-row justify-end mt-2">
        <div class="w-full sm:w-1/4">
          <div class="text-xl text-black font-nnsqneo-bold">가격</div>
          <input
            type="number"
            v-model="price"
            placeholder="가격"
            class="w-full border-2 border-gray-400 rounded-md p-2 mt-2 focus:outline-none focus:border-slate-500"
          />
          <div class="mt-2 flex flex-col">
            <span class="text-xl text-black font-nnsqneo-bold">공개여부</span>
            <button
              @click="isPublic = !isPublic"
              class="bg-blue-500 text-white px-4 py-2 rounded-md w-full mt-2 flex flex-row items-center justify-center gap-2 border-2 transition-all duration-50"
              :class="{
                'bg-red-500 hover:bg-red-600 active:bg-red-700 border-red-500': !isPublic,
                'bg-green-500 hover:bg-green-600 active:bg-green-700 border-green-500': isPublic,
              }"
            >
              <span class="material-icons text-md text-right">
                {{ isPublic ? "visibility" : "visibility_off" }}
              </span>
              {{ isPublic ? "공개" : "비공개" }}
            </button>
          </div>
        </div>
        <div class="w-full pl-0 sm:pl-6 sm:w-3/4 pt-2 sm:pt-0">
          <div class="text-xl text-black font-nnsqneo-bold">태그</div>
          <div
            class="flex flex-row flex-wrap transition-all duration-50"
            :class="{ 'mt-2': tags.length > 0 }"
          >
            <TransitionGroup name="fade">
              <div
                v-for="tag in tags"
                :key="tag"
                class="border-2 border-gray-400 bg-gray-100 text-gray-700 rounded-full flex flex-row items-center justify-center overflow-hidden w-fit mr-2 mb-2"
              >
                <span class="text-md pl-4 pr-2 py-2 truncate">#{{ tag }}</span>
                <button
                  @click="handleTagRemove(tag)"
                  class="text-white flex items-center justify-center h-full px-2 bg-red-500 hover:bg-red-600 rounded-l-full"
                >
                  <span class="material-icons text-md text-right">close</span>
                </button>
              </div>
            </TransitionGroup>
          </div>
          <div
            class="flex flex-row gap-1 items-center transition-all duration-50"
            :class="{ 'mt-2': tags.length == 0 }"
          >
            <input
              type="text"
              v-model="tag"
              placeholder="태그"
              class="w-full border-2 border-gray-400 rounded-full px-3 py-2 focus:outline-none focus:border-slate-500"
              @keydown.enter="handleTagAdd"
            />
            <button
              @click="handleTagAdd"
              class="bg-blue-500 text-white px-4 py-2 rounded-full min-w-fit hover:bg-blue-600 active:bg-blue-700"
            >
              추가
            </button>
          </div>
          <div class="flex flex-row items-center h-4 mt-2">
            <Transition name="error">
              <div v-if="tagError" class="text-red-500 px-2">
                {{ tagErrorMessage }}
              </div>
            </Transition>
          </div>
        </div>
      </div>
      <button
        class="bg-blue-500 text-white px-4 py-2 rounded-md mt-4 hover:bg-blue-600 active:bg-blue-700"
        @click="handleUpload"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";

const emit = defineEmits<{
  (event: "submit"): void;
  (event: "update:price", price: number): void;
  (event: "update:tags", tags: string[]): void;
  (event: "update:isPublic", isPublic: boolean): void;
}>();

const isPublic = ref<boolean>(true);

const price = ref<number>(0);
const tags = ref<string[]>([]);
const tag = ref<string>("");
const tagError = ref<boolean>(false);
const tagErrorMessage = ref<string>("");

function handleTagAdd() {
  const trimmedTag = tag.value.trim();
  if (trimmedTag.length > 20) {
    tagError.value = true;
    tagErrorMessage.value = "20자 이상의 태그는 추가할 수 없습니다.";
    return;
  }
  if (tags.value.includes(trimmedTag)) {
    tagError.value = true;
    tagErrorMessage.value = "중복된 태그입니다.";
    return;
  }
  tags.value.push(trimmedTag);
  tag.value = "";
  tagError.value = false;
  tagErrorMessage.value = "";
}

function handleTagRemove(tagToRemove: string) {
  const index = tags.value.indexOf(tagToRemove);
  if (index !== -1) {
    tags.value.splice(index, 1);
  }
}

watch(price, (newValue) => {
  emit("update:price", newValue);
});

watch(tags, (newValue) => {
  emit("update:tags", newValue);
}, { deep: true });

watch(isPublic, (newValue) => {
  emit("update:isPublic", newValue);
});

function handleUpload() {
  emit("submit");
}
</script>

<style scoped>
/* textarea 크기 조정 비활성화 */
textarea {
  resize: none;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.4s ease;
  max-width: 400px;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  filter: blur(10px);
  max-width: 0;
  padding: 0;
  margin: 0;
}

.error-enter-active,
.error-leave-active {
  transition: all 0.2s ease;
}

.error-enter-from,
.error-leave-to {
  opacity: 0;
}
</style>
