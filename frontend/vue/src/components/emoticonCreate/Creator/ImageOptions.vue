<template>
  <div class="px-6 py-6">
    <div class="flex flex-col gap-2">
      <div class="flex flex-col gap-2 flex-grow">
        <div>
          <p>이미지 생성 시드</p>
          <div class="flex flex-row gap-2">
            <div class="flex-grow">
              <input
                type="text"
                placeholder="시드"
                class="border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500 w-full"
                v-model="createStore.seed"
              />
            </div>
            <div class="flex-shrink-0">
              <button
                @click="createStore.shuffleSeed"
                class="border px-2 py-1 rounded-md bg-slate-500 text-white flex items-center justify-center hover:bg-slate-600 transition-all duration-200 ease-in-out active:bg-slate-700"
              >
                <span class="material-symbols-outlined">shuffle</span>
              </button>
            </div>
          </div>
        </div>
        <div>
          <p>이미지 프롬프트</p>
          <div class="grid grid-cols-3 gap-4">
            <!-- 캐릭터 타입 -->
            <div>
              <p class="font-medium mb-1">캐릭터 타입</p>
              <select
                v-model="createStore.characterType"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="type in characterTypes"
                  :key="type.value"
                  :value="type.value"
                >
                  {{ type.label }}
                </option>
              </select>
            </div>

            <!-- 피부색 -->
            <div>
              <p class="font-medium mb-1">피부색</p>
              <select
                v-model="createStore.skinColor"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="color in skinColors"
                  :key="color.value"
                  :value="color.value"
                >
                  {{ color.label }}
                </option>
              </select>
            </div>

            <!-- 헤어스타일 -->
            <div>
              <p class="font-medium mb-1">헤어스타일</p>
              <select
                v-model="createStore.hairStyle"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="style in hairStyles"
                  :key="style.value"
                  :value="style.value"
                >
                  {{ style.label }}
                </option>
              </select>
            </div>

            <!-- 머리색 -->
            <div>
              <p class="font-medium mb-1">머리색</p>
              <select
                v-model="createStore.hairColor"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="color in hairColors"
                  :key="color.value"
                  :value="color.value"
                >
                  {{ color.label }}
                </option>
              </select>
            </div>

            <!-- 표정 -->
            <div>
              <p class="font-medium mb-1">표정</p>
              <select
                v-model="createStore.expression"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="expr in expressions"
                  :key="expr.value"
                  :value="expr.value"
                >
                  {{ expr.label }}
                </option>
              </select>
            </div>

            <!-- 구도 -->
            <div>
              <p class="font-medium mb-1">구도</p>
              <select
                v-model="createStore.composition"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="comp in compositions"
                  :key="comp.value"
                  :value="comp.value"
                >
                  {{ comp.label }}
                </option>
              </select>
            </div>

            <!-- 의상 -->
            <div>
              <p class="font-medium mb-1">의상</p>
              <select
                v-model="createStore.outfit"
                class="w-full border px-2 py-1 rounded-md focus:outline-none focus:border-slate-500"
              >
                <option
                  v-for="outfit in outfits"
                  :key="outfit.value"
                  :value="outfit.value"
                >
                  {{ outfit.label }}
                </option>
              </select>
            </div>
          </div>
        </div>
      </div>
      <div>
        <button
          class="border px-4 py-1 rounded-md bg-slate-500 text-white w-full h-full hover:bg-slate-600 transition-all duration-200 ease-in-out active:bg-slate-700"
          @click="handleCreateImage"
        >
          생성
        </button>
      </div>
    </div>
    <div class="grid grid-cols-2 gap-2 mt-2">
      <button
        class="border px-4 py-1 rounded-md bg-red-500 text-white w-full h-full hover:bg-red-600 transition-all duration-200 ease-in-out active:bg-red-700"
        @click="handleCancelImage"
      >
        취소
      </button>
      <button
        class="border px-4 py-1 rounded-md bg-blue-500 text-white w-full h-full hover:bg-blue-600 transition-all duration-200 ease-in-out active:bg-blue-700"
        @click="handleApplyImage"
      >
        적용
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCreateStore } from "@/stores/create";
import { onMounted } from "vue";

const emit = defineEmits<{
  (e: "update:image", image: File): void;
  (e: "update:cancel"): void;
  (e: "update:apply"): void;
}>();

const createStore = useCreateStore();

const handleCreateImage = async () => {
  const prompt = `Chibi, solo, big head, ${createStore.characterType}, ${createStore.skinColor}, ${createStore.hairStyle}, ${createStore.hairColor}, ${createStore.expression}, ${createStore.composition}, ${createStore.outfit}`;
  const image = await createStore.createImage(
    createStore.seed.toString(),
    prompt
  );
  emit("update:image", image);
};

