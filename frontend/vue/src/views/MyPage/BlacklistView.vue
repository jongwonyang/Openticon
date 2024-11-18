<template>
  <BlacklistpageBanner />
  <div class="px-4 mt-4 max-w-screen-lg mx-auto">
    <div class="text-gray-600 px-4 py-4 bg-gray-200 rounded-lg">
      업로드한 이모티콘 팩 중 AI 심사 통과실패 및 다수의 신고를 받은 이모티콘 팩
      목록입니다.<br />
      &nbsp;&nbsp;&nbsp;&nbsp;이모티콘 팩 중 표시중단 목록에 포함된 이모티콘
      팩은 다른 사용자에게 표시되지 않습니다.
    </div>
  </div>
  <StateSelector
    @update:displayedState="updateDisplayedState"
    :displayedState="displayedState"
  />
  <Blacklist :displayedState="displayedState" />
</template>

<script setup lang="ts">
import Blacklist from "@/components/blacklist/Blacklist.vue";
import BlacklistpageBanner from "@/components/blacklist/BlacklistpageBanner.vue";
import StateSelector from "@/components/blacklist/StateSelector.vue";
import { ref } from "vue";

const displayedState = ref<"PENDING" | "RECEIVED" | "APPROVED" | "REJECTED">(
  "PENDING"
);

const updateDisplayedState = (
  state: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED"
) => {
  displayedState.value = state;
};
</script>
