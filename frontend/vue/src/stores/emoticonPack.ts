import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import type { EmoticonPackInList } from "@/types/emoticonPackInList";
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

  const getNewEmoticonPackList = async (page: number, size: number): Promise<EmoticonPackInList[]> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        page: page,
        size: size,
        sort: "new",
      },
    });
    return response.data.content;
  };

  const getPopularEmoticonPackList = async (page: number, size: number): Promise<EmoticonPackInList[]> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        page: page,
        size: size,
        sort: "most",
      },
    });
    return response.data.content;
  };

  return { getEmoticonPackData, getNewEmoticonPackList, getPopularEmoticonPackList };
});
