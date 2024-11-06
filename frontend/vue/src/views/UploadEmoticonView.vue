<template>
  <UploadBanner />
  <UploadHeader @update:thumbnailFile="handleThumbnailFileUpdate" @update:listFile="handleListFileUpdate" @update:packTitle="handlePackTitleUpdate" @update:selectedCategory="handleCategoryUpdate" />
  <UploadContent @update:emoticonImages="handleEmoticonImagesUpdate" />
  <UploadFooter @submit="handleSubmit" />
  <UploadRule />
</template>

<script setup lang="ts">
import UploadBanner from "@/components/emoticonUpload/UploadBanner.vue";
import UploadHeader from "@/components/emoticonUpload/UploadHeader.vue";
import UploadContent from "@/components/emoticonUpload/UploadContent.vue";
import UploadFooter from "@/components/emoticonUpload/UploadFooter.vue";
import UploadRule from "@/components/emoticonUpload/UploadRule.vue";
import { ref } from "vue";
import type { EmoticonPackUploadInfo, EmoticonPackUploadFiles } from "@/types/emoticonPackUpload";
import { useEmoticonPackStore } from "@/stores/emoticonPack";

const emoticonPackStore = useEmoticonPackStore();

const emoticonPackUploadInfo = ref<EmoticonPackUploadInfo>({
  packTitle: "",
  isAiGenerated: false,
  isPublic: true,
  category: "REAL",
  description: "",
  price: 0,
  tags: [],
});

const emoticonPackUploadFiles = ref<EmoticonPackUploadFiles>({
  thumbnailImg: new File([], ""),
  listImg: new File([], ""),
  emoticons: [],
});

function handleThumbnailFileUpdate(file: File) {
  emoticonPackUploadFiles.value.thumbnailImg = file;
}

function handleListFileUpdate(file: File) {
  emoticonPackUploadFiles.value.listImg = file;
} 

function handlePackTitleUpdate(title: string) {
  emoticonPackUploadInfo.value.packTitle = title;
} 

function handleCategoryUpdate(category: string) {
  emoticonPackUploadInfo.value.category = category as "REAL" | "CHARACTER" | "ENTERTAINMENT" | "LETTER";
} 

function handleEmoticonImagesUpdate(emoticonImages: File[]) {
  emoticonPackUploadFiles.value.emoticons = emoticonImages;
}

function handleSubmit() {
  emoticonPackStore.uploadEmoticonPack(emoticonPackUploadInfo.value, emoticonPackUploadFiles.value);
}
</script>