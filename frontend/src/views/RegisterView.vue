<template>
  <div class="card" style="max-width: 480px; margin: 0 auto">
    <h2>注册基本信息</h2>
    <p class="tip">注册后需管理员审核通过，才能登录并完善通讯录。</p>
    <div class="field">
      <label>学号（10 位数字）</label>
      <input v-model.trim="form.username" />
    </div>
    <div class="field" style="margin-top: 12px">
      <label>姓名</label>
      <input v-model.trim="form.realName" />
    </div>
    <div class="field" style="margin-top: 12px">
      <label>密码（至少 8 位）</label>
      <input v-model="form.password" type="password" />
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="done" class="success">{{ done }}</p>
    <div style="margin-top: 16px; display: flex; gap: 12px">
      <button :disabled="loading" @click="submit">提交注册</button>
      <RouterLink to="/login"><button class="ghost">返回登录</button></RouterLink>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { authApi } from '../api'

const form = reactive({ username: '', realName: '', password: '' })
const error = ref('')
const done = ref('')
const loading = ref(false)

async function submit() {
  error.value = ''
  done.value = ''
  loading.value = true
  try {
    const res = await authApi.register({ ...form })
    done.value = res.message || '注册成功，请等待审核'
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
