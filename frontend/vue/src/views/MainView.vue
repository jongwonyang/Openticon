<script setup lang="ts">
import MainBanner from "@/components/main/banner/Banner.vue";
import NewListShort from "@/components/main/newListShort/NewListShort.vue";
</script>

<template>
  <MainBanner />
  <NewListShort />
  <div class="container p-4 mx-auto max-w-screen-lg">
    <div
      class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4"
    >
      <div class="border p-4">하이1</div>
      <div class="border p-4">하이2</div>
      <div class="border p-4">하이3</div>
      <div class="border p-4">하이4</div>
      <div class="border p-4">하이5</div>
    </div>
  </div>
</template>

<style scoped></style>
