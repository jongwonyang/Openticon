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
          <span v-else>업로드중...</span>
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
      thumbnailImg: URL.createObjectURL(
        newVal.emoticonPackUploadFiles.thumbnailImg
      ),
      listImg: URL.createObjectURL(newVal.emoticonPackUploadFiles.listImg),
      emoticons: newVal.emoticonPackUploadFiles.emoticons.map((file) =>
        URL.createObjectURL(file)
      ),
      tags: newVal.emoticonPackUploadInfo.tags,
      view: 0,
      public: newVal.emoticonPackUploadInfo.isPublic,
      blacklist: false,
    };
  }
});

const emoticonPackStore = useEmoticonPackStore();

const isUploading = ref(false);
const isUploaded = ref(false);
const isFailed = ref(false);
function closeModal() {
  emit("update:isOpen", false);
}

function handleConfirm() {
  if (isUploading.value) return;
  isUploading.value = true;
  emoticonPackStore
    .uploadEmoticonPack(
      props.emoticonPackUploadInfo,
      props.emoticonPackUploadFiles
    )
    .then((e) => {
      isUploading.value = false;
      isUploaded.value = true;
    }).catch((e) => {
      isUploading.value = false;
      isFailed.value = true;
      setTimeout(() => {
        isFailed.value = false;
      }, 3000);
    });

//     {
//     "id": 127,
//     "title": "레니 테스트",
//     "member": {
//         "id": 5,
//         "email": "pcs3373@naver.com",
//         "nickname": "새로운 사용자5"
//     },
//     "price": 0,
//     "view": 0,
//     "category": "REAL",
//     "thumbnailImg": "https://apitest.openticon.store/static/upload/images/2aed7d70-1d28-467e-bf4a-ddaa68996fd1..jpg",
//     "listImg": "https://apitest.openticon.store/static/upload/images/18e8a5bf-438e-45c0-beb4-656c6a5d5b1b..jpg",
//     "description": "",
//     "shareLink": "4392cb9f-37b0-44b5-a33c-c7b0d38eed93",
//     "createdAt": "2024-11-07 13:26:32:67",
//     "updatedAt": "2024-11-07 13:26:32:67",
//     "tags": [],
//     "public": true,
//     "aiGenerated": false,
//     "blacklist": false
// }

    // setTimeout(() => {
    //   isUploading.value = false;
    //   isUploaded.value = true;
    // }, 3000);
}
</script>
