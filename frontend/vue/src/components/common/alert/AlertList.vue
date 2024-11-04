<template>
  <div class="alert-list" :class="{ 'hasMoreAlert': alerts.length > displayedAlertLimit }">
    <TransitionGroup name="fade-shrink">
      <div
        class="mx-1 mt-1 p-4 flex justify-between border border-gray-300 rounded-md bg-gray-200 transition-colors duration-300 cursor-pointer shadow-md"
        :class="{ 'bg-gray-400': overflowExpaned }"
        v-if="alerts.length > displayedAlertLimit"
        @click="overflowExpaned = !overflowExpaned"
      >
        <i
          class="mdi mdi-plus-circle-outline mr-2 text-lg text-gray-900 font-bold"
        ></i>
        <span class="truncate text-lg text-gray-900 font-bold"
          >{{ alerts.length - displayedAlertLimit }}개의 추가 알림</span
        >
        <span class="flex-grow"></span>
        <button class="text-nowrap" @click.prevent="clearAlerts">
          모두 지우기
        </button>
      </div>
      <template v-for="alert in alerts.slice(sliceLength)" :key="alert.id">
        <Alert :alert="alert" @close="alertStore.removeAlert(alert.id)" />
      </template>
    </TransitionGroup>
  </div>
</template>

<script setup lang="ts">
import { useAlertStore } from "@/stores/alert"; // Notification 타입 가져오기
import { storeToRefs } from "pinia";
import Alert from "@/components/common/alert/Alert.vue";
import { computed, ref, watch } from "vue";

const alertStore = useAlertStore(); // Pinia 스토어 사용
const { alerts } = storeToRefs(alertStore);

const overflowExpaned = ref(false);

watch(alerts, (newVal) => {
  if (newVal.length <= 1) {
    overflowExpaned.value = false;
  }
});

const displayedAlertLimit = 1
const sliceLength = computed(() => {
  return overflowExpaned.value ? -alerts.value.length : -displayedAlertLimit;
});

const clearAlerts = (e: Event) => {
  e.stopPropagation();
  alertStore.clearAlerts();
};
</script>

<style scoped>
.fade-shrink-enter-active,
.fade-shrink-leave-active {
  transition: all ease 0.30s;
  max-height: 62px;
  box-sizing: border-box;
}

.fade-shrink-enter-from,
.fade-shrink-leave-to {
  filter: blur(10px);
  opacity: 0;
  max-height: 0px;
  padding-top: 0px;
  padding-bottom: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
  margin-left: 10px;
  margin-right: 10px;
  border-width: 0px;
}

.alert-list {
  transition: min-height 0.30s ease;
  min-height: 0px;
}

.hasMoreAlert {
  min-height: 130px;
}
</style>
