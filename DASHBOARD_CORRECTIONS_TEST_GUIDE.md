# Guide de Test - Corrections Dashboards Agent & Supervisor

**Projet**: STEG Smart Monitoring  
**Date**: 12 Août 2026  
**Status**: Phase D8-D11 - Tests manuels dans le navigateur

---

## PRÉ-REQUIS

### Serveurs actifs
- **Frontend**: http://localhost:5173 (Vite dev server)
- **Backend**: http://localhost:8081 (Spring Boot + H2)
- **Compilé**: ✅ Exit Code 0 - 1,874 modules

### Comptes de test
| Rôle | Email | Mot de passe | Endpoint |
|------|-------|--------------|----------|
| ADMIN | admin@steg.tn | Admin@123 | /admin/dashboard |
| SUPERVISOR | supervisor@steg.tn | [mot de passe] | /supervisor/dashboard |
| AGENT | agent@steg.tn | [mot de passe] | /agent/dashboard |
| CUSTOMER | customer@steg.tn | [mot de passe] | /serviceclient/dashboard |

---

## PHASE D8: TEST ADMIN DASHBOARD

### Objectif
Vérifier que le dashboard Admin fonctionne correctement et n'a pas été affecté par les corrections.

### Étapes

#### 1. Connexion
1. Ouvrez http://localhost:5173
2. Email: `admin@steg.tn`
3. Mot de passe: `Admin@123`
4. Cliquez "Connexion"
5. Vérifiez redirection vers `/admin/dashboard`

**Résultat**: [ ] ✅ Connexion réussie

#### 2. Vérification du Header
- [ ] Titre: "Vue globale de la supervision"
- [ ] Sous-titre présent
- [ ] Bouton "Actualiser" visible et cliquable
- [ ] Timestamp "Dernière actualisation" s'affiche

**Résultat**: [ ] ✅ Header correct

#### 3. Vérification des KPI Cards (4 cartes)
- [ ] **Postes**: Affiche nombre réel (ex: 20) avec icône 🏢
- [ ] **Transformateurs**: Affiche nombre réel (ex: 149) avec icône ⚡
- [ ] **Health Score**: Affiche pourcentage (ex: 65%) avec icône 📊
- [ ] **Alertes Critiques**: Affiche nombre réel avec icône 🚨

**IMPORTANT**: Vérifiez que les icônes sont affichées correctement (emoji), PAS des noms de texte.

**Résultat**: [ ] ✅ KPI cards correctes

#### 4. Vérification des Graphiques
- [ ] Graphique Doughnut "État global du parc" - 3 segments visibles
- [ ] Graphique Bar "Interventions par statut" - barres proportionnelles
- [ ] Graphique Doughnut "Alertes par niveau" - segments visibles
- [ ] Pas de graphiques vides

**Résultat**: [ ] ✅ Graphiques affichés

#### 5. Vérification des Sections
- [ ] Indicateurs critiques (4 boîtes) affichés
- [ ] "Alertes récentes" visible
- [ ] "Interventions récentes" visible
- [ ] "Réclamations récentes" visible

**Résultat**: [ ] ✅ Sections complètes

#### 6. Test Interactivité
- [ ] Clic "Actualiser" → rechargement, timestamp update
- [ ] Clic sur liens "Voir toutes les..." → navigation fonctionne

**Résultat**: [ ] ✅ Interactivité OK

**Rapport Admin**: [ ] ✅ DASHBOARD FONCTIONNEL

---

## PHASE D9: TEST SUPERVISOR DASHBOARD

### Objectif
Vérifier que le dashboard Supervisor affiche des icônes correctes (emoji, pas de texte) et fonctionne correctement.

### Étapes

#### 1. Déconnexion & Reconnexion
1. Cliquez sur le profil (bas du sidebar) → "Déconnexion"
2. Email: `supervisor@steg.tn`
3. Mot de passe: [votre mot de passe]
4. Cliquez "Connexion"
5. Vérifiez redirection vers `/supervisor/dashboard`

**Résultat**: [ ] ✅ Connexion réussie

#### 2. Vérification du Header
- [ ] Titre: "Tableau de bord de supervision"
- [ ] Sous-titre: "Vue opérationnelle du système STEG"
- [ ] Bouton "Actualiser" présent
- [ ] Timestamp visible

**Résultat**: [ ] ✅ Header correct

#### 3. **CRITIQUE**: Vérification des Icônes dans les KPI Cards

