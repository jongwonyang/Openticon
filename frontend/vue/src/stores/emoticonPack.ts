import { ref, computed } from "vue";
import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import axios from "axios";
import type { EmoticonPack } from "@/types/emoticonPack";

export const useEmoticonPackStore = defineStore("emoticonPack", () => {
  const getEmoticonPackData = async (id: number): Promise<EmoticonPack> => {
    // const response = await apiClient.get<EmoticonPack>(`/emoticonPacks/info`, {
    //     params: {
    //         id: id
    //     }
    // });

    const response = axios
      .get(`https://public.sgr.cspark.kr/OPENTICON/emoticonPack2.json`)
      .then((res) => {
        return res.data;
      });
    return response;
  };

  const getNewEmoticonPackList = async (): Promise<EmoticonPack[]> => {
    const response = await apiClient.get(`/emoticonpacks/search`, {
      params: {
        page: 0,
        size: 10,
        sort: "new",
      },
    });
    return response.data;
  };

  return { getEmoticonPackData, getNewEmoticonPackList };
});
