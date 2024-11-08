import apiClient from "@/util/apiClient";
import { defineStore } from "pinia";
import { ref } from "vue";
import type { UserInfo } from "@/types/userInfo";

export const useUserStore = defineStore("user", () => {
  const accessToken = ref<string | null>(
    localStorage.getItem("accessToken") || null
  );
  const isLogined = ref<boolean>(!!accessToken.value);
  const userInfo = ref<UserInfo | null>(null);

  const login = (token: string) => {
    localStorage.setItem("accessToken", token);
    apiClient.get("/member").then((res) => {
      userInfo.value = res.data;
    });
    accessToken.value = token;
    isLogined.value = true;
  };

  if (accessToken.value) {
    login(accessToken.value);
  }

  const logout = () => {
    localStorage.removeItem("accessToken");
    accessToken.value = null;
    isLogined.value = false;
    userInfo.value = null;
  };

  const getAccessToken = () => {
    return accessToken.value;
  };

  const updateProfileImage = (file: File) => {
    alert("프로필 이미지 변경은 준비중입니다.");
    // apiClient.put("/member/profile-image", { file }).then((res) => {
    //   userInfo.value = res.data;
    // });
  };

  return { isLogined, userInfo, getAccessToken, login, logout, updateProfileImage };
});
