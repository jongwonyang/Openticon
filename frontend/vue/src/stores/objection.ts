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
import type { BlacklistResult } from "@/types/blacklistResult";
import type { ObjectionListResult } from "@/types/objectionlistResult";

export const useObjectionStore = defineStore("objection", () => {
  const getBlockedEmoticonPackList = async (
    page: number,
    size: number
  ): Promise<BlacklistResult> => {
    const response = await apiClient.get(`/objection/list`, {
      params: {
        page: page,
        size: size,
      },
    });
    return response.data;
  };

  const makeObjection = async (objectionId: number, content: string) => {
    const response = await apiClient.post(`/objection`, {
      objectionId,
      content,
    });
    return response.data;
  };

  const getObjectionList = async (
    page: number,
    size: number,
  ): Promise<ObjectionListResult> => {
    const response = await apiClient.get(`/objection/manager-list`, {
      params: { page, size },
    });
    return response.data;
  };

  const handleObjection = async (
    objectionId: number,
    reportStateType: string,
    content: string
  ) => {
    const response = await apiClient.post(`/objection/manager-answer`, {
      objectionId,
      reportStateType,
      content,
    });
    return response.data;
  };

  return {
    getBlockedEmoticonPackList,
    makeObjection,
    getObjectionList,
    handleObjection,
  };
});
