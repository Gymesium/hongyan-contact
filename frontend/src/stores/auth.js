import { defineStore } from 'pinia'
import { authApi } from '../api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('hy_token') || '',
    user: JSON.parse(localStorage.getItem('hy_user') || 'null')
  }),
  getters: {
    isLogged: (state) => Boolean(state.token),
    isAdmin: (state) => Boolean(state.user) && state.user.role === 'ADMIN'
  },
  actions: {
    async login(username, password) {
      const data = await authApi.login({ username, password })
      this.token = data.token
      this.user = data.user
      localStorage.setItem('hy_token', data.token)
      localStorage.setItem('hy_user', JSON.stringify(data.user))
    },
    async refresh() {
      if (!this.token) return
      this.user = await authApi.me()
      localStorage.setItem('hy_user', JSON.stringify(this.user))
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('hy_token')
      localStorage.removeItem('hy_user')
    }
  }
})
