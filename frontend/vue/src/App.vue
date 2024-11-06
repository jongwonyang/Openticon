<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import AlertList from "./components/common/alert/AlertList.vue";
import NavBar from "./components/common/navigation/NavBar.vue";
import Footer from "./components/common/footer/Footer.vue";
</script>

<template>
  <AlertList />
  <NavBar />
  <RouterView />
  <Footer class="footer" />
</template>

<style scoped>
.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
}
</style>
