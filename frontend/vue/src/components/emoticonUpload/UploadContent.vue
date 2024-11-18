<template>
  <div class="flex flex-col max-w-screen-lg mx-auto mt-4">
    <div class="px-4">
      <div class="flex flex-row items-center">
        <span class="text-lg text-black font-nnsqneo-bold">이모티콘 이미지</span>
        <span class="text-red-500">*</span>
      </div>
      <VueDraggable
        v-model="emoticonImages"
        :animation="200"
        item-key="id"
        class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 mt-2"
        ghost-class="opacity-50"
        handle=".drag-handle"
        style="user-select: none"
      >
        <template #item="{ element }">
          <div class="drag-handle cursor-move col-s pan-1">
            <ImagePreview
              :image="element.file"
              @delete="handleEmoticonImageRemove(element)"
            />
          </div>
        </template>
        <template #footer>
          <ImageUploaderWOPreview @update:file="handleEmoticonImageAdd" />
        </template>
      </VueDraggable>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import ImageUploaderWOPreview from "@/components/emoticonUpload/ImageUploaderWOPreview.vue";
import ImagePreview from "@/components/emoticonUpload/ImagePreview.vue";
import VueDraggable from "vuedraggable";
import { watch } from "vue";

const emit = defineEmits<{
  (event: "update:emoticonImages", value: File[]): void;
}>();

type EmoticonImage = {
  id: number;
  file: File;
};

const emoticonImages = ref<EmoticonImage[]>([]);
let lastEmoticonImageId = 0;

function handleEmoticonImageAdd(file: File) {
  emoticonImages.value.push({
    id: lastEmoticonImageId++,
    file,
  });
}

function handleEmoticonImageRemove(image: EmoticonImage) {
  console.log("handleEmoticonImageRemove", image);
  const index = emoticonImages.value.findIndex(({ id }) => id === image.id);
  if (index !== -1) {
    emoticonImages.value.splice(index, 1);
  }
}

watch(emoticonImages, (newValue) => {
    emit("update:emoticonImages", newValue.map((image) => image.file));
  },
  { deep: true });
</script>

<style scoped>
.drag-handle {
  touch-action: none;
  width: 100%;
  height: 100%;
}

/* 드래그 중인 아이템 스타일 */
:deep(.sortable-ghost) {
  opacity: 0.5;
  background: #c8ebfb;
}

/* 드래그 대상 위치 표시 스타일 */
:deep(.sortable-drag) {
  opacity: 0.9;
}
</style>