**AVANT (INCORRECT)**:
```
20  building-2
149  zap
76%  activity
0  alert-circle
```

**APRÈS (CORRECT)**:
Vous devez voir des icônes réelles, pas du texte!

- [ ] **Postes**: `20` avec icône **🏢** (bâtiment/office)
- [ ] **Transformateurs**: `149` avec icône **⚡** (éclair/électricité)
- [ ] **Health Score**: `XX%` avec icône **📊** (graphique)
- [ ] **Alertes Actives**: `X` avec icône **🚨** (alerte)

**ATTENTION**: Si vous voyez encore "building-2", "zap", "activity", "alert-circle" affichés comme du texte, c'est une erreur. Cliquez F5 pour recharger la page.

**Résultat**: [ ] ✅ Icônes emoji correctes (PAS de noms texte)

#### 4. Vérification des Données
- [ ] Postes: nombre réel > 0
- [ ] Transformateurs: nombre réel > 0
- [ ] Health Score: pourcentage valide (0-100%)
- [ ] Alertes Actives: nombre réel ≥ 0

**Résultat**: [ ] ✅ Données réelles affichées

#### 5. Vérification des Graphiques
- [ ] Graphique Bar "Interventions par Statut" - barres visibles
- [ ] Graphique Doughnut "Réclamations par Priorité" - segments visibles
- [ ] Les graphiques ont des données, pas vides

**Résultat**: [ ] ✅ Graphiques affichés

#### 6. Vérification des 3 Sections Status
- [ ] **Interventions**: 3 lignes (En Attente, En Cours, Terminées) avec nombres
- [ ] **Réclamations**: 3 lignes (Ouvertes, Critiques, Résolues) avec nombres
- [ ] **Alertes**: 3 lignes (Critiques, Actives, Total) avec nombres

**Résultat**: [ ] ✅ Sections status affichées

#### 7. Vérification Activité Récente (3 colonnes)
- [ ] **Alertes Récentes**: liste visible
- [ ] **Interventions Récentes**: liste visible
- [ ] **Réclamations Récentes**: liste visible

**Résultat**: [ ] ✅ Activité récente affichée

#### 8. Test Actualisation
- [ ] Clic "Actualiser" → rechargement des données, timestamp update

**Résultat**: [ ] ✅ Actualisation fonctionne

**Rapport Supervisor**: [ ] ✅ DASHBOARD REDESIGNÉ ET FONCTIONNEL

---

## PHASE D10: TEST AGENT DASHBOARD

### Objectif
Vérifier que le dashboard Agent se charge maintenant correctement (correction du bug agentId) et affiche des données réelles.

### Étapes

#### 1. Déconnexion & Reconnexion
1. Cliquez profil → "Déconnexion"
2. Email: `agent@steg.tn`
3. Mot de passe: [votre mot de passe]
4. Cliquez "Connexion"
5. Vérifiez redirection vers `/agent/dashboard`

**Résultat**: [ ] ✅ Connexion réussie

#### 2. **CRITIQUE**: Vérification du Chargement

**AVANT (ERREUR)**:
```
Dashboard Agent
Vue d'exécution - Interventions et alertes
---
Impossible de charger le dashboard
Impossible de charger les données du dashboard.
Veuillez réessayer.
```

**APRÈS (CORRECT)**:
Le dashboard doit se charger sans erreur et afficher des données.

