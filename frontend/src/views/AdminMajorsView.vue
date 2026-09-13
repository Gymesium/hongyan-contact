<template>
  <div class="card">
    <h2>专业信息维护</h2>
    <div class="toolbar">
      <div class="field">
        <label>专业代码</label>
        <input v-model.trim="form.code" placeholder="如 CS" />
      </div>
      <div class="field">
        <label>专业名称</label>
        <input v-model.trim="form.name" placeholder="如 计算机科学与技术" />
      </div>
      <button @click="submit">{{ editingId ? '保存修改' : '新增专业' }}</button>
      <button v-if="editingId" class="ghost" @click="cancel">取消</button>
    </div>

    <table>
      <thead>
        <tr><th>代码</th><th>名称</th><th>状态</th><th>操作</th></tr>
      </thead>
      <tbody>
        <tr v-for="m in majors" :key="m.id">
          <td>{{ m.code }}</td>
          <td>{{ m.name }}</td>
          <td>{{ m.enabled ? '启用' : '停用' }}</td>
          <td style="display: flex; gap: 8px">
            <button class="ghost" @click="edit(m)">编辑</button>
            <button v-if="m.enabled" class="danger" @click="disable(m)">停用</button>
            <button v-else @click="enable(m)">启用</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { majorApi } from '../api'

const majors = ref([])
const error = ref('')
const editingId = ref(null)
const form = reactive({ code: '', name: '' })

onMounted(load)

async function load() {
  try {
    majors.value = await majorApi.listAll()
  } catch (e) {
    error.value = e.message
  }
}

function edit(major) {
  editingId.value = major.id
  form.code = major.code
  form.name = major.name
}

function cancel() {
  editingId.value = null
  form.code = ''
  form.name = ''
}

async function submit() {
  error.value = ''
  try {
    if (editingId.value) {
      await majorApi.update(editingId.value, { code: form.code, name: form.name, enabled: true })
    } else {
      await majorApi.create({ code: form.code, name: form.name, enabled: true })
    }
    cancel()
    load()
  } catch (e) {
    error.value = e.message
  }
}

async function disable(major) {
  try {
    await majorApi.disable(major.id)
    load()
  } catch (e) {
    error.value = e.message
  }
}

async function enable(major) {
  try {
    await majorApi.update(major.id, { code: major.code, name: major.name, enabled: true })
    load()
  } catch (e) {
    error.value = e.message
  }
}
</script>
