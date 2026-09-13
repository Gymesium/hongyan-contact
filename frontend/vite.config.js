import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 开发期把 /api 代理到本地 Spring Boot，避开跟源问题。
      // 注意：后端 server.servlet.context-path=/hongyan，因此目标要带 /hongyan 前缀，
      // 否则开发环境请求会 404（生产环境由 VITE_API_BASE 直接写完整地址）。
      '/api': {
        target: 'http://localhost:8080/hongyan',
        changeOrigin: true
      }
    }
  },
  build: { outDir: 'dist' }
})
