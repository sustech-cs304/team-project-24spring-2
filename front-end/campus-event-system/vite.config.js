import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080/api/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/images': {
        target: 'http://localhost:19000/images/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/images/, '')
      },
      '/documents': {
        target: 'http://localhost:19000/documents/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/documents/, '')
      },
      '/comments': {
        target: 'http://localhost:19000/comments/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/comments/, '')
      },

    }
  }
  
})
