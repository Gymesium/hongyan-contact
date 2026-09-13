import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', redirect: '/directory' },
  { path: '/login', component: () => import('../views/LoginView.vue'), meta: { guest: true } },
  { path: '/register', component: () => import('../views/RegisterView.vue'), meta: { guest: true } },
  { path: '/directory', component: () => import('../views/DirectoryView.vue'), meta: { auth: true } },
  { path: '/profile', component: () => import('../views/ProfileView.vue'), meta: { auth: true } },
  { path: '/admin/accounts', component: () => import('../views/AdminAccountsView.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/majors', component: () => import('../views/AdminMajorsView.vue'), meta: { auth: true, admin: true } },
  { path: '/:pathMatch(.*)*', redirect: '/directory' }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.auth && !auth.isLogged) return '/login'
  if (to.meta.admin && !auth.isAdmin) return '/directory'
  if (to.meta.guest && auth.isLogged) return '/directory'
  return true
})

export default router
