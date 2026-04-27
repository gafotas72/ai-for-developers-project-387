import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/owner?tab=event-types'
  },
  {
    path: '/owner',
    name: 'owner',
    component: () => import('@/views/OwnerLayout.vue')
  },
  {
    path: '/guest',
    name: 'guest',
    component: () => import('@/views/guest/GuestPage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
