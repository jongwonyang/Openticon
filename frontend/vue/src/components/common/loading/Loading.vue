<template>
    <div class="flex flex-col items-center justify-center p-5" v-if="isLoading">
        <div class="spinner"></div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            isLoading: true, // 로딩 상태를 관리하는 변수
        };
    },
    // ... 기존 코드 ...
}
</script>

<style scoped>

.spinner {
    border: 4px solid rgba(255, 255, 255, 0.3);
    border-top: 4px solid #000;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>
