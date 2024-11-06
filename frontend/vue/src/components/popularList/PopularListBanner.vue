<template>
    <div class="w-full h-28 sm:h-40 bg-[#5fafff] overflow-hidden transition-all duration-300" style="user-select: none;">
        <div class="relative mx-auto max-w-screen-lg h-full">
            <p class="absolute top-[42%] left-[31%] text-3xl font-nnsqneo-heavy text-blue-600 z-0 bg-text-blur whitespace-nowrap">유행하는</p>
            <p class="absolute top-[77%] left-[43%] text-5xl font-nnsqneo-heavy text-blue-600 z-0 bg-text-blur whitespace-nowrap">너도나도</p>
            <p class="absolute top-[19%] left-[53%] text-3xl font-nnsqneo-heavy text-sky-600 z-0 bg-text-blur whitespace-nowrap">많이쓰는</p>
            <p class="absolute top-[28%] left-[67%] text-3xl font-nnsqneo-heavy text-blue-600 z-0 bg-text-blur whitespace-nowrap">익숙한</p>
            <p class="absolute top-[67%] left-[75%] text-4xl font-nnsqneo-heavy text-indigo-500 z-0 bg-text-blur whitespace-nowrap">항상쓰는</p>
            <p class="absolute -top-[12%] left-[70%] text-indigo-800 z-0 whitespace-nowrap rotate-[20deg]">
                <span class="material-icons text-[150px] sm:text-[200px] transition-all duration-300">local_fire_department</span>
            </p>
            <p class="absolute top-12 left-6 text-7xl sm:text-9xl transition-all duration-300 font-nnsqneo-heavy text-blue-900 z-10 shadow-text">인기</p>
        </div>
    </div>
</template>

<style scoped>
.bg-text-blur {
    filter: blur(1px);
    @media (max-width: 768px) {
        filter: blur(calc((768px - 100vw) * 0.01 + 1px));
    }
}
</style>