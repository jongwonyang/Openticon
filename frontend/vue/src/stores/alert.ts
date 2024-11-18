import { ref } from "vue";
import { defineStore } from "pinia";

export type Alert = {
  id: number;
  message: string;
  type: "normal" | "special" | "error" | "success" | "warning";
  title: string;
};

export const useAlertStore = defineStore("alert", () => {
  const lastId = ref(0);

  const alerts = ref<Alert[]>([]); // 유형 추가

  const addAlert = (
    message: string,
    type: "normal" | "special" | "error" | "success" | "warning" = "normal",
    title: string = "알림"
  ) => {
    // 기본값 'normal'
    const id = lastId.value++; // 고유 ID 생성
    alerts.value.push({
      id,
      message,
      type,
      title,
    });
    if (alerts.value.length > 11) {
      alerts.value.splice(0, 1);
    }
  };

  const removeAlert = (id: number) => {
    alerts.value = alerts.value.filter((alert) => alert.id !== id);
  };

  const clearAlerts = () => {
    alerts.value = [];
  };

  return { alerts, addAlert, removeAlert, clearAlerts };
});