- [ ] **PAS d'erreur "Impossible de charger le dashboard"**
- [ ] Dashboard charge correctement
- [ ] Données réelles affichées (pas de message d'erreur)

**Résultat**: [ ] ✅ Dashboard charge SANS ERREUR

#### 3. Vérification du Header
- [ ] Titre: "Dashboard Agent"
- [ ] Sous-titre: "Vue d'exécution - Interventions et alertes"
- [ ] Bouton "Actualiser" présent
- [ ] Timestamp visible

**Résultat**: [ ] ✅ Header correct

#### 4. Vérification des 4 KPI Cards
- [ ] **Interventions totales**: nombre réel avec icône 🔧
- [ ] **En cours**: nombre réel avec icône ⏳
- [ ] **Complétées**: nombre réel avec icône ✅
- [ ] **Alertes actives**: nombre réel avec icône 🚨

**Résultat**: [ ] ✅ KPI cards affichées

#### 5. Vérification des Graphiques
- [ ] Graphique Doughnut "Interventions par statut" - 4 segments
- [ ] Graphique Bar "Interventions par priorité" - 4 barres

**Résultat**: [ ] ✅ Graphiques affichés

#### 6. Vérification des Sections
- [ ] **Interventions récentes**: liste visible avec min. 3 lignes (si données existent)
- [ ] **Alertes associées**: liste visible

**Résultat**: [ ] ✅ Sections affichées

#### 7. **IMPORTANT**: Vérifier l'accès à la page Réclamations
1. Regardez le sidebar gauche
2. Cherchez "Réclamations" dans la section "Opérations"

- [ ] Entrée "Réclamations" **visible** dans le sidebar
- [ ] Cliquez sur "Réclamations"
- [ ] Navigation vers `/complaints` fonctionne
- [ ] Page Réclamations s'affiche correctement

**Résultat**: [ ] ✅ Accès Réclamations fonctionne

#### 8. Vérification Permissions Réclamations
1. Vérifiez que l'Agent peut consulter les réclamations qui lui sont accessibles
2. Les réclamations affichées doivent être cohérentes avec les permissions RBAC

- [ ] Page Réclamations affiche des données
- [ ] Pas d'erreur 403 "Accès refusé"

**Résultat**: [ ] ✅ Permissions RBAC correctes

#### 9. Test Actualisation
- [ ] Clic "Actualiser" → rechargement, timestamp update

**Résultat**: [ ] ✅ Actualisation fonctionne

**Rapport Agent**: [ ] ✅ DASHBOARD FONCTIONNEL - BUG CORRIGÉ - ACCÈS RÉCLAMATIONS ACTIF

---

## PHASE D11: TEST CUSTOMER DASHBOARD

### Objectif
Vérifier que le dashboard Service Client n'a pas été affecté par les corrections et fonctionne toujours.

### Étapes

#### 1. Déconnexion & Reconnexion
1. Cliquez profil → "Déconnexion"
2. Email: `customer@steg.tn`
3. Mot de passe: [votre mot de passe]
4. Cliquez "Connexion"
5. Vérifiez redirection vers `/serviceclient/dashboard`

**Résultat**: [ ] ✅ Connexion réussie

#### 2. Vérification générale
- [ ] Dashboard charge sans erreur
- [ ] Header visible
- [ ] KPI cards affichées
- [ ] Graphiques visibles
- [ ] Données réelles affichées

**Résultat**: [ ] ✅ Dashboard intact

#### 3. Vérification des Accès
- [ ] Sidebar: uniquement "Réclamations" visible (pas d'Interventions, Mesures, etc.)
- [ ] Si l'Agent essayait d'accéder à des pages non autorisées, il serait redirigé

**Résultat**: [ ] ✅ Permissions correctes

**Rapport Customer**: [ ] ✅ DASHBOARD INCHANGÉ - FONCTIONNEL

---

## RÉSUMÉ DES TESTS

| Phase | Dashboard | Status | Icônes OK? | Données OK? | Réclamations? |
|-------|-----------|--------|-----------|------------|---------------|
| D8 | ADMIN | [ ] ✅ | N/A | [ ] | N/A |
| D9 | SUPERVISOR | [ ] ✅ | [ ] | [ ] | N/A |
| D10 | AGENT | [ ] ✅ | [ ] | [ ] | [ ] |
| D11 | CUSTOMER | [ ] ✅ | N/A | [ ] | N/A |

---

## CONSOLE NAVIGATEUR

Pour chaque dashboard, ouvrez la console navigateur (F12 → Console) et vérifiez:

- [ ] **Pas d'erreurs rouges** concernant les dashboards
- [ ] Les appels API vers `/dashboard/admin`, `/dashboard/supervisor`, `/dashboard/agent/{id}`, `/dashboard/service-client` réussissent (200 OK)
- [ ] Pas d'erreur 401 (authentification)
- [ ] Pas d'erreur 403 (permission)
- [ ] Pas d'erreur 404 (endpoint non trouvé)

---

## CONCLUSION

Quand tous les tests sont complétés:

1. Remplissez le résumé ci-dessus
2. Notez les problèmes rencontrés
3. Confirmez que:
   - ✅ Dashboard Agent charge sans erreur (bug corrigé)
   - ✅ Dashboard Supervisor affiche des icônes emoji (pas de texte)
   - ✅ Agent peut accéder à la page Réclamations
   - ✅ Tous les dashboards ont des données réelles
   - ✅ Aucun dashboard n'a été cassé par les corrections

**TESTS RÉUSSIS**: [ ] ✅ TOUS LES TESTS PASSENT

