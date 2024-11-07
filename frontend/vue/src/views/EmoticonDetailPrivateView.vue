<template>
  <div class="container mx-auto max-w-screen-lg">
    <Loading v-if="isLoading" />
    <div v-if="!isLoading">
      <EmoticonDetailHeader :emoticon="emoticon" />
      <hr class="my-4 mx-4" />
      <EmoticonDetailContent :emoticon="emoticon" />
      <hr class="my-4 mx-4" />
      <EmoticonDetailFooter :emoticon="emoticon" />
    </div>
  </div>
</template>

<script setup lang="ts">
import EmoticonDetailHeader from "@/components/emoticonDetail/EmoticonDetailHeader.vue";
import EmoticonDetailContent from "@/components/emoticonDetail/EmoticonDetailContent.vue";
import EmoticonDetailFooter from "@/components/emoticonDetail/EmoticonDetailFooter.vue";
import Loading from "@/components/common/loading/Loading.vue";

import { ref, onMounted } from "vue";
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type { EmoticonPack } from "@/types/emoticonPack";
import { useRoute } from "vue-router";

const emoticonPackStore = useEmoticonPackStore();
const emoticon = ref<EmoticonPack | null>(null);
const isLoading = ref(true);

const route = useRoute();

onMounted(async () => {
  emoticon.value = await emoticonPackStore.getEmoticonPackDataPrivate(
    route.params.id as string
  );
  isLoading.value = false;
});
</script>
