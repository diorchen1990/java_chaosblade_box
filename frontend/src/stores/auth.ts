import { defineStore } from 'pinia';
import axios from 'axios';
import { LoginForm, LoginResponse, UserInfo } from '@/types/auth';

interface AuthState {
  token: string | null;
  userInfo: UserInfo | null;
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token'),
    userInfo: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userRoles: (state) => state.userInfo?.roles || []
  },

  actions: {
    async login(form: LoginForm) {
      try {
        const { data } = await axios.post<LoginResponse>('/api/auth/login', form);
        this.setToken(data.token);
        await this.fetchUserInfo();
        return true;
      } catch (error) {
        console.error('Login failed:', error);
        return false;
      }
    },

    async fetchUserInfo() {
      try {
        const { data } = await axios.get<UserInfo>('/api/auth/me');
        this.userInfo = data;
      } catch (error) {
        console.error('Failed to fetch user info:', error);
      }
    },

    setToken(token: string) {
      this.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    },

    logout() {
      this.token = null;
      this.userInfo = null;
      localStorage.removeItem('token');
      delete axios.defaults.headers.common['Authorization'];
    }
  }
}); 