import { resolve } from 'path';
import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import svgLoader from 'vite-svg-loader';
import configArcoStyleImportPlugin from './plugin/arcoStyleImport';

export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    svgLoader({ svgoConfig: {} }),
    configArcoStyleImportPlugin(),
  ],
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, '../src'),
      },
      {
        find: 'assets',
        replacement: resolve(__dirname, '../src/assets'),
      },
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js', // Resolve the i18n warning issue
      },
      {
        find: 'vue',
        replacement: 'vue/dist/vue.esm-bundler.js', // compile template
      },
    ],
    extensions: ['.ts', '.js'],
  },

  server: {
    proxy: {
      '/api': {
        target:
        //   loadEnv('production', process.cwd()).VITE_PROXY_API_TARGET ||
          'http://sustc.mark455.cn:8080/api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/'),
      },
      '/images': {
        target:
        //   loadEnv('production', process.cwd()).VITE_PROXY_IMAGE_TARGET ||
          'http://sustc.mark455.cn:19000/images',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/images/, '/'),
      },
      '/documents': {
        target:
        //   loadEnv('production', process.cwd()).VITE_PROXY_DOC_TARGET ||
          'http://sustc.mark455.cn:19000/documents',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/documents/, '/'),
      },
    },
  },

  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          hack: `true; @import (reference) "${resolve(
            'src/assets/style/breakpoint.less'
          )}";`,
        },
        javascriptEnabled: true,
      },
    },
  },
});
