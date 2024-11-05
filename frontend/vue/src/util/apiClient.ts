// apiClient.js
import axios from "axios";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import { makeErrorAlert } from "./alert";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // API 기본 URL
});

// 요청 인터셉터
apiClient.interceptors.request.use(
  async (config) => {
    const userStore = useUserStore();

    // 액세스 토큰이 있으면 Authorization 헤더에 추가
    if (userStore.isLogined) {
      const token = userStore.getAccessToken();
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    // 요청 오류 처리
    return Promise.reject(error);
  }
);

// 응답 인터셉터
apiClient.interceptors.response.use(
  (response) => response, // 응답이 성공적일 때 처리
  async (error) => {
    makeErrorAlert(
      `HTTP ${error.response?.status} 에러 : 콘솔을 확인해주세요.`
    );
    console.error(error);
    return Promise.reject(error);
  }
);

export default apiClient;
