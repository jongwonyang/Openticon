<template>
  <CreateBanner />
  <CreateHeader
    @update:thumbnailFile="handleThumbnailFileUpdate"
    @update:listFile="handleListFileUpdate"
    @update:packTitle="handlePackTitleUpdate"
    @update:selectedCategory="handleCategoryUpdate"
    @update:description="handleDescriptionUpdate"
  />
  <CreateContent @update:emoticonImages="handleEmoticonImagesUpdate" />
  <CreateFooter
    @update:price="handlePriceUpdate"
    @update:tags="handleTagsUpdate"
    @update:isPublic="handleIsPublicUpdate"
    @submit="handleSubmit"
  />
  <ConfirmModal
    v-model:isOpen="isModalOpen"
    :emoticon-pack-upload-info="emoticonPackUploadInfo"
    :emoticon-pack-upload-files="emoticonPackUploadFiles"
  />
</template>

<script setup lang="ts">
import CreateBanner from "@/components/emoticonCreate/CreateBanner.vue";
import CreateHeader from "@/components/emoticonCreate/CreateHeader.vue";
import CreateContent from "@/components/emoticonCreate/CreateContent.vue";
import CreateFooter from "@/components/emoticonCreate/CreateFooter.vue";
import ConfirmModal from "@/components/emoticonCreate/ConfirmModal.vue";

import { ref } from "vue";
import type {
  EmoticonPackUploadInfo,
  EmoticonPackUploadFiles,
} from "@/types/emoticonPackUpload";
import { useEmoticonPackStore } from "@/stores/emoticonPack";

const emoticonPackStore = useEmoticonPackStore();

const emoticonPackUploadInfo = ref<EmoticonPackUploadInfo>({
  packTitle: "",
  isAiGenerated: true,
  isPublic: true,
  category: "REAL",
  description: "",
  price: 0,
  tags: [],
});

const emoticonPackUploadFiles = ref<EmoticonPackUploadFiles>({
  thumbnailImg: null,
  listImg: null,
  emoticons: [],
});

const isModalOpen = ref(false);

function handleThumbnailFileUpdate(file: File) {
  emoticonPackUploadFiles.value.thumbnailImg = file;
}

function handleListFileUpdate(file: File) {
  emoticonPackUploadFiles.value.listImg = file;
}

function handlePackTitleUpdate(title: string) {
  emoticonPackUploadInfo.value.packTitle = title;
}

const requiredCategory = ref<string>("");

function handleCategoryUpdate(category: string) {
  requiredCategory.value = category;
  emoticonPackUploadInfo.value.category = category as
    | "REAL"
    | "CHARACTER"
    | "ENTERTAINMENT"
    | "TEXT";
}

function handleDescriptionUpdate(description: string) {
  emoticonPackUploadInfo.value.description = description;
}

function handleEmoticonImagesUpdate(emoticonImages: File[]) {
  emoticonPackUploadFiles.value.emoticons = emoticonImages;
}

function handlePriceUpdate(price: number) {
  emoticonPackUploadInfo.value.price = price;
}

function handleTagsUpdate(tags: string[]) {
  emoticonPackUploadInfo.value.tags = tags;
}

function handleIsPublicUpdate(isPublic: boolean) {
  emoticonPackUploadInfo.value.isPublic = isPublic;
}

function handleSubmit() {
  if (emoticonPackUploadInfo.value.packTitle === "") {
    alert("이모티콘 팩 이름을 입력해주세요.");
    return;
  }
  if (requiredCategory.value === "") {
    alert("카테고리를 선택해주세요.");
    return;
  }
  if (emoticonPackUploadFiles.value.thumbnailImg === null) {
    alert("이모티콘 대표 이미지를 추가해주세요.");
    return;
  }
  if (emoticonPackUploadFiles.value.listImg === null) {
    alert("이모티콘 목록 이미지를 추가해주세요.");
    return;
  }
  if (emoticonPackUploadFiles.value.emoticons.length === 0) {
    alert("이모티콘을 최소 하나 이상 추가해주세요.");
    return;
  }
  isModalOpen.value = true;
}
</script>
