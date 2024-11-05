<template>
    <Transition name="fade">
        <div v-if="isOpen"
            class="absolute bg-white border border-gray-300 rounded-lg rounded-tr-3xl shadow-lg z-10 w-72 h-28 right-0 top-0 p-2 overflow-hidden" ref="menuTarget">
            <div class="flex flex-col h-14 justify-center items-start pl-2 pr-12">
                <div class="text-lg font-nnsqneo-heavy text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ userStore.userInfo?.nickname }}</div>
                <div class="text-sm text-gray-500 text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ userStore.userInfo?.email }}</div>
            </div>
            <div class="grid grid-cols-2">
                <div @click="goToMypage"
                    class="p-2 cursor-pointer transition-colors duration-200 hover:bg-gray-200 flex justify-center items-center rounded-md text-nowrap">
                    <span class="material-icons">person</span><span class="ml-2">마이페이지</span>
                </div>
                <div @click="logout"
                    class="p-2 cursor-pointer transition-colors duration-200 hover:bg-gray-200 flex justify-center items-center rounded-md text-nowrap">
                    <span class="material-icons">logout</span><span class="ml-2">로그아웃</span>
                </div>
            </div>
        </div>
    </Transition>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user';
import { defineEmits, ref } from 'vue';
import { useRouter } from 'vue-router';
import { onClickOutside } from '@vueuse/core'

const emit = defineEmits();

const userStore = useUserStore();

const props = defineProps({
    isOpen: {
        type: Boolean,
        required: true
    },
    profileImageElement: {
        type: Object as () => Record<string, any> | null,
    }
});

function logout() {
    const userStore = useUserStore();
    userStore.logout();
    emit('close');
    window.location = import.meta.env.VITE_APP_URL;
}

const router = useRouter();

function goToMypage() {
    emit('close');
    router.push({ name: 'mypage' });
}


const menuTarget = ref(null)

onClickOutside(menuTarget, (e: MouseEvent) => {
    if (e.target === props.profileImageElement) return;
    emit('close');
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: width 0.3s ease, height 0.3s ease, filter 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    width: 0;
    height: 0;
    filter: blur(30px);
}
</style>