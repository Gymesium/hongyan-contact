<template>
  <div class="card">
    <h2>我的通讯录</h2>
    <p class="tip">仅本人可修改；管理员无权修改你的通讯录信息。</p>
    <div class="form-grid">
      <div class="field">
        <label>学号</label>
        <input :value="form.username" disabled />
      </div>
      <div class="field">
        <label>姓名</label>
        <input :value="form.realName" disabled />
      </div>
      <div class="field">
        <label>所读专业</label>
        <select v-model="form.majorId">
          <option :value="null">请选择</option>
          <option v-for="m in majors" :key="m.id" :value="m.id">{{ m.name }}</option>
        </select>
      </div>
      <div class="field">
        <label>所在班级</label>
        <input v-model.trim="form.className" placeholder="如 计科2201" />
      </div>
      <div class="field">
        <label>入校年份</label>
        <input v-model.number="form.enrollYear" type="number" />
      </div>
      <div class="field">
        <label>毕业年份</label>
        <input v-model.number="form.graduateYear" type="number" />
      </div>
      <div class="field">
        <label>就业单位</label>
        <input v-model.trim="form.employer" />
      </div>
      <div class="field">
        <label>所在城市</label>
        <input v-model.trim="form.city" />
      </div>
      <div class="field">
        <label>联系方式</label>
        <input v-model.trim="form.phone" />
      </div>
      <div class="field">
        <label>电子邮箱</label>
        <input v-model.trim="form.email" />
      </div>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="done" class="success">{{ done }}</p>
    <div style="margin-top: 16px">
      <button :disabled="loading" @click="save">保存</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { majorApi, profileApi } from '../api'

const majors = ref([])
const error = ref('')
const done = ref('')
const loading = ref(false)
const form = reactive({
  username: '', realName: '', majorId: null, className: '',
  enrollYear: null, graduateYear: null, employer: '', city: '', phone: '', email: ''
})

onMounted(async () => {
  try {
    majors.value = await majorApi.listEnabled()
    Object.assign(form, await profileApi.mine())
  } catch (e) {
    error.value = e.message
  }
})

async function save() {
  error.value = ''
  done.value = ''
  loading.value = true
  try {
    const payload = {
      majorId: form.majorId,
      className: form.className,
      enrollYear: form.enrollYear,
      graduateYear: form.graduateYear,
      employer: form.employer,
      city: form.city,
      phone: form.phone,
      email: form.email
    }
    Object.assign(form, await profileApi.save(payload))
    done.value = '保存成功'
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
