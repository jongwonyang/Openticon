import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import AboutView from '@/views/AboutView.vue'
import DebugView from '@/views/DebugView.vue'
import NewEmoticonListView from '@/views/NewEmoticonListView.vue'
import PopularEmoticonListView from '@/views/PopularEmoticonListView.vue'
import UploadEmoticonView from '@/views/UploadEmoticonView.vue'
import EmoticonDetailView from '@/views/EmoticonDetailView.vue'
import ProcessLoginView from '@/views/ProcessLogin.vue'
import MypageView from '@/views/MypageView.vue'
import { useUserStore } from '@/stores/user'
import { makeWarningAlert } from '@/util/alert'

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
    },
    {
      path: '/processLogin',
      name: 'processLogin',
      component: ProcessLoginView
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: MypageView,
      beforeEnter: (to, from, next) => {
        const isLoggedIn = useUserStore().isLogined; // 로그인 상태 확인
        if (isLoggedIn) {
          next(); // 로그인 상태이면 접근 허용
        } else {
          makeWarningAlert('로그인이 필요한 서비스입니다. 메인페이지로 이동합니다.');
          next({ name: 'main' }); // 로그인 상태가 아니면 메인 페이지로 리다이렉트
        }
      },
    }
  ]
})

export default router
