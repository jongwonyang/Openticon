<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- 배경 오버레이 -->
    <div class="absolute inset-0 bg-black bg-opacity-50" @click="close"></div>
    
    <!-- 모달 내용 -->
    <div class="relative bg-white rounded-lg p-6 w-[90%] max-w-md">
      <h3 class="text-lg font-bold mb-4">공유하기</h3>
      
      <div class="flex flex-col gap-4">
        <button
          @click="handleShare('clipboard')"
          class="flex items-center gap-2 p-3 hover:bg-gray-100 rounded-lg transition"
        >
          <i class="material-symbols-outlined">content_copy</i>
          <span>링크 복사하기</span>
        </button>
        
        <button
          v-if="canUseShareAPI"
          @click="handleShare('native')"
          class="flex items-center gap-2 p-3 hover:bg-gray-100 rounded-lg transition"
        >
          <i class="material-symbols-outlined">share</i>
          <span>다른 앱으로 공유하기</span>
        </button>
      </div>
      
      <button
        @click="close"
        class="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
      >
        <i class="material-symbols-outlined">close</i>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EmoticonPack } from '@/types/emoticonPack';
import { ref, onMounted } from 'vue';

const props = defineProps<{
  emoticon: EmoticonPack | null;
  isOpen: boolean;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
}>();

const canUseShareAPI = ref(false);

onMounted(() => {
  canUseShareAPI.value = !!navigator.share;
});

const close = () => {
  emit('close');
};

const shareUrl = import.meta.env.VITE_SHARE_URL;

const handleShare = async (type: 'clipboard' | 'native') => {
  try {
    if (type === 'native' && navigator.share) {
      await navigator.share({
        title: props.emoticon?.title,
        text: props.emoticon?.description,
        url: `${shareUrl}/${props.emoticon?.sharedLink}`,
      });
    } else {
      await navigator.clipboard.writeText(window.location.href);
    }
    close();
  } catch (error) {
    if (error instanceof Error && error.name !== 'AbortError') {
      console.error('공유 중 오류가 발생했습니다:', error);
    }
  }
};
</script> 