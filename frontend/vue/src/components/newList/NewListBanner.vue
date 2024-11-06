<template>
  <div class="w-full h-28 sm:h-40 bg-[#ffda76] overflow-hidden transition-all duration-300" style="user-select: none;">
    <div class="relative mx-auto max-w-screen-lg h-full">
      <p class="absolute top-[42%] left-[31%] text-3xl font-nnsqneo-heavy text-red-400 z-0 bg-text-blur whitespace-nowrap">새로운</p>
      <p class="absolute top-[82%] left-[43%] text-5xl font-nnsqneo-heavy text-red-400 z-0 bg-text-blur whitespace-nowrap">따끈따끈한</p>
      <p class="absolute top-[19%] left-[53%] text-3xl font-nnsqneo-heavy text-rose-400 z-0 bg-text-blur whitespace-nowrap">이왜있?</p>
      <p class="absolute top-[28%] left-[67%] text-3xl font-nnsqneo-heavy text-rose-400 z-0 bg-text-blur whitespace-nowrap">처음보는</p>
      <p class="absolute top-[67%] left-[75%] text-4xl font-nnsqneo-heavy text-red-400 z-0 bg-text-blur whitespace-nowrap">유행하는</p>
      <p class="absolute -top-[12%] left-[70%] text-red-600 z-0 whitespace-nowrap rotate-[20deg]">
        <span class="material-icons text-[150px] sm:text-[200px] transition-all duration-300">new_releases</span>
      </p>
      <p class="absolute top-12 left-6 text-7xl sm:text-9xl transition-all duration-300 font-nnsqneo-heavy text-red-600 z-10">신규</p>
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