<!-- Start Generation Here -->
<template>
  <nav class="p-4 container mx-auto">
    <ul class="flex space-x-4">
      <li>
        <RouterLink
          to="/"
          class="text-black hover:text-gray-300 font-bold text-2xl"
          >홈</RouterLink
        >
      </li>
      <li>
        <RouterLink
          to="/about"
          class="text-black hover:text-gray-300 font-bold text-2xl"
          >소개</RouterLink
        >
      </li>
      <li>
        <RouterLink
          to="/debug"
          class="text-black hover:text-gray-300 font-bold text-2xl"
          >디버그</RouterLink
        >
      </li>   
    </ul>
  </nav>
</template>

<script setup lang="ts">
import { RouterLink } from "vue-router";
</script>

<style scoped></style>
<!-- End Generation Here -->
