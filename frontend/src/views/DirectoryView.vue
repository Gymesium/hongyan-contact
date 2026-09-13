<template>
  <div class="card">
    <h2>同学通讯录</h2>
    <div class="toolbar">
      <div class="field">
        <label>专业</label>
        <select v-model="query.majorId">
          <option :value="null">全部</option>
          <option v-for="m in majors" :key="m.id" :value="m.id">{{ m.name }}</option>
        </select>
      </div>
      <div class="field">
        <label>入校年份</label>
        <input v-model.number="query.enrollYear" type="number" placeholder="如 2022" />
      </div>
      <div class="field">
        <label>关键字</label>
        <input v-model.trim="query.keyword" placeholder="姓名 / 班级 / 单位 / 城市" />
      </div>
      <button @click="reload(0)">查询</button>
      <button class="ghost" @click="reset">重置</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>姓名</th><th>学号</th><th>专业</th><th>班级</th>
          <th>入校/毕业</th><th>就业单位</th><th>城市</th><th>联系方式</th><th>邮箱</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.username">
          <td>{{ item.realName }}</td>
          <td>{{ item.username }}</td>
          <td>{{ item.majorName || '-' }}</td>
          <td>{{ item.className || '-' }}</td>
          <td>{{ (item.enrollYear || '-') + ' / ' + (item.graduateYear || '-') }}</td>
          <td>{{ item.employer || '-' }}</td>
          <td>{{ item.city || '-' }}</td>
          <td>{{ item.phone || '-' }}</td>
          <td>{{ item.email || '-' }}</td>
        </tr>
        <tr v-if="!items.length">
          <td colspan="9" class="tip">暂无符合条件的同学</td>
        </tr>
      </tbody>
    </table>

    <div class="pager">
      <button class="ghost" :disabled="page === 0" @click="reload(page - 1)">上一页</button>
      <span class="tip">第 {{ page + 1 }} 页 / 共 {{ total }} 人</span>
      <button class="ghost" :disabled="(page + 1) * size >= total" @click="reload(page + 1)">下一页</button>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { directoryApi, majorApi } from '../api'

const majors = ref([])
const items = ref([])
const total = ref(0)
const page = ref(0)
const size = ref(10)
const error = ref('')
const query = reactive({ majorId: null, enrollYear: null, keyword: '' })

onMounted(async () => {
  majors.value = await majorApi.listEnabled().catch(() => [])
  reload(0)
})

async function reload(nextPage) {
  error.value = ''
  try {
    const data = await directoryApi.search({
      majorId: query.majorId || undefined,
      enrollYear: query.enrollYear || undefined,
      keyword: query.keyword || undefined,
      page: nextPage,
      size: size.value
    })
    items.value = data.items
    total.value = data.total
    page.value = data.page
  } catch (e) {
    error.value = e.message
  }
}

function reset() {
  query.majorId = null
  query.enrollYear = null
  query.keyword = ''
  reload(0)
}
</script>
