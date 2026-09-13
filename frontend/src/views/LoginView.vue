<template>
  <div class="card" style="max-width: 420px; margin: 0 auto">
    <h2>登录</h2>
    <div class="field">
      <label>学号 / 管理员账号</label>
      <input v-model.trim="form.username" autocomplete="username" />
    </div>
    <div class="field" style="margin-top: 12px">
      <label>密码</label>
      <input v-model="form.password" type="password" autocomplete="current-password" />
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <div style="margin-top: 16px; display: flex; gap: 12px">
      <button :disabled="loading" @click="submit">{{ loading ? '登录中...' : '登录' }}</button>
      <RouterLink to="/register"><button class="ghost">注册新账号</button></RouterLink>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const form = reactive({ username: '', password: '' })
const error = ref('')
const loading = ref(false)
const auth = useAuthStore()
const router = useRouter()

async function submit() {
  error.value = ''
  loading.value = true
  try {
    await auth.login(form.username, form.password)
    router.push(auth.isAdmin ? '/admin/accounts' : '/directory')
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
