# Guide de Référence Rapide - Postes & Transformateurs Frontend

**Date**: 14 Juillet 2026

---

## 🚀 DÉMARRAGE RAPIDE

### Serveurs

```bash
# Terminal 1 - Frontend
cd smart-monitoring-frontend
npm run dev
# → http://localhost:5174

# Terminal 2 - Backend
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run
# → http://localhost:8081
```

### Accès Modules

1. **Aller à**: http://localhost:5174
2. **Connexion Admin**: admin@steg.tn / Admin@123
3. **Menu**: Postes | Transformateurs
4. **Dashboard**: Voir statistiques
5. **Action**: Créer/Modifier/Supprimer

---

## 📁 FICHIERS CLÉS

### Composants

| Fichier | Fonction |
|---------|----------|
| `PosteFormModal.vue` | Modal créer/modifier postes |
| `TransformateurFormModal.vue` | Modal créer/modifier transformateurs |
| `HealthScoreBar.vue` | Barre health score colorée |
| `StatusBadge.vue` | Badge statut 5 couleurs |
| `RiskLevelBadge.vue` | Badge risque 4 couleurs |

### Vues

| Fichier | Fonction |
|---------|----------|
| `PostesManagement.vue` | Vue principale postes |
| `TransformateursManagement.vue` | Vue principale transformateurs |

### État & Logique

| Fichier | Fonction |
|---------|----------|
| `posteStore.js` | État et actions postes |
| `transformateurStore.js` | État et actions transformateurs |
| `router/index.js` | Routes application |
| `Sidebar.vue` | Menu navigation |

---

## 🎯 FONCTIONNALITÉS PAR MODULE

### Postes Management

```
📋 Liste paginée          → Table 10 par page
🔍 Recherche              → Par nom (300ms délai)
🏷️  Filtres               → Région, État
📊 Statistiques           → 4 cartes colorées
➕ Créer                   → Admin only
✏️  Modifier               → Admin/Supervisor
🗑️  Supprimer              → Admin only + confirmation
```

### Transformateurs Management

```
📋 Liste paginée          → Table 10 par page
🔍 Recherche              → Par référence (300ms)
🏷️  Filtres               → Fabricant, Statut, Risque
📊 Statistiques           → 4 cartes colorées
💚 Health Score           → Barre colorée 0-100%
⚠️  Risk Level             → Badge 4 couleurs
🟢 Status                 → Badge 5 couleurs
➕ Créer                   → Admin only
✏️  Modifier               → Admin/Supervisor
🗑️  Supprimer              → Admin only + confirmation
```

---

## 🔐 SÉCURITÉ & RÔLES

### Permissions

| Feature | Admin | Supervisor | Agent |
|---------|-------|-----------|-------|
| Voir | ✅ | ✅ | ❌ |
| Créer | ✅ | ❌ | ❌ |
| Modifier | ✅ | ✅ | ❌ |
| Supprimer | ✅ | ❌ | ❌ |

### Routes

```javascript
/postes              → [ADMIN, SUPERVISOR]
/transformateurs     → [ADMIN, SUPERVISOR]
```

---

## 🔗 ENDPOINTS API

### Postes

```
GET    /api/postes
GET    /api/postes/{id}
POST   /api/postes
PUT    /api/postes/{id}
DELETE /api/postes/{id}
GET    /api/postes/search/advanced
GET    /api/postes/statistics
```

### Transformateurs

```
GET    /api/transformateurs
GET    /api/transformateurs/{id}
POST   /api/transformateurs
PUT    /api/transformateurs/{id}
DELETE /api/transformateurs/{id}
GET    /api/transformateurs/by-poste/{id}
GET    /api/transformateurs/search/advanced
GET    /api/transformateurs/statistics
```

---

## 📊 STATISTIQUES AFFICHÉES

### Postes

- **Total Postes** - Nombre total
- **Actifs** - Postes ACTIF
- **Inactifs** - Postes INACTIF
- **Maintenance** - Postes en MAINTENANCE

### Transformateurs

- **Total Transformateurs** - Nombre total
- **Actifs** - Transformateurs ACTIF
- **Health Score Moyen** - % moyen
- **Critiques** - Transformateurs CRITICAL

---

## 🎨 COULEURS & STYLES

### Health Score Bar

```
80-100% → 🟢 Vert       (#10B981)
60-79%  → 🟡 Jaune      (#F59E0B)
40-59%  → 🟠 Orange     (#F97316)
0-39%   → 🔴 Rouge      (#EF4444)
```

### Status Badge

```
ACTIF        → 🟢 Vert       (#10B981)
ALERTE       → 🟡 Jaune      (#F59E0B)
DEFAUT       → 🟠 Orange     (#F97316)
MAINTENANCE  → 🔵 Bleu       (#3B82F6)
HORS_SERVICE → 🔴 Rouge      (#EF4444)
```

### Risk Level Badge

```
LOW      → 🟢 Vert       (#10B981) - Bas
MEDIUM   → 🟡 Jaune      (#F59E0B) - Moyen
HIGH     → 🟠 Orange     (#F97316) - Haut
CRITICAL → 🔴 Rouge      (#EF4444) - Critique
```

---

## ⚡ ACTIONS RAPIDES

### Créer Poste

1. Clic "Ajouter Poste"
2. Remplir formulaire
3. Clic "Enregistrer"

### Modifier Poste

