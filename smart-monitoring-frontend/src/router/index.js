import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

// Pages
import LoginView from '@/views/LoginView.vue'
import AdminDashboard from '@/views/ModernAdminDashboard.vue'
import SupervisorDashboard from '@/views/SupervisorDashboard.vue'
import AgentDashboard from '@/views/AgentDashboard.vue'
import ServiceClientDashboard from '@/views/ServiceClientDashboard.vue'
import UsersManagementNew from '@/views/UsersManagementNew.vue'
import PostesManagementNew from '@/views/PostesManagementNew.vue'
import TransformateursManagementNew from '@/views/TransformateursManagementNew.vue'
import TransformateurDetail from '@/views/TransformateurDetail.vue'
import MeasurementsManagement from '@/views/MeasurementsManagement.vue'
import InterventionsManagement from '@/views/InterventionsManagement.vue'
import ComplaintsManagement from '@/views/ComplaintsManagement.vue'
import AlertsManagement from '@/views/AlertsManagement.vue'
import ChatbotInterface from '@/views/ChatbotInterface.vue'
import Settings from '@/views/Settings.vue'
import ProfileView from '@/views/ProfileView.vue'
import UnauthorizedView from '@/views/UnauthorizedView.vue'
import NotFoundView from '@/views/NotFoundView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: { requiresAuth: false }
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true, roles: ['ADMIN'] }
  },
  {
    path: '/supervisor/dashboard',
    name: 'SupervisorDashboard',
    component: SupervisorDashboard,
    meta: { requiresAuth: true, roles: ['SUPERVISOR'] }
  },
  {
    path: '/agent/dashboard',
    name: 'AgentDashboard',
    component: AgentDashboard,
    meta: { requiresAuth: true, roles: ['AGENT'] }
  },
  {
    path: '/serviceclient/dashboard',
    name: 'ServiceClientDashboard',
    component: ServiceClientDashboard,
    meta: { requiresAuth: true, roles: ['SERVICE_CLIENT'] }
  },
  {
    path: '/users',
    name: 'UsersManagement',
    component: UsersManagementNew,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR'] }
  },
  {
    path: '/postes',
    name: 'PostesManagement',
    component: PostesManagementNew,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR'] }
  },
  {
    path: '/transformateurs',
    name: 'TransformateursManagement',
    component: TransformateursManagementNew,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT'] }
  },
  {
    path: '/transformateurs/:id',
    name: 'TransformateurDetail',
    component: TransformateurDetail,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT'] }
  },
  {
    path: '/measurements',
    name: 'MeasurementsManagement',
    component: MeasurementsManagement,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT'] }
  },
  {
    path: '/interventions',
    name: 'InterventionsManagement',
    component: InterventionsManagement,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT'] }
  },
  {
    path: '/complaints',
    name: 'ComplaintsManagement',
    component: ComplaintsManagement,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT', 'SERVICE_CLIENT'] }
  },
  {
    path: '/alerts',
    name: 'AlertsManagement',
    component: AlertsManagement,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT'] }
  },
  {
    path: '/chatbot',
    name: 'Chatbot',
    component: ChatbotInterface,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT', 'SERVICE_CLIENT'] }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT', 'SERVICE_CLIENT'] }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileView,
    meta: { requiresAuth: true, roles: ['ADMIN', 'SUPERVISOR', 'AGENT', 'SERVICE_CLIENT'] }
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: UnauthorizedView,
    meta: { requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFoundView,
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * Guard global pour la gestion de l'authentification et des rôles
 */
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()

  // Restaurer la session si elle n'est pas déjà restaurée
  if (!authStore.token) {
    authStore.restoreSession()
  }

  const isAuthenticated = authStore.isAuthenticated
  const userRole = authStore.role

  // Si la route nécessite l'authentification
  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      // Rediriger vers la page de connexion
      next('/login')
      return
    }

    // Vérifier le rôle si requis
    if (to.meta.roles && to.meta.roles.length > 0) {
      if (!to.meta.roles.includes(userRole)) {
        // Rediriger vers le dashboard approprié
        if (userRole === 'ADMIN') next('/admin/dashboard')
        else if (userRole === 'SUPERVISOR') next('/supervisor/dashboard')
        else if (userRole === 'AGENT') next('/agent/dashboard')
        else if (userRole === 'SERVICE_CLIENT') next('/serviceclient/dashboard')
        else next('/unauthorized')
        return
      }
    }
  }

  // Si l'utilisateur est connecté et essaie d'accéder à /login
  if (to.path === '/login' && isAuthenticated) {
    // Rediriger vers le dashboard approprié
    if (userRole === 'ADMIN') next('/admin/dashboard')
    else if (userRole === 'SUPERVISOR') next('/supervisor/dashboard')
    else if (userRole === 'AGENT') next('/agent/dashboard')
    else if (userRole === 'SERVICE_CLIENT') next('/serviceclient/dashboard')
    else next('/login')
    return
  }

  next()
})

export default router
