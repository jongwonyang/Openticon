import { useAlertStore } from '@/stores/alert';

export const makeErrorAlert = (message: string) => {
  const alertStore = useAlertStore();
  alertStore.addAlert(message, 'error', '오류');
};

export const makeSuccessAlert = (message: string) => {
  const alertStore = useAlertStore();
  alertStore.addAlert(message, 'success', '성공');
};

export const makeWarningAlert = (message: string) => {
  const alertStore = useAlertStore();
  alertStore.addAlert(message, 'warning', '경고');
};

export const makeSpecialAlert = (message: string) => {
  const alertStore = useAlertStore();
  alertStore.addAlert(message, 'special', '특별');
};

export const makeNormalAlert = (message: string) => {
  const alertStore = useAlertStore();
  alertStore.addAlert(message, 'normal', '일반');
};

