<template>
  <div class="container mx-auto max-w-screen-lg">
    <SearchBar :query="query" :type="type" />
    <SearchList :query="query" :type="type" />
  </div>
</template>

<script setup lang="ts">
import SearchBar from "@/components/main/search/SearchBar.vue";
import { onMounted, ref } from "vue";
import { onBeforeRouteUpdate, useRoute } from "vue-router";
import SearchList from "@/components/search/searchList.vue";

const route = useRoute();

const query = ref((route.query.query as string) || "");
const type = ref((route.query.type as string) || "");

onBeforeRouteUpdate((to, from) => {
  query.value = to.query.query as string;
  type.value = to.query.type as string;
});
</script>