const handleCancelImage = () => {
  emit("update:cancel");
};

const handleApplyImage = () => {
  emit("update:apply");
};

onMounted(() => {
  if(createStore.characterType === "") {
    createStore.characterType = characterTypes[0].value;
  }
  if(createStore.skinColor === "") {
    createStore.skinColor = skinColors[0].value;
  }
  if(createStore.hairStyle === "") {
    createStore.hairStyle = hairStyles[0].value;
  }
  if(createStore.hairColor === "") {
    createStore.hairColor = hairColors[0].value;
  }
  if(createStore.expression === "") {
    createStore.expression = expressions[0].value;
  }
  if(createStore.composition === "") {
    createStore.composition = compositions[0].value;
  }
  if(createStore.outfit === "") {
    createStore.outfit = outfits[0].value;
  }

});

const characterTypes = [
  { label: "그냥여자", value: "1 girl" },
  { label: "고양이 여자", value: "1 cat girl" },
  { label: "강아지 여자", value: "1 dog girl" },
  { label: "그냥 남자", value: "1 man" },
  { label: "고양이 남자", value: "1 cat man" },
  { label: "강아지 남자", value: "1 dog man" },
];

const skinColors = [
  { label: "일반", value: "" },
  { label: "회색", value: "Grey_skin" },
  { label: "흰색", value: "White_skin" },
  { label: "빨간색", value: "Red_skin" },
  { label: "분홍색", value: "Pink_skin" },
  { label: "주황색", value: "Orange_skin" },
  { label: "노란색", value: "Yellow_skin" },
  { label: "초록색", value: "Green_skin" },
  { label: "파란색", value: "Blue_skin" },
  { label: "보라색", value: "Purple_skin" },
  { label: "어두운색", value: "Dark_skin" },
  { label: "검은색", value: "Black_skin" },
];

const hairStyles = [
  { label: "중간", value: "medium_hair" },
  { label: "짧다", value: "short_hair" },
  { label: "매우 짧다", value: "very_short_hair" },
  { label: "엄청 짧다", value: "absurdly_short_hair" },
  { label: "길다", value: "long_hair" },
  { label: "매우 길다", value: "very_long_hair" },
  { label: "엄청 길다", value: "absurdly_long_hair" },
  { label: "비대칭", value: "asymmetrical_bangs" },
];

const hairColors = [
  { label: "회색", value: "gray_hair" },
  { label: "흰색", value: "white_hair" },
  { label: "갈색", value: "brown_hair" },
  { label: "빨강색", value: "red_hair" },
  { label: "분홍색", value: "pink_hair" },
  { label: "주황색", value: "orange_hair" },
  { label: "노란색", value: "yellow_hair" },
  { label: "황금색", value: "blonde_hair" },
  { label: "연두색", value: "light_green_hair" },
  { label: "초록색", value: "green_hair" },
  { label: "하늘색", value: "sky_blue_hair" },
  { label: "파란색", value: "blue_hair" },
  { label: "보라색", value: "purple_hair" },
  { label: "검은색", value: "black_hair" },
  { label: "무지개색", value: "rainbow_hair" },
  { label: "투 톤", value: "two-tone_hair" },
  { label: "줄무늬", value: "streaked_hair" },
  { label: "그라데이션", value: "gradient_hair" },
  { label: "색깔 분열", value: "split-color_hair" },
  { label: "여러 색깔", value: "multicolored_hair" },
  { label: "내부 착색", value: "colored_inner_hair" },
];

const expressions = [
  { label: "기쁜 표정", value: "Happy_face" },
  { label: "놀란 표정", value: "surprised_face" },
  { label: "웃는 표정", value: "smiling_face" },
  { label: "감동한 표정", value: "touched_face" },
  { label: "느긋한 표정", value: "easygoing_face" },
  { label: "슬픈 표정", value: "Sad_face" },
  { label: "화난 표정", value: "Angry_face" },
  { label: "무서운 표정", value: "Scary_face" },
  { label: "부끄러운 표정", value: "Shameful_face" },
  { label: "지루한 표정", value: "Boring_face" },
  { label: "우울한 표정", value: "Gloomy_face" },
  { label: "피곤한 표정", value: "Tired_face" },
  { label: "혼란한 표정", value: "confused_face" },
  { label: "행복한 표정", value: "joyful_face" },
  { label: "절망한 표정", value: "despair_face" },
];

const compositions = [
  { label: "얼굴만", value: "Face only" },
  { label: "상체", value: "upper body" },
  { label: "전신", value: "Whole_body" },
];

const outfits = [
  { label: "흰색 티셔츠", value: "clean white t-shirt" },
  { label: "검은 티셔츠", value: "clean black t-shirt" },
  { label: "흰색 드래스", value: "white dress" },
  { label: "검정 드래스", value: "black dress" },
];
</script>
