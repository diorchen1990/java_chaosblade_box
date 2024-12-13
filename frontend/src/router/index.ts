import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import DatabaseConfig from '@/views/settings/DatabaseConfig.vue'
import Layout from '@/layouts/default.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/cases',
    component: () => import('@/views/cases/Index.vue'),
    children: [
      { 
        path: 'list',
        component: () => import('@/views/cases/List.vue'),
        props: route => ({
          preloadEdit: () => import('@/views/cases/Edit.vue'),
          preloadCreate: () => import('@/views/cases/Create.vue')
        })
      }
    ]
  },
  {
    path: '/experiments',
    component: () => import('@/views/experiments/Index.vue'),
    children: [
      { path: 'java', component: () => import('@/views/experiments/Java.vue') },
      { path: 'server', component: () => import('@/views/experiments/Server.vue') },
      { path: 'database', component: () => import('@/views/experiments/Database.vue') },
      { path: 'distributed', component: () => import('@/views/experiments/Distributed.vue') }
    ]
  },
  {
    path: '/monitor',
    component: () => import('@/views/monitor/Index.vue')
  },
  {
    path: '/probes',
    component: () => import('@/views/probes/Index.vue')
  },
  {
    path: '/settings',
    component: Layout,
    children: [
      {
        path: 'database',
        name: 'DatabaseConfig',
        component: DatabaseConfig,
        meta: {
          title: '数据库配置',
          requiresAuth: true
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)

  if (requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router 