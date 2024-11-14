<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center"
  >
    <div class="fixed inset-0 bg-black opacity-50"></div>
    <div
      class="relative bg-white rounded-lg max-w-2xl w-full mx-4 max-h-[90vh]"
    >
      <h2
        class="text-2xl font-nnsqneo-heavy sticky top-0 p-6 z-10 whitespace-nowrap"
      >
        이모티콘 팩 업로드 확인
      </h2>

      <div
        class="overflow-y-scroll overflow-x-hidden px-4"
        style="max-height: calc(90vh - 14rem)"
      >
        <EmoticonDetailHeader :emoticon="emoticonPack" />
        <hr />
        <EmoticonDetailContent :emoticon="emoticonPack" />
        <hr />
        <EmoticonDetailFooter :emoticon="emoticonPack" />
      </div>
      <div v-if="!isUploaded" class="flex justify-end gap-2 p-6">
        <div class="flex justify-center items-center">이대로 업로드할까요?</div>
        <button
          class="border border-red-500 bg-red-500 text-white rounded-md px-4 py-2 hover:bg-red-600 transition-all duration-200 ease-in-out active:bg-red-700"
          @click="closeModal"
        >
          취소
        </button>
        <button
          class="border border-blue-500 bg-blue-500 text-white rounded-md px-4 py-2 hover:bg-blue-600 transition-all duration-200 ease-in-out active:bg-blue-700"
          @click="handleConfirm"
          :class="{
            'bg-gray-500 border-gray-500 hover:bg-gray-500 active:bg-gray-500':
              isUploading,
          }"
          v-if="!isFailed"
        >
          <span v-if="!isUploading">업로드</span>
          <span v-else>업로드중... {{ uploadProgress }}%</span>
        </button>
        <button
          class="border border-orange-500 bg-orange-500 text-white rounded-md px-4 py-2 hover:bg-orange-600 transition-all duration-200 ease-in-out active:bg-orange-700"
          v-if="isFailed"
        >
          업로드 실패!
        </button>
      </div>
      <div v-if="isUploaded" class="flex justify-end gap-2 p-6">
        <RouterLink :to="{ name: 'mypage' }" class="border border-slate-500 bg-slate-500 text-white rounded-md px-4 py-2 hover:bg-slate-600 transition-all duration-200 ease-in-out active:bg-slate-700">업로드 완료! 마이페이지로 가기</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type {
  EmoticonPackUploadFiles,
  EmoticonPackUploadInfo,
} from "@/types/emoticonPackUpload";
import { ref, computed, watch } from "vue";
import EmoticonDetailHeader from "./Confirm/EmoticonDetailHeader.vue";
import type { EmoticonPack } from "@/types/emoticonPack";
import { useUserStore } from "@/stores/user";
import EmoticonDetailContent from "./Confirm/EmoticonDetailContent.vue";
import EmoticonDetailFooter from "./Confirm/EmoticonDetailFooter.vue";
import axios, { type CancelTokenSource } from "axios";

const props = defineProps<{
  emoticonPackUploadInfo: EmoticonPackUploadInfo;
  emoticonPackUploadFiles: EmoticonPackUploadFiles;
  isOpen: boolean;
}>();

const emit = defineEmits<{
  (e: "update:isOpen", value: boolean): void;
}>();

const emoticonPack = ref<EmoticonPack | null>(null);

const userStore = useUserStore();

watch(props, (newVal) => {
  if (newVal.isOpen) {
    emoticonPack.value = {
      author: {
        id: userStore.userInfo?.id ?? 0,
        nickname: userStore.userInfo?.nickname ?? "",
        profile: userStore.userInfo?.profile_image ?? "",
      },
      aigenerated: false,
      createdAt: new Date().toISOString(),
      id: 0,
      title: newVal.emoticonPackUploadInfo.packTitle,
      price: newVal.emoticonPackUploadInfo.price,
      category: newVal.emoticonPackUploadInfo.category,
      description: newVal.emoticonPackUploadInfo.description,
      thumbnailImg: newVal.emoticonPackUploadFiles.thumbnailImg ? URL.createObjectURL(newVal.emoticonPackUploadFiles.thumbnailImg) : "",
      listImg: newVal.emoticonPackUploadFiles.listImg ? URL.createObjectURL(newVal.emoticonPackUploadFiles.listImg) : "",
      emoticons: newVal.emoticonPackUploadFiles.emoticons.map((file) =>
        URL.createObjectURL(file)
      ),
      tags: newVal.emoticonPackUploadInfo.tags,
      view: 0,
      public: newVal.emoticonPackUploadInfo.isPublic,
      blacklist: false,
      sharedLink: "",
    };
  }
});

const emoticonPackStore = useEmoticonPackStore();

const isUploading = ref(false);
const isUploaded = ref(false);
const isFailed = ref(false);
const uploadProgress = ref(0);
const cancelToken = ref<CancelTokenSource | null>(null);

function closeModal() {
  if (cancelToken.value) {
    cancelToken.value.cancel();
  }
  emit("update:isOpen", false);
}

function handleConfirm() {
  if (isUploading.value) return;
  isUploading.value = true;
  cancelToken.value = axios.CancelToken.source();
  emoticonPackStore
    .uploadEmoticonPack(
      props.emoticonPackUploadInfo,
      props.emoticonPackUploadFiles,
      (progressEvent) => {
        uploadProgress.value = Math.round(progressEvent.loaded / progressEvent.total! * 100);
      },
      cancelToken.value
    )
    .then((e) => {
      isUploading.value = false;
      isUploaded.value = true;
      cancelToken.value = null;
    }).catch((e) => {
      isUploading.value = false;
      isFailed.value = true;
      setTimeout(() => {
        isFailed.value = false;
      }, 3000);
    });
}
</script>
