<template>
    <div class="container mx-auto max-w-screen-lg">

        <h1 class="text-2xl font-nnsqneo-bold mt-4 px-4">마이페이지</h1>

        <div class="mt-4">
            <div class="flex flex-col gap-2">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
                    <div class="w-full flex flex-col justify-center items-center sm:items-end">
                        <div class="w-48 relative mx-4">
                            <img :src="userStore.userInfo?.profile_image" alt="프로필 이미지"
                                class="w-full object-cover rounded-full" />
                            <button
                                class="absolute top-0 right-0 bg-blue-500 text-white p-2 h-12 w-12 rounded-full flex justify-center items-center hover:bg-blue-600 hover:shadow-lg">
                                <span class="material-icons text-3xl">
                                    edit
                                </span>
                            </button>
                        </div>
                    </div>
                    <div class="flex flex-col gap-1 text-center justify-center sm:text-left mx-4">
                        <div class="flex items-center justify-center sm:justify-start">
                            <span class="text-3xl font-nnsqneo-heavy">{{ userStore.userInfo?.nickname }}</span>
                            <button class="text-sm text-gray-500">
                                <span class="material-symbols-outlined">
                                    edit_square
                                </span>
                            </button>
                        </div>
                        <div class="text-md font-nnsqneo text-gray-500">{{ userStore.userInfo?.email }}</div>
                        <div v-if="userStore.userInfo?.manager" class="text-md font-nnsqneo text-gray-500">관리자</div>
                        <div class="text-md font-nnsqneo text-gray-500">
                            가입일시 : {{ userStore.userInfo?.createdAt }}</div>
                        <div class="text-2xl text-blue-700 font-nnsqneo-extra-bold">{{ userStore.userInfo?.point }}
                            포인트</div>
                    </div>
                </div>

                <div class="flex flex-col p-4">
                    <MyEmoticonList />
                </div>

            </div>
        </div>


    </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user';
import { onMounted, ref } from 'vue';
import { RouterLink } from 'vue-router';
import MyEmoticonList from '@/components/mypage/MyEmoticonList.vue';

const userStore = useUserStore();

</script>

<style scoped>
.menu-link {
    @apply flex gap-2 items-center w-full border border-gray-200 p-4 rounded-lg hover:bg-gray-100 hover:shadow-lg transition-all duration-200
}

.menu-link:hover {
    transform: scale(1.02);
}
</style>