<template>
  <div class="max-w-screen-md mx-auto mt-4">
    <div class="grid gap-4 grid-cols-2 px-4">
      <div class="flex flex-col items-center justify-center">
        <RouterLink
          :to="{ name: 'uploadEmoticon' }"
          class="flex flex-col items-center justify-center w-full bg-gray-100 rounded-lg aspect-square"
          >
          <span class="material-icons text-[150px] text-gray-500">upload</span>
          <span class="text-xl">업로드</span>
        </RouterLink>
      </div>
      <div class="flex flex-col items-center justify-center">
        <RouterLink
          :to="{ name: 'createEmoticon' }"
          class="flex flex-col items-center justify-center w-full bg-gray-100 rounded-lg aspect-square"
          >
          <span class="material-icons text-[150px] text-gray-500">face</span>
          <span class="text-xl">AI로 만들기</span>
        </RouterLink>
      </div>
    </div>
  </div>
</template>
