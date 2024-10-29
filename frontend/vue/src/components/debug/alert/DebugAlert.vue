<!-- Start Generation Here -->
<template>
  <div class="flex justify-center flex-wrap border border-gray-300 rounded-md p-4">
    <button
      class="border border-gray-300 rounded-md px-4 py-2 m-1 bg-gradient-to-r from-pink-400 via-yellow-400 to-green-400 animate-gradient"
      @click="alertStore.addAlert('특수 알림 메시지', 'special', '특수')"
    >
      특수 알림 추가
    </button>
    <button
      class="border border-gray-300 rounded-md px-4 py-2 m-1"
      @click="alertStore.addAlert('일반 알림 메시지', 'normal', '일반')"
    >
      일반 알림 추가
    </button>
    <button
      class="border border-gray-300 rounded-md px-4 py-2 m-1 bg-green-400"
      @click="alertStore.addAlert('성공 알림 메시지', 'success', '성공')"
    >
      성공 알림 추가
    </button>
    <button
      class="border border-gray-300 rounded-md px-4 py-2 m-1 bg-red-400"
      @click="
        alertStore.addAlert('서버에.', 'error', '오류')
      "
    >
      실패 알림 추가
    </button>
    <button
      class="border border-gray-300 rounded-md px-4 py-2 m-1 bg-yellow-400"
      @click="
        alertStore.addAlert('너무 많은 요청을 보냈습니다.', 'warning', '경고')
      "
    >
      경고 알림 추가
    </button>
    <div>
      <button
        class="border border-gray-300 rounded-md px-4 py-2 m-1 bg-blue-400"
      @click="
        randOn = !randOn
      "
      >
        랜덤 알림 추가
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAlertStore } from "@/stores/alert";
import { ref } from "vue";

const alertStore = useAlertStore();

setInterval(() => {
  if (randOn.value) {
    const random = Math.floor(Math.random() * 5);
    const type = ['normal', 'special', 'success', 'error', 'warning'][random];
    const title = ['일반', '특수', '성공', '실패', '경고'][random];
    const message = ['일반 메시지', '특수 메시지', '성공 메시지', '실패 메시지', '경고 메시지'][random];
    alertStore.addAlert(message, type as any, title);
  }
}, 500);


const randOn = ref(false);

</script>

<style scoped>
</style>
<!-- End Generation Here -->
