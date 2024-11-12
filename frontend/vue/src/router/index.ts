import { createRouter, createWebHistory } from "vue-router";
import MainView from "../views/MainView.vue";
import NewEmoticonListView from "@/views/QueryEmoticon/NewEmoticonListView.vue";
import PopularEmoticonListView from "@/views/QueryEmoticon/PopularEmoticonListView.vue";
import UploadEmoticonView from "@/views/MakeEmoticon/UploadEmoticonView.vue";
import EmoticonDetailView from "@/views/QueryEmoticon/EmoticonDetailView.vue";
import ProcessLoginView from "@/views/Utils/ProcessLogin.vue";
import MypageView from "@/views/MyPage/MypageView.vue";
import { useUserStore } from "@/stores/user";
import { makeWarningAlert } from "@/util/alert";
import SearchResultView from "@/views/QueryEmoticon/SearchResultView.vue";
import EmoticonDetailPrivateView from "@/views/QueryEmoticon/EmoticonDetailPrivateView.vue";
import BlacklistView from "@/views/MyPage/BlacklistView.vue";
import MethodSelectView from "@/views/MakeEmoticon/MethodSelectView.vue";
import AICreateEmoticonView from "@/views/MakeEmoticon/AICreateEmoticonView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: MainView,
    },
    {
      path: "/new",
      name: "newList",
      component: NewEmoticonListView,
    },
    {
      path: "/popular",
      name: "popularList",
      component: PopularEmoticonListView,
    },
    {
      path: "/make",
      name: "methodSelect",
      component: MethodSelectView,
    },
    {
      path: "/make/upload",
      name: "uploadEmoticon",
      component: UploadEmoticonView,
      beforeEnter: (to, from, next) => {
        const isLoggedIn = useUserStore().isLogined; // 로그인 상태 확인
        if (isLoggedIn) {
          next(); // 로그인 상태이면 접근 허용
        } else {
          makeWarningAlert(
            "로그인이 필요한 서비스입니다. 메인페이지로 이동합니다."
          );
          next({ name: "main" }); // 로그인 상태가 아니면 메인 페이지로 리다이렉트
        }
      },
    },
    {
      path: "/make/create",
      name: "createEmoticon",
      component: AICreateEmoticonView,
      beforeEnter: (to, from, next) => {
        const isLoggedIn = useUserStore().isLogined; // 로그인 상태 확인
        if (isLoggedIn) {
          next(); // 로그인 상태이면 접근 허용
        } else {
          makeWarningAlert(
            "로그인이 필요한 서비스입니다. 메인페이지로 이동합니다."
          );
          next({ name: "main" }); // 로그인 상태가 아니면 메인 페이지로 리다이렉트
        }
      },
    },
    {
      path: "/packDetail/:id",
      name: "packDetail",
      component: EmoticonDetailView,
    },
    {
      path: "/packDetailPrivate/:id",
      name: "packDetailPrivate",
      component: EmoticonDetailPrivateView,
    },
    {
      path: "/processLogin",
      name: "processLogin",
      component: ProcessLoginView,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MypageView,
      beforeEnter: (to, from, next) => {
        const isLoggedIn = useUserStore().isLogined; // 로그인 상태 확인
        if (isLoggedIn) {
          next(); // 로그인 상태이면 접근 허용
        } else {
          makeWarningAlert(
            "로그인이 필요한 서비스입니다. 메인페이지로 이동합니다."
          );
          next({ name: "main" }); // 로그인 상태가 아니면 메인 페이지로 리다이렉트
        }
      },
    },
    {
      path: "/search",
      name: "searchResult",
      component: SearchResultView,
    },
    {
      path: "/blacklist",
      name: "blacklist",
      component: BlacklistView,
    },
  ],
});

export default router;
