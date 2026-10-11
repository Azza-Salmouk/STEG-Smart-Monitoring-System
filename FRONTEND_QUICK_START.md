# 🚀 FRONTEND QUICK START GUIDE

## Démarrage en 5 minutes

### Prérequis
- Node.js 16+
- npm ou yarn
- Backend running (`npm run dev` dans `smart-monitoring-backend/`)

### 1. Installation

```bash
cd smart-monitoring-frontend
npm install
```

### 2. Configuration Backend

Vérifier que le backend tourne sur `http://localhost:8081`

Éditer `.env` si nécessaire:
```env
VITE_API_BASE_URL=http://localhost:8081/api
```

### 3. Démarrage du Frontend

```bash
npm run dev
```

Frontend démarre sur `http://localhost:5173`

### 4. Test Connexion

1. Ouvrir http://localhost:5173
2. Vous voyez la page Login
3. Entrer les credentials:
   - Email: `admin@steg.tn`
   - Password: `Admin@123`
4. Cliquer "Connexion"
5. Redirection vers `/admin/dashboard` ✅

---

## 🎯 Flux Principal

```
http://localhost:5173
  ↓
/login (LoginView)
  ↓
Entrer credentials
  ↓
axios POST /api/auth/login
  ↓
Backend retourne JWT token
  ↓
Token sauvé en localStorage + Pinia store
  ↓
Redirection /admin/dashboard
  ↓
Navbar + Sidebar + Contenu dashboard
  ↓
Token automatiquement envoyé avec chaque requête
```

---

## 📁 Structure Importante

```
src/
├── api/
│   ├── axios.js          ← Configuration HTTP + Intercepteurs
│   └── authApi.js        ← Endpoints
├── stores/
│   └── authStore.js      ← Pinia store (token, user, login/logout)
├── router/
│   └── index.js          ← Routes protégées + guards
├── views/
│   ├── LoginView.vue     ← Page connexion
│   └── *Dashboard.vue    ← Dashboards (admin/supervisor/agent)
└── components/
    ├── Navbar.vue        ← Barre top
    ├── Sidebar.vue       ← Menu latéral
    └── *.vue             ← Composants réutilisables
```

---

## 🔄 Cycle de Vie

### Au Démarrage de l'App
1. `main.js` → créer app Vue 3
2. `App.vue` → onMounted() → `authStore.restoreSession()`
3. `router/index.js` → beforeEach guard → vérifier session
4. Redirect vers /login ou dashboard selon état

### À la Connexion
1. `LoginView.vue` → form submit
2. `authStore.login(credentials)`
3. `authApi.login()` → POST /api/auth/login
4. Store sauvegarde token + user
5. Router redirect vers dashboard

### À chaque Requête API
1. `axios.js` → interceptor request
2. Ajoute `Authorization: Bearer TOKEN`
3. Envoi requête
4. Si 401 → logout auto + redirect /login

### À la Déconnexion
1. Navbar → click Logout
2. `authStore.logout()`
3. Vide localStorage + store
4. Router.push('/login')

---

## 🐛 Débogage

### Vérifier l'État du Store
```javascript
// Dans la console du navigateur
import { useAuthStore } from '@/stores/authStore'
const store = useAuthStore()
console.log(store) // voir token, user, role, etc.
```

### Vérifier le Token
```javascript
// Voir le token
localStorage.getItem('token')

// Décoder le token
import { decodeToken } from '@/utils/token'
const decoded = decodeToken(localStorage.getItem('token'))
console.log(decoded)
```

### Vérifier les Requêtes API
- Ouvrir DevTools → Network
- Voir les appels vers `localhost:8081/api`
- Vérifier les headers (Authorization: Bearer...)
- Vérifier les status codes (200, 401, 403, etc.)

---

## 📦 Build pour Production

```bash
npm run build
```

Génère `dist/` prêt à déployer.

Preview:
```bash
npm run preview
```

---

## ⚙️ Configuration

### .env
```env
VITE_API_BASE_URL=http://localhost:8081/api
VITE_APP_NAME=Smart Monitoring
VITE_APP_VERSION=0.1.0
```

### vite.config.js
- Alias `@/` vers `src/`
- Port 5173
- Plugin Vue 3

### tailwind.config.js
- Couleur primary (bleu)
- Couleur gray
- Responsive breakpoints

---

## 🔒 Sécurité

### Bearer Token
Automatiquement ajouté par axios:
```javascript
config.headers.Authorization = `Bearer ${token}`
```

### 401 Auto-Logout
Si 401 reçu:
```javascript
authStore.logout()
window.location.href = '/login'
```

### LocalStorage
Token stocké mais attention:
- ⚠️ LocalStorage = XSS vulnerable
- ✅ Pour dev/POC ok
- 🔒 Production: SessionStorage + HTTP-only cookies

---

## 🛠️ Modifications Courantes

### Ajouter une Route Protégée

1. Dans `src/router/index.js`:
```javascript
{
  path: '/users',
  component: UsersPage,
  meta: { requiresAuth: true, requiredRole: 'ADMIN' }
}
```

2. Créer le composant `src/views/UsersPage.vue`

### Ajouter un Lien Sidebar

1. Dans `src/components/Sidebar.vue`:
```javascript
const menuItems = computed(() => {
  return [
    { label: 'Dashboard', path: '...' },
    { label: 'Utilisateurs', path: '/users', icon: 'users' },
    { label: 'Postes', path: '/posts', icon: 'chart' },
  ]
})
```

### Ajouter un Composant Réutilisable

1. Créer `src/components/MyComponent.vue`
2. Utiliser dans d'autres components:
```vue
<script setup>
import MyComponent from '@/components/MyComponent.vue'
</script>

<template>
  <MyComponent :prop="value" @event="handleEvent" />
</template>
```

---

## 🎨 Tailwind CSS

### Utiliser les Couleurs
```vue
<div class="bg-primary-600 text-white"> ← Bleu primaire
<div class="bg-gray-100 text-gray-900"> ← Gris
```

### Responsive
```vue
<div class="text-sm md:text-lg lg:text-2xl"> ← Tailles selon écran
<div class="grid grid-cols-1 md:grid-cols-2"> ← 1 col mobile, 2 cols desktop
```

### Animations
```vue
<div class="hover:bg-gray-50 transition-colors duration-200"> ← Hover smooth
```

---

## 💡 Bonnes Pratiques

1. **Composition API** → Utiliser `<script setup>`
2. **Store Pinia** → Centraliser l'état
3. **Router Guards** → Protéger les routes
4. **Axios Interceptors** → DRY code
5. **Reusable Components** → Pas de duplication
6. **Props Validation** → Éviter les bugs
7. **Comments** → Documenter le code
8. **Tailwind Classes** → Plutôt que CSS custom

---

## ❓ FAQ

**Q: Token expiré, que faire?**
A: Page automatiquement redirigée vers /login au 401

**Q: Comment tester différents rôles?**
A: Créer d'autres comptes via register, changer le rôle dans le form

**Q: Changer le port frontend?**
A: Éditer `vite.config.js` → `server.port`

**Q: Build trop gros?**
A: Analyser avec `npm run build -- --report`

**Q: Erreur CORS?**
A: Vérifier backend a CORS activé pour `localhost:5173`

---

## 🚀 Prochaines Étapes

1. Tester register + login
2. Tester logout
3. Tester routes protégées (403 si rôle wrong)
4. Tester refresh (page reload = session restaurée)
5. Tester build production
6. Ajouter modules métier (users, posts, etc.)

---

**Happy Coding! 🎉**
