<script setup lang="ts">
import { ref } from 'vue';
import ImageUploader from '@/components/emoticonUpload/ImageUploader.vue';
import ImageUploaderWOPreview from '@/components/emoticonUpload/ImageUploaderWOPreview.vue';
import ImagePreview from '@/components/emoticonUpload/ImagePreview.vue';
import VueDraggable from 'vuedraggable';

const description = ref<string>('');

type EmoticonImage = {
  id: number;
  file: File;
};

let lastEmoticonImageId = 0;

const thumbnailFile = ref<File | null>(null);
const listFile = ref<File | null>(null);
const selectedCategory = ref<string>('');
const emoticonImages = ref<EmoticonImage[]>([]);

const price = ref<number>(0);
const tags = ref<string[]>([]);
const tag = ref<string>('');

const categories = [
  { value: 'REAL', label: '실사' },
  { value: 'ENTERTAINMENT', label: '방송연예' },
  { value: 'CHARACTER', label: '캐릭터' },
  { value: 'TEXT', label: '텍스트' },
];

function handleThumbnailUpdate(file: File) {
  thumbnailFile.value = file;
}

function handleListImageUpdate(file: File) {
  listFile.value = file;
}

function handleEmoticonImageAdd(file: File) {
  emoticonImages.value.push({
    id: lastEmoticonImageId++,
    file,
  });
}

function handleEmoticonImageRemove(image: EmoticonImage) {
  console.log('handleEmoticonImageRemove', image);
  const index = emoticonImages.value.findIndex(({ id }) => id === image.id);
  if (index !== -1) {
    emoticonImages.value.splice(index, 1);
  }
}

function handleTagAdd() {
  const trimmedTag = tag.value.trim();
  if (trimmedTag && !tags.value.includes(trimmedTag)) {
    tags.value.push(trimmedTag);
    tag.value = '';
  }
}

function handleTagRemove(tagToRemove: string) {
  const index = tags.value.indexOf(tagToRemove);
  if (index !== -1) {
    tags.value.splice(index, 1);
  }
}
</script>

<template>
  <div class="flex flex-col max-w-screen-lg mx-auto mt-10 px-4">
    <div class="flex flex-row items-center justify-around mx-0 md:mx-36">
      <ImageUploader label="대표 이미지" @update:file="handleThumbnailUpdate" />
      <ImageUploader label="목록 이미지" @update:file="handleListImageUpdate" />
    </div>

    <div class="flex flex-col md:flex-row items-center justify-center mx-0 md:mx-36 mt-4">
      <input type="text" placeholder="이모티콘 이름"
        class="w-full md:w-2/3 h-10 border-2 border-gray-400 rounded-md mr-0 md:mr-2 p-2" />
      <select v-model="selectedCategory"
        class="w-full md:w-1/3 h-10 border-2 border-gray-400 rounded-md p-2 mt-2 md:mt-0">
        <option value="" disabled>카테고리 선택</option>
        <option v-for="category in categories" :key="category.value" :value="category.value">
          {{ category.label }}
        </option>
      </select>
    </div>
    <div class="pt-4 px-4">
      <div class="text-xl text-black font-nnsqneo-bold">이모티콘 이미지</div>
      <VueDraggable v-model="emoticonImages" :animation="200" item-key="id"
        class="grid grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 mt-2" ghost-class="opacity-50" handle=".drag-handle"
        style="user-select: none;">
        <template #item="{ element }">
          <div class="drag-handle cursor-move col-span-1">
            <ImagePreview :image="element.file" @delete="handleEmoticonImageRemove(element)" />
          </div>
        </template>
        <template #footer>
          <ImageUploaderWOPreview @update:file="handleEmoticonImageAdd" />
        </template>
      </VueDraggable>
    </div>
    <div class="flex flex-col justify-center mt-4 px-4">
      <div class="text-xl text-black font-nnsqneo-bold">설명</div>
      <textarea v-model="description" placeholder="이모티콘 설명" class="w-full border-2 border-gray-400 rounded-md p-2 mt-2"
        rows="5">
      </textarea>
      <div class="flex flex-col sm:flex-row justify-end mt-2">
        <div class="w-full sm:w-1/4">
          <div class="text-xl text-black font-nnsqneo-bold">가격</div>
          <input type="number" v-model="price" placeholder="가격" class="w-full border-2 border-gray-400 rounded-md p-2 mt-2"/>
        </div>
        <div class="w-full pl-0 sm:pl-6 sm:w-3/4 pt-2 sm:pt-0">
          <div class="text-xl text-black font-nnsqneo-bold">태그</div>
          <div class="flex flex-row flex-wrap gap-2" :class="{ 'mt-2': tags.length > 0 }">
            <div v-for="tag in tags" :key="tag" class="border-2 border-gray-400 bg-gray-100 text-gray-700 rounded-md p-2 w-fit min-w-fit">
              {{ tag }}
              <button @click="handleTagRemove(tag)" class="text-red-500">삭제</button>
            </div>
          </div>
          <div class="flex flex-row gap-1 mt-2 items-center">
              <input type="text" v-model="tag" placeholder="태그" class="w-full border-2 border-gray-400 rounded-md p-2"/>
              <button @click="handleTagAdd" class="bg-blue-500 text-white px-4 py-2 rounded-md min-w-fit">추가</button> 
            </div>
        </div>
      </div>
      <button class="bg-blue-500 text-white px-4 py-2 rounded-md mt-4">등록</button>
    </div>
    <div class="mt-4 mx-4 bg-gray-100 text-gray-600 rounded-md p-4 mb-24">
      <p>1. 이모티콘 이미지는 폭력, 성희롱, 음란물 등 부적절한 이미지는 등록할 수 없습니다.</p>
      <p>2. 이모티콘의 가격은 100원 단위로 입력해야 합니다.</p>
      <p>3. 이모티콘 팩 태그에는 최대 5개의 태그를 입력할 수 있습니다.</p>
      <p>4. 가로세로 200px이상의 정사각형 이미지만 등록할 수 있습니다.</p>
      <p>&nbsp;&nbsp;4-1. 200px이상의 이미지는 200px로 자동 조정됩니다.</p>
      <p>5. 이미지는 jpg, png, gif 형식만 등록할 수 있습니다.</p>
      <p>6. 저작권 침해 또는 명예훼손 등 법적 문제가 있는 이미지는 관리자에 의해 삭제될 수 있습니다.</p>
      <p>7. 사용자의 신고 누적으로 인해 이모티콘 팩이 표시중단 될 수 있습니다.</p>
    </div>
  </div>
</template>

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

/* textarea 크기 조정 비활성화 */
textarea {
  resize: none;
}
</style>
