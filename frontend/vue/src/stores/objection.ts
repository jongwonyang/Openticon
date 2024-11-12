import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import type { EmoticonPackSearchList } from "@/types/emoticonPackSearchList";
import type { EmoticonPack } from "@/types/emoticonPack";
import type {
  EmoticonPackUploadInfo,
  EmoticonPackUploadFiles,
} from "@/types/emoticonPackUpload";
import { ref } from "vue";
import type { UploadResult } from "@/types/uploadResult";

export const useObjectionStore = defineStore("objection", () => {
  const getBlockedEmoticonPackList = async (
    page: number,
    size: number
  ): Promise<EmoticonPackSearchList> => {
    const response = await apiClient.get(`/objection/list`, {
      params: {
        page: page,
        size: size,
      },
    });
    return response.data;
  };

  return {
    getBlockedEmoticonPackList,
  };
});
