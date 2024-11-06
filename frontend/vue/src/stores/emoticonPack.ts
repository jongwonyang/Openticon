import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import type { EmoticonPackSearchList } from "@/types/emoticonPackSearchList";
import type { EmoticonPack } from "@/types/emoticonPack";

export const useEmoticonPackStore = defineStore("emoticonPack", () => {
  const getEmoticonPackData = async (id: number): Promise<EmoticonPack> => {
    const response = await apiClient.get<EmoticonPack>(`/emoticonpacks/info`, {
      params: {
        emoticonPackId: id,
      },
    });
    return response.data;
  };

  const getNewEmoticonPackList = async (page: number, size: number): Promise<EmoticonPackSearchList> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        page: page,
        size: size,
        sort: "new",
      },
    });
    return response.data;
  };

  const getPopularEmoticonPackList = async (page: number, size: number): Promise<EmoticonPackSearchList> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        page: page,
        size: size,
        sort: "most",
      },
    });
    return response.data;
  };

  const searchEmoticonPack = async (query: string, type: string, page: number, size: number): Promise<EmoticonPackSearchList> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        query: query,
        type: type,
        page: page,
        size: size,
      },
    });
    return response.data;
  };

  return { getEmoticonPackData, getNewEmoticonPackList, getPopularEmoticonPackList, searchEmoticonPack };
});
