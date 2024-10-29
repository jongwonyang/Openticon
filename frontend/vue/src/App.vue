<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import AlertList from "./components/common/alert/AlertList.vue";
import NavBar from "./components/common/navigation/NavBar.vue";
</script>

<template>
  <AlertList />
  <NavBar />
  <RouterView />
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");



* {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
