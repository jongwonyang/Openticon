import { defineStore } from "pinia";
import apiClient from "@/util/apiClient";
import { ref } from "vue";

export const useCreateStore = defineStore("create", () => {
  const seed = ref(Math.floor(Math.random() * 1000000));
  const characterType = ref('');
  const skinColor = ref('');
  const hairStyle = ref('');
  const hairColor = ref('');
  const expression = ref('');
  const composition = ref('');
  const outfit = ref('');
  const textColor = ref('#FFFFFF');
  const textOutlineColor = ref('#000000');

  const createImage = async (seed: string, prompt: string): Promise<File> => {
    const response = await apiClient.post(`/ai/create-image`, {
      seed: seed,
      prompt: prompt,
    }, {
      responseType: 'blob'
    });
    const blob = response.data;
    const file = new File([blob], "image.png", { type: "image/png" });
    return file;
  };

  const shuffleSeed = () => {
    seed.value = Math.floor(Math.random() * 2147483647);
  };

  return {
    seed,
    characterType,
    skinColor,
    hairStyle,
    hairColor,
    expression,
    composition,
    outfit,
    textColor,
    textOutlineColor,
    createImage,
    shuffleSeed,
  };
});
