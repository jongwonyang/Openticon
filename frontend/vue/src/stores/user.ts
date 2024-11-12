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

  const retrieveUserInfo = () => {
    if(isLogined.value) {
    apiClient.get("/member").then((res) => {
        userInfo.value = res.data;
      });
    }else{
      console.error("로그인되어있지 않습니다.");
    }
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

  const updateProfileImage = (file: File): Promise<void> => {
    const formData = new FormData();
    formData.append("profile_img", file);
    formData.append("nickname", userInfo.value?.nickname ?? "");
    formData.append("bio", userInfo.value?.bio ?? "");
    return apiClient.put("/member", formData);
  };

  const updateNickname = (nickname: string): Promise<void> => {
    const formData = new FormData();
    formData.append("nickname", nickname);
    formData.append("bio", userInfo.value?.bio ?? "");
    return apiClient.put("/member", formData);
  };

  const updateBio = (bio: string): Promise<void> => {
    const formData = new FormData();
    formData.append("nickname", userInfo.value?.nickname ?? "");
    formData.append("bio", bio);
    return apiClient.put("/member", formData);
  };

  return { isLogined, userInfo, getAccessToken, login, logout, updateProfileImage, updateNickname, updateBio, retrieveUserInfo };
});