1. Clic "Modifier" sur ligne
2. Modifier champs
3. Clic "Enregistrer"

### Supprimer Poste

1. Clic "Supprimer" (Admin only)
2. Confirmer suppression
3. Poste supprimé

### Rechercher

1. Taper dans champs recherche
2. Attendre 300ms
3. Résultats filtrés automatiquement

### Filtrer

1. Sélectionner filtre
2. Résultats mis à jour
3. Combiner multiple filtres

### Paginer

1. Clic "Suivant" ou "Précédent"
2. Nouvelle page affichée

---

## 🔧 DÉVELOPPEMENT

### Structure Composant Vue 3

```vue
<template>
  <!-- HTML structure -->
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from '@/stores/store'

const store = useStore()
const data = ref([])

onMounted(async () => {
  await store.fetch()
})
</script>

<style scoped>
/* Tailwind CSS classes in template -->
</style>
```

### Pattern CRUD Standard

```javascript
// 1. Fetch
await store.fetchItems()

// 2. Create
await store.createItem(data)

// 3. Update
await store.updateItem(id, data)

// 4. Delete
await store.deleteItem(id)

// 5. Search
await store.searchItems(filters)

// 6. Statistics
await store.fetchStatistics()
```

### Gestion Erreurs

```javascript
try {
  await action()
} catch (err) {
  error.value = err.message
  // AlertError auto-affichée
}
```

---

## 📱 RESPONSIVE BREAKPOINTS

```
Mobile         < 768px   → 1 colonne
Tablet         768-1024  → 2-3 colonnes
Desktop        > 1024px  → 4 colonnes
```

---

## 🐛 TROUBLESHOOTING

### Frontend ne démarre pas

```bash
npm install
npm run dev
```

### Backend ne répond pas

```bash
cd smart-monitoring-backend/smart-monitoring-backend
mvn spring-boot:run
```

### Erreur CORS

✅ Backend: Vérifier corsConfig.xml  
✅ Frontend: Vérifier axios baseURL

### Modal ne s'ouvre pas

✅ Vérifier `show` prop  
✅ Vérifier click handler  
✅ Vérifier z-index

### Données ne s'affichent pas

✅ Vérifier API response  
✅ Vérifier console errors  
✅ Vérifier store state

### Pagination ne fonctionne pas

✅ Vérifier currentPage state  
✅ Vérifier totalPages  
✅ Vérifier API params

---

## 📚 DOCUMENTATION

- **FRONTEND_IMPLEMENTATION_REPORT.md** - Rapport détaillé
- **IMPLEMENTATION_SUMMARY.md** - Résumé exécutif
- **FILES_CREATED_AND_MODIFIED.md** - Liste fichiers
- **FINAL_VERIFICATION.md** - Vérification finale
- **QUICK_REFERENCE_GUIDE.md** - Ce fichier

---

## 🎓 RESSOURCES

### Code Examples

#### Components réutilisables

```vue
<!-- HealthScoreBar.vue -->
<HealthScoreBar :score="transformateur.healthScore" />

<!-- StatusBadge.vue -->
<StatusBadge :status="transformateur.status" />

<!-- RiskLevelBadge.vue -->
<RiskLevelBadge :riskLevel="transformateur.riskLevel" />
```

#### Pinia Stores

```javascript
import { usePosteStore } from '@/stores/posteStore'
const store = usePosteStore()

await store.fetchPostes()
await store.searchPostes(0)
await store.createPoste(data)
```

---

## 🚀 PROCHAINES ÉTAPES

### Court Terme (1 semaine)

- [ ] UAT avec utilisateurs
- [ ] Seed data 5 postes + 25 transformateurs
- [ ] Performance testing
- [ ] Security audit

### Moyen Terme (1 mois)

- [ ] Module Mesures IoT
- [ ] Module Alertes
- [ ] Export PDF/Excel
- [ ] Caching côté client

### Long Terme (3 mois)

- [ ] Module Interventions
- [ ] Module Réclamations
- [ ] Dashboard analytique
- [ ] Machine Learning

---

## 💡 ASTUCES

### Performance

- Importer seulement ce qui est nécessaire
- Utiliser lazy-loading pour routes
- Memoizer les computeds complexes

### Sécurité

- Toujours vérifier authStore.role
- Valider data avant d'envoyer
- Utiliser Bearer token en headers

### Code Qualité

- Nommer variables clairement
- Ajouter comments pour logic complexe
- Utiliser composition API

### Debug

- Chrome DevTools Vue extension
- Console.log dans setup()
- Inspecter Pinia state

---

## 📞 SUPPORT

### Fichiers à Consulter

```
Components    → src/components/
Vues          → src/views/
Stores        → src/stores/
Routes        → src/router/index.js
Config        → vite.config.js
```

### Conventions

- CamelCase pour variables/functions
- UPPER_CASE pour constants
- kebab-case pour CSS classes
- PascalCase pour components

---

## ✅ CHECKLIST QUOTIDIENNE

- [ ] Frontend build sans erreurs
- [ ] Backend compile sans erreurs
- [ ] Serveurs actifs et accessibles
- [ ] Routes fonctionnent
- [ ] CRUD complet testé
- [ ] Authentification OK
- [ ] Pas d'erreurs console
- [ ] Responsive sur mobile

---

**Quick Reference v1.0**  
Généré: 14 Juillet 2026  
Status: ✅ Production Ready
