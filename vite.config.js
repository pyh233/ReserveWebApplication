import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from "path"

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    open: true,
    port: 2333,
    proxy: {
      "/api": {
        target: "http://localhost:8080/",//代理地址
        changeOrigin: true,//允许跨域
        rewrite: (path) => path.replace(/^\/api/, "/api/v1")//重写路径
      },
      "/static": {
        target: "http://localhost:8080/",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/static/, '/static'),
      }
    }
  },
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, "src")
      }
    ]
  }
})
