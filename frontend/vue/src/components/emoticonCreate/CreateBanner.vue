<template>
  <div class="w-full h-28 overflow-hidden" style="user-select: none; background: radial-gradient(circle at center, #f5f5f5 0%, #d8d8d8 120%);">
    <div class="relative mx-auto max-w-screen-lg h-full">
      <p class="absolute -top-[12%] left-[70%] text-gray-600 z-10 whitespace-nowrap rotate-[20deg]">
        <span class="material-icons text-[150px]">palette</span>
      </p>

      <p class="absolute top-[7%] left-[40%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[11px] opacity-70">
        <span class="material-icons text-[90px]">upload</span>
      </p>
      <p class="absolute -top-[1%] left-[50%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[8px] opacity-70">
        <span class="material-icons text-[90px]">upload</span>
      </p>
      <p class="absolute top-[38%] left-[55%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[6.2px] opacity-70">
        <span class="material-icons text-[100px]">upload</span>
      </p>
      <p class="absolute -top-[9%] left-[60%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[7px] opacity-70">
        <span class="material-icons text-[90px]">upload</span>
      </p>
      <p class="absolute top-[32%] left-[64%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[6.3px] opacity-60">
        <span class="material-icons text-[110px]">upload</span>
      </p>
      <p class="absolute top-[16%] left-[78%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[8.3px] opacity-70">
        <span class="material-icons text-[100px]">upload</span>
      </p>
      <p class="absolute top-[10%] left-[85%] text-gray-600 z-0 whitespace-nowrap rotate-[20deg] blur-[10px] opacity-70">
        <span class="material-icons text-[80px]">upload</span>
      </p>
      <p class="absolute -bottom-2 left-[5%] text-7xl font-nnsqneo-heavy text-gray-600 z-10 whitespace-nowrap">생성</p>
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