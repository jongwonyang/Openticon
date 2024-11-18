<script setup lang="ts">
import MainBanner from "@/components/main/banner/Banner.vue";
import NewListShort from "@/components/main/newListShort/NewListShort.vue";
import PopularListShort from "@/components/main/popularListShort/PopularListShort.vue";
import SearchBar from "@/components/main/search/SearchBar.vue";
</script>

<template>
  <MainBanner />
  <SearchBar :query="null" :type="null" />
  <NewListShort />
  <PopularListShort />
</template>

<style scoped></style>
