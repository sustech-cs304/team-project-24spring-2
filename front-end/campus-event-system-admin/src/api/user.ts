import axios from 'axios';
import type { RouteRecordNormalized } from 'vue-router';
import { UserState } from '@/store/modules/user/types';

export interface LoginData {
  username: string;
  password: string;
}

export interface LoginRes {
  access_token: string;
  expire_time: number;
  token_type: string;
  user: UserState;
}

export function login(data: LoginData) {
  const formal = {
    user_input: data.username,
    password: data.password,
  };
  return axios.post<LoginRes>('/api/user/login', formal);
}

export function logout() {
  //   return axios.post<LoginRes>('/api/user/logout');
  return true;
}

export function getMyProfile() {
  return axios.post<UserState>('/api/user/profile');
}



export function getMenuList() {
  return axios.post<RouteRecordNormalized[]>('/api/user/menu');
}

export function getUploadImages() {
  return axios.post<string[]>('/api/user/get-upload-images');
}
