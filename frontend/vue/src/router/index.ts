import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import AboutView from '@/views/AboutView.vue'
import DebugView from '@/views/DebugView.vue'
import NewEmoticonListView from '@/views/NewEmoticonListView.vue'
import PopularEmoticonListView from '@/views/PopularEmoticonListView.vue'
import UploadEmoticonView from '@/views/UploadEmoticonView.vue'
import EmoticonDetailView from '@/views/EmoticonDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainView
    },
    {
      path: '/new',
      name: 'newList',
      component: NewEmoticonListView
    },
    {
      path: '/popular',
      name: 'popularList',
      component: PopularEmoticonListView
    },
    {
      path: '/upload',
      name: 'uploadEmoticon',
      component: UploadEmoticonView
    },
    {
      path: '/packDetail/:id',
      name: 'packDetail',
      component: EmoticonDetailView
    }
  ]
})

export default router
