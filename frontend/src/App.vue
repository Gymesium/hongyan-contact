<template>
  <div class="layout">
    <header class="topbar">
      <div class="brand">🕊️ 鸿雁通讯录</div>
      <nav v-if="auth.isLogged">
        <RouterLink to="/directory">同学通讯录</RouterLink>
        <RouterLink v-if="!auth.isAdmin" to="/profile">我的资料</RouterLink>
        <RouterLink v-if="auth.isAdmin" to="/admin/accounts">账户审核</RouterLink>
        <RouterLink v-if="auth.isAdmin" to="/admin/majors">专业维护</RouterLink>
      </nav>
      <div class="user" v-if="auth.isLogged">
        <span>{{ auth.user.realName }}</span>
        <span class="muted" v-if="auth.user.loginCount">第 {{ auth.user.loginCount }} 次登录</span>
        <button class="link" @click="onLogout">退出</button>
      </div>
    </header>
    <main class="content">
      <RouterView />
    </main>
    <footer class="footer">WEB程序设计大作业 · 鸿雁通讯录</footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const auth = useAuthStore()
const router = useRouter()

onMounted(() => {
  auth.refresh().catch(() => auth.logout())
})

function onLogout() {
  auth.logout()
  router.push('/login')
}
</script>
