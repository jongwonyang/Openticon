<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const slides = ref([
  {
    id: 1,
    image: 'https://mud-kage.kakaocdn.net/dn/calp54/btsKjHtWXxe/P4pXxUOx68r4xXKlU3FCZk/img.gif'
  },
  {
    id: 2,
    image: 'https://item.kakaocdn.net/dn/bJyZM2/btsKhwg4KL4/JQvm5km7FDNG3IDXEwtgzk/img.gif'
  },
]);

const currentSlide = ref(0);
const slideInterval = ref<ReturnType<typeof setInterval> | null>(null);
const direction = ref('next');
const isButtonDisabled = ref(false);

const debounceButton = (callback: () => void) => {
  if (isButtonDisabled.value) return;
  
  isButtonDisabled.value = true;
  callback();
  
  setTimeout(() => {
    isButtonDisabled.value = false;
  }, 1000);
};

const nextSlide = () => {
  debounceButton(() => {
    direction.value = 'next';
    currentSlide.value = (currentSlide.value + 1) % slides.value.length;
    resetSlideShow();
  });
};

const prevSlide = () => {
  debounceButton(() => {
    direction.value = 'prev';
    currentSlide.value = currentSlide.value === 0 
      ? slides.value.length - 1 
      : currentSlide.value - 1;
    resetSlideShow();
  });
};

const goToSlide = (index: number) => {
  debounceButton(() => {
    direction.value = index > currentSlide.value ? 'next' : 'prev';
    currentSlide.value = index;
    resetSlideShow();
  });
};

const startSlideShow = () => {
  slideInterval.value = setInterval(nextSlide, 10000); // 10초마다 슬라이드 변경
};

const stopSlideShow = () => {
  if (slideInterval.value) {
    clearInterval(slideInterval.value);
  }
};

const resetSlideShow = () => {
  stopSlideShow();
  startSlideShow();
};

onMounted(() => {
  startSlideShow();
});

onUnmounted(() => {
  stopSlideShow();
});
</script>

<template>
  <div class="relative w-full h-[100px] sm:h-[125px] md:h-[150px] lg:h-[250px] overflow-hidden">
    <!-- 슬라이드 -->
    <div class="relative w-full h-full overflow-hidden">
      <transition-group :name="direction === 'next' ? 'slide-next' : 'slide-prev'">
        <div v-for="(slide, index) in slides" 
             :key="slide.id"
             v-show="currentSlide === index"
             class="absolute top-0 left-0 w-full h-full">
          <img :src="slide.image" 
               class="w-full h-full object-cover" />
        </div>
      </transition-group>
    </div>

    <!-- 네비게이션 버튼 -->
    <button @click="prevSlide" 
            class="absolute left-4 top-1/2 transform -translate-y-1/2 bg-black bg-opacity-30 text-white p-2 rounded-full w-10 h-10">
      <span class="material-icons">chevron_left</span>
    </button>
    <button @click="nextSlide" 
            class="absolute right-4 top-1/2 transform -translate-y-1/2 bg-black bg-opacity-30 text-white p-2 rounded-full w-10 h-10">
      <span class="material-icons">chevron_right</span>
    </button>

    <!-- 인디케이터 -->
    <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
      <button v-for="(_, index) in slides" 
              :key="index"
              @click="goToSlide(index)"
              :class="[
                'w-3 h-3 rounded-full shadow-2xl border border-gray-600',
                currentSlide === index ? 'bg-white' : 'bg-gray-900 bg-opacity-50'
              ]">
      </button>
    </div>
  </div>
</template>

<style scoped>
.slide-next-enter-active,
.slide-next-leave-active,
.slide-prev-enter-active,
.slide-prev-leave-active {
  transition: all 0.5s ease;
  position: absolute;
  width: 100%;
}

.slide-next-enter-from {
  transform: translateX(100%);
}

.slide-next-leave-to {
  transform: translateX(-100%);
}

.slide-prev-enter-from {
  transform: translateX(-100%);
}

.slide-prev-leave-to {
  transform: translateX(100%);
}

.slide-next-enter-to,
.slide-next-leave-from,
.slide-prev-enter-to,
.slide-prev-leave-from {
  transform: translateX(0);
}
</style>
