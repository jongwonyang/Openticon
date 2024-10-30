import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useEmoticonDetailStore = defineStore('emoticonDetail', () => {
    const emoticon = ref({
        image: '',
        name: '',
    })

    const getEmoticon = () => {

        return emoticon.value;
    }

    return { getEmoticon }
})
