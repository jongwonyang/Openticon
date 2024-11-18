import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import type { ObjectionListResult } from "@/types/objectionlistResult";

export const useAdminStore = defineStore("admin", () => {
  const getObjectionList = async (
    page: number,
    size: number
  ): Promise<ObjectionListResult> => {
    const response = await apiClient.get(`/objection/manager-list`, {
      params: {
        page: page,
        size: size,
      },
    });
    return response.data;
  };

  return {
    getObjectionList,
  };
});
