<script setup lang="ts">
import DebugAlert from "@/components/debug/alert/DebugAlert.vue";
</script>

<template>
  <div class="container mx-auto">
    <h1 class="text-2xl font-bold text-center">디버그 메뉴</h1>
    <div class="flex justify-center">
      <div class="mt-4 m-3">
        <h2 class="text-xl font-bold text-center">알림</h2>
        <DebugAlert />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
