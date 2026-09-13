import axios from 'axios'

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || '/api',
  timeout: 10000
})

// 请求拦截器：给每个 Ajax 请求带上 JWT
http.interceptors.request.use((config) => {
  const token = localStorage.getItem('hy_token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

// 响应拦截器：统一提取后端错误消息
http.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error.response ? error.response.status : 0
    const message =
      error.response && error.response.data && error.response.data.message
        ? error.response.data.message
        : '网络异常，请稍后重试'
    if (status === 401) {
      localStorage.removeItem('hy_token')
      localStorage.removeItem('hy_user')
    }
    return Promise.reject(new Error(message))
  }
)

export default http
