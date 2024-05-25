import { defineStore } from 'pinia';
import {
  login as userLogin,
  logout as userLogout,
  getMyProfile,
  LoginData,
} from '@/api/user';
import { setToken, clearToken } from '@/utils/auth';
import { removeRouteListener } from '@/utils/route-listener';
import { assign } from 'lodash';
import { UserState } from './types';
import useAppStore from '../app';

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    id: '',
    nickname: '',
    real_name: '',
    description: '',
    email: '',
    phone: '',
    gender: '',
    permission_group: '',
    avatar_url: '',
  }),

  getters: {
    userInfo(state: UserState): UserState {
      const newinfo = {
        id: state.id,
        nickname: state.nickname,
        real_name: state.real_name,
        description: state.description,
        email: state.email,
        phone: state.phone,
        gender: state.gender,
        permission_group: state.permission_group,
        avatar_url: state.avatar_url,
      };
      return { ...newinfo };
    },
  },

  actions: {
    switchRoles() {
      return new Promise((resolve) => {
        console.log(this.permission_group);
        // this.permission_group = this.permission_group === 'USER' ? 'ADMIN' : 'USER';
        resolve(this.permission_group);
      });
    },
    // Set user's information
    setInfo(partial: Partial<UserState>) {
      this.$patch(partial);
    },

    // Reset user's information
    resetInfo() {
      this.$reset();
    },

    // Get user's information
    async info() {
      const res = await getMyProfile();

      this.setInfo(res.data);
    },

    // Login
    async login(loginForm: LoginData) {
      try {
        const res = await userLogin(loginForm);
        const token = res.data.access_token;
        const expire = res.data.expire_time;

        setToken(token, expire);
      } catch (err) {
        clearToken();
        console.log(err);
        throw err;
      }
    },
    logoutCallBack() {
      const appStore = useAppStore();
      this.resetInfo();
      clearToken();
      removeRouteListener();
      appStore.clearServerMenu();
    },
    // Logout
    async logout() {
      try {
        await userLogout();
      } finally {
        this.logoutCallBack();
      }
    },
  },
});

export default useUserStore;
