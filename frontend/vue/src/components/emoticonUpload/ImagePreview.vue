<script setup lang="ts">
const props = defineProps<{
  image: File;
}>();

const emit = defineEmits<{
  (e: "delete"): void;
}>();

const url = URL.createObjectURL(props.image);

const deleteImage = () => {
  console.log("deleteImage");
  emit("delete");
};
</script>

<template>
  <div class="flex flex-col items-center justify-center relative">
    <button
      class="absolute -top-2 -right-2 w-8 h-8 bg-red-500 text-white rounded-full p-1"
      @click="deleteImage"
    >
      <span class="material-icons">delete</span>
    </button>
    <div
      :class="[
        'w-full h-full bg-gray-100 rounded-md border-2 flex flex-col items-center justify-center',
        'border-gray-400 border-solid',
      ]"
    >
      <img :src="url" class="w-full h-full object-cover rounded-md" />
    </div>
  </div>
</template>
