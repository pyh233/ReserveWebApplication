import { createApp } from 'vue'
import '@/style.css'
import App from '@/App.vue'
// index.js可以省略
import router from "@/router/index.js"
// 导入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);
app.use(ElementPlus, {
    locale: zhCn,
});    // 使用ElementPlus
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
};    // 使用ElementPlus的图标
app.use(router);    // 使用路由
app.mount('#app');
