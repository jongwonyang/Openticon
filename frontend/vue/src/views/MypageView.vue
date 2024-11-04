<template>
    <div class="container mx-auto max-w-screen-lg">
        
        <h1 class="text-2xl font-nnsqneo-bold mt-4">마이페이지</h1>

        <div class="mt-4">
            <div class="grid grid-cols-2 gap-2">
                <h2 class="text-xl font-nnsqneo-bold">내 정보</h2>
                <div class="flex flex-row gap-2"></div>

            </div>
        </div>


    </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
</script>
