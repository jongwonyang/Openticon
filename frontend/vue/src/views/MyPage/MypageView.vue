<template>
  <MypageHeader />
  <div class="container mx-auto max-w-screen-lg">
    <div class="mt-4">
      <div class="flex flex-col gap-2">
        <div class="flex flex-col p-4">
          <MyEmoticonList :myEmoticonList="myEmoticonList" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "@/stores/user";
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";
import MyEmoticonList from "@/components/mypage/MyEmoticonList.vue";
import { useEmoticonPackStore } from "@/stores/emoticonPack";
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
import MypageHeader from "@/components/mypage/MypageHeader.vue";

const userStore = useUserStore();
const emoticonStore = useEmoticonPackStore();

const myEmoticonList = ref<EmoticonPackInList[]>([]);

onMounted(async () => {
  const response = await emoticonStore.getMyEmoticonPackList(0, 10);
  myEmoticonList.value = response.content;
});
</script>

<style scoped>
.menu-link {
  @apply flex gap-2 items-center w-full border border-gray-200 p-4 rounded-lg hover:bg-gray-100 hover:shadow-lg transition-all duration-200;
}

.menu-link:hover {
  transform: scale(1.02);
}
</style>
