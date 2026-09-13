<template>
  <div class="card">
    <h2>账户审核与管理</h2>
    <div class="toolbar">
      <div class="field">
        <label>状态</label>
        <select v-model="query.status">
          <option value="">全部</option>
          <option value="PENDING">待审核</option>
          <option value="APPROVED">已通过</option>
          <option value="REJECTED">已驳回</option>
          <option value="DISABLED">已禁用</option>
        </select>
      </div>
      <div class="field">
        <label>关键字</label>
        <input v-model.trim="query.keyword" placeholder="学号 / 姓名" />
      </div>
      <button @click="reload(0)">查询</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>学号</th><th>姓名</th><th>状态</th><th>注册时间</th>
          <th>最近登录</th><th>登录次数</th><th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in items" :key="a.id">
          <td>{{ a.username }}</td>
          <td>{{ a.realName }}</td>
          <td>{{ statusText(a.status) }}</td>
          <td>{{ fmt(a.createdAt) }}</td>
          <td>{{ fmt(a.lastLoginAt) }}</td>
          <td>{{ a.loginCount }}</td>
          <td style="display: flex; gap: 8px; flex-wrap: wrap">
            <button v-if="a.status === 'PENDING'" @click="act(a, 'approve')">通过</button>
            <button v-if="a.status === 'PENDING'" class="ghost" @click="act(a, 'reject')">驳回</button>
            <button v-if="a.status === 'APPROVED'" class="ghost" @click="act(a, 'disable')">禁用</button>
            <button v-if="a.status === 'DISABLED'" @click="act(a, 'enable')">启用</button>
            <button
              v-if="a.status === 'PENDING' || a.status === 'REJECTED'"
              class="danger"
              @click="remove(a)"
            >删除</button>
          </td>
        </tr>
        <tr v-if="!items.length"><td colspan="7" class="tip">暂无账户</td></tr>
      </tbody>
    </table>

    <div class="pager">
      <button class="ghost" :disabled="page === 0" @click="reload(page - 1)">上一页</button>
      <span class="tip">第 {{ page + 1 }} 页 / 共 {{ total }} 条</span>
      <button class="ghost" :disabled="(page + 1) * 10 >= total" @click="reload(page + 1)">下一页</button>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <p class="tip">说明：已审核通过的账户只能禁用、不能删除；管理员无法修改学生通讯录内容。</p>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { adminApi } from '../api'

const items = ref([])
const total = ref(0)
const page = ref(0)
const error = ref('')
const query = reactive({ status: 'PENDING', keyword: '' })

const statusMap = {
  PENDING: '待审核',
  APPROVED: '已通过',
  REJECTED: '已驳回',
  DISABLED: '已禁用'
}

onMounted(() => reload(0))

function statusText(s) {
  return statusMap[s] || s
}

function fmt(value) {
  return value ? new Date(value).toLocaleString('zh-CN') : '-'
}

async function reload(nextPage) {
  error.value = ''
  try {
    const data = await adminApi.accounts({
      status: query.status || undefined,
      keyword: query.keyword || undefined,
      page: nextPage,
      size: 10
    })
    items.value = data.items
    total.value = data.total
    page.value = data.page
  } catch (e) {
    error.value = e.message
  }
}

async function act(account, action) {
  error.value = ''
  try {
    await adminApi[action](account.id)
    reload(page.value)
  } catch (e) {
    error.value = e.message
  }
}

async function remove(account) {
  if (!window.confirm('确认删除账户 ' + account.username + ' ？')) return
  try {
    await adminApi.remove(account.id)
    reload(page.value)
  } catch (e) {
    error.value = e.message
  }
}
</script>
