import apiClient from '@/util/apiClient';
import { defineStore } from 'pinia'
import { ref } from 'vue';

 // Start of Selection
interface UserInfo {
    id: number;
    email: string;
    nickname: string;
    profile_image: string;
    createdAt: number;
    updatedAt: number;
    isResigned: boolean;
    manager: boolean;
    mobile_fcm: string;
    web_fcm: string;
    point: number;
}

export const useUserStore = defineStore('user', () => {
    const accessToken = ref<string | null>(localStorage.getItem('accessToken') || null);
    const isLogined = ref<boolean>(!!accessToken.value);
    const userInfo = ref<UserInfo | null>(null);

    const login = (token: string) => {
        localStorage.setItem('accessToken', token);
        apiClient.get('/member').then((res) => {
            console.log(res.data);
            userInfo.value = res.data;
        });
        accessToken.value = token;
        isLogined.value = true;
    }

    if (accessToken.value) {
        login(accessToken.value);
    }

    const logout = () => {
        localStorage.removeItem('accessToken');
        accessToken.value = null;
        isLogined.value = false;
        userInfo.value = null;
    }

    const getAccessToken = () => {
        return accessToken.value;
    }
    return { isLogined, userInfo, getAccessToken, login, logout }
})
