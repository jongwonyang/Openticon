<template>
    <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-8 w-96">
            <div class="flex justify-between items-center mb-6">
                <h2 class="text-2xl font-nnsqneo-extra-bold">로그인</h2>
                <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
                    <span class="text-2xl">&times;</span>
                </button>
            </div>

            <div class="space-y-4">
                <button @click="handleLogin('kakao')"
                    class="w-full py-3 px-4 bg-[#FEE500] hover:bg-[#fdd803] rounded-lg flex items-center justify-center">
                    <img src="@/assets/login-btn/kakao-logo.png" alt="카카오 로고" class="w-6 h-6 mr-2">
                    카카오로 시작하기
                </button>

                <button @click="handleLogin('naver')"
                    class="w-full py-3 px-4 bg-[#03C75A] hover:bg-[#02b351] text-white rounded-lg flex items-center justify-center">
                    <img src="@/assets/login-btn/naver-logo.png" alt="네이버 로고" class="w-6 h-6 mr-2">
                    네이버로 시작하기
                </button>

                <button @click="handleLogin('google')"
                    class="w-full py-3 px-4 bg-gray-100 hover:bg-gray-200 border border-gray-300 rounded-lg flex items-center justify-center">
                    <img src="@/assets/login-btn/google-logo.png" alt="구글 로고" class="w-6 h-6 mr-2">
                    구글로 시작하기
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user';
import { makeSuccessAlert } from '@/util/alert';

defineProps<{
    isOpen: boolean
}>();

const emit = defineEmits<{
    (e: 'close'): void
}>();

const BASE_URL = import.meta.env.VITE_API_BASE_URL;
const PAGE_BASE_URL = import.meta.env.VITE_APP_URL;
const REDIRECT_URI = `${PAGE_BASE_URL}/processLogin`;

function handleMessage(event: MessageEvent) {
    const userStore = useUserStore();
    userStore.login(event.data);
    makeSuccessAlert('로그인이 완료되었습니다. access_token: ' + event.data);
    closeModal();
    window.removeEventListener('message', handleMessage);
}

function handleLogin(provider: string) {
    const loginUrl = `${BASE_URL}/oauth2/authorization/${provider}?redirect_uri=${REDIRECT_URI}&mode=login`;
    console.log(loginUrl);
    window.open(loginUrl, '_blank', 'popup,width=600,height=600');
    window.addEventListener('message', handleMessage);
}

function closeModal() {
    emit('close');
}
</script>
