# Manuel de Test des 4 Dashboards Harmonisés

**Projet**: STEG Smart Monitoring  
**Date**: 12 Août 2026  
**Objectif**: Tester les 4 dashboards harmonisés avec données réelles

---

## PRÉ-REQUIS

### Serveurs actifs
- **Frontend**: http://localhost:5173 (Vite dev server)
- **Backend**: http://localhost:8081 (Spring Boot + H2)

### Comptes de test
1. **ADMIN**: admin@steg.tn / Admin@123
2. **SUPERVISOR**: supervisor@steg.tn / [password]
3. **AGENT**: agent@steg.tn / [password]
4. **CUSTOMER**: customer@steg.tn / [password]

---

## TÂCHE #11: TEST ADMIN DASHBOARD

### Étape 1: Connexion
1. Ouvrez http://localhost:5173
2. Entrez: `admin@steg.tn` / `Admin@123`
3. Cliquez "Connexion"
4. Vérifiez redirection vers `/admin/dashboard`

### Étape 2: Vérification Header
- [ ] Titre: "Vue globale de la supervision"
- [ ] Sous-titre: "Dashboard administrateur - Supervision globale du système STEG"
- [ ] Bouton "Actualiser" présent et fonctionnel
- [ ] Timestamp "Dernière actualisation" s'affiche après chargement

### Étape 3: Vérification KPI Cards (4 cartes)
- [ ] **Postes**: Affiche nombre réel (ex: 20) + "X actifs"
- [ ] **Transformateurs**: Affiche nombre réel (ex: 149) + "X actifs"
- [ ] **Health Score**: Affiche pourcentage (ex: 65%) avec barre de progression
- [ ] **Alertes Critiques**: Affiche nombre réel (ex: 15) + "X alertes totales"

**IMPORTANT**: Si une carte affiche "0" ou des tirets, NOTE: _______________

### Étape 4: Vérification Section État Global
- [ ] **Graphique Doughnut**: "État global du parc"
  - Affiche 3 segments: Normal (vert), Avertissement (orange), Critique (rouge)
  - Chiffres réels dans la légende
  - [ ] Pas de graphique vide (valeur > 0)

- [ ] **Indicateurs critiques** (4 boîtes):
  - [ ] Transformateurs Critiques (rouge)
  - [ ] Alertes Actives (orange)
  - [ ] Interventions En Cours (bleu)
  - [ ] Réclamations En Attente (violet)
  - Chacun affiche un nombre réel ≥ 0

### Étape 5: Vérification Section Alertes Récentes
- [ ] Titre: "Alertes et anomalies récentes"
- [ ] 4 mini-cartes avec comptages par niveau:
  - [ ] Critiques (nombre réel)
  - [ ] Hautes (nombre réel)
  - [ ] Moyennes (nombre réel)
  - [ ] Infos (nombre réel)
- [ ] Liste d'alertes (min. 3 lignes si données existent)
  - Format: [Couleur] | Titre alerte | Transf | Date | Niveau badge
- [ ] Bouton "Voir toutes les alertes"

### Étape 6: Vérification Section Transformateurs à Surveiller
- [ ] Titre: "Transformateurs à surveiller"
- [ ] Liste des transformateurs critiques (max 5 affichés)
  - Format: Ref | Poste | Health% | Badge "Critique"
- [ ] Si aucun: message "✅ Tous les transformateurs sont en bon état"

### Étape 7: Vérification Section Activité Récente (3 colonnes)
- [ ] **Interventions récentes**:
  - Min. 3 lignes si données existent
  - Ref | Statut badge | Date
  - Bouton "Voir les interventions"
  
- [ ] **Réclamations récentes**:
  - Min. 3 lignes si données existent
  - Description | Date
  - Bouton "Voir les réclamations"
  
- [ ] **Statistiques du réseau**:
  - Mesures (24h): nombre réel
  - Taux de résolution: % avec barre
  - MTTR (heures): nombre réel

### Étape 8: Vérification Graphiques de Distribution
- [ ] **Graphique Bar**: "Interventions par statut"
  - 4 barres: En Attente, En Cours, Terminées, Annulées
  - Hauteurs proportionnelles aux chiffres
  
- [ ] **Graphique Doughnut**: "Alertes par niveau"
  - 4 segments: Critique, Haute, Moyenne, Basse
  - Couleurs distinctes
  - Chiffres visibles en légende

### Étape 9: Test Interactivité
- [ ] Clic sur "Actualiser" → rechargement données, timestamp update
- [ ] Clic sur "Voir toutes les alertes" → navigation `/alerts`
- [ ] Clic sur "Voir les interventions" → navigation `/interventions`
- [ ] Clic sur "Voir les réclamations" → navigation `/complaints`

### Résumé Admin
| Élément | OK? | Valeur observée | Notes |
|---------|-----|-----------------|-------|
| KPI Postes | [ ] | | |
| KPI Transformateurs | [ ] | | |
| KPI Health Score | [ ] | | |
| KPI Alertes Critiques | [ ] | | |
| Graphique État | [ ] | | |
| Indicateurs critiques | [ ] | | |
| Alertes affichées | [ ] | | |
| Interventions affichées | [ ] | | |
| Réclamations affichées | [ ] | | |
| Graphique Bar interventions | [ ] | | |
| Graphique Doughnut alertes | [ ] | | |

---

## TÂCHE #12: TEST SUPERVISOR DASHBOARD

### Étape 1: Déconnexion & Reconnnexion
1. Déconnectez-vous (clic profil → Déconnexion)
2. Email: `supervisor@steg.tn` / [password]
3. Vérifiez redirection vers `/supervisor/dashboard`

### Étape 2: Vérification Header
- [ ] Titre: "Tableau de bord de supervision"
- [ ] Sous-titre: "Vue opérationnelle du système STEG"
- [ ] **IMPORTANT**: PAS de "Bienvenue, [nom]" (infos de connexion supprimées)
- [ ] Bouton "Actualiser" présent

### Étape 3: Vérification KPI Cards (4 cartes)
- [ ] **Postes Supervisés**: nombre réel (ex: 20)
- [ ] **Transformateurs**: nombre réel (ex: 149)
- [ ] **Health Score Moyen**: pourcentage (ex: 65%)
- [ ] **Alertes Actives**: nombre réel

### Étape 4: Vérification Graphiques & Sections
- [ ] Graphique Bar: "Interventions par statut" (4 barres)
- [ ] Graphique Doughnut: "Réclamations par priorité"
- [ ] 3 sections statut:
  - [ ] Interventions (En Attente / En Cours / Terminées)
  - [ ] Réclamations (Ouvertes / Critiques / Résolues)
  - [ ] Alertes (Critiques / Actives / Total)

### Étape 5: Vérification Activité Récente (3 colonnes)
- [ ] Alertes Récentes (min. 3 lignes)
- [ ] Interventions Récentes (min. 3 lignes)
- [ ] Réclamations Récentes (min. 3 lignes)

### Étape 6: Pas de données techniques!
- [ ] ❌ PAS de "ID utilisateur:"
- [ ] ❌ PAS de "Email:"
- [ ] ❌ PAS de "Rôle:"
- [ ] ❌ PAS de date système seule
- [ ] ✅ SEULEMENT données métier

### Résumé Supervisor
| Élément | OK? | Problème? |
|---------|-----|----------|
| Header sans "Bienvenue" | [ ] | |
| KPI 4 cartes | [ ] | |
| Graphiques présents | [ ] | |
| Activité récente | [ ] | |
| Données réelles | [ ] | |

---

## TÂCHE #13: TEST AGENT DASHBOARD

### Étape 1: Déconnexion & Reconnexion
1. Déconnectez
2. Email: `agent@steg.tn` / [password]
3. Vérifiez redirection vers `/agent/dashboard`

### Étape 2: Vérification Header
- [ ] Titre: "Dashboard Agent"
- [ ] Sous-titre: "Vue d'exécution - Interventions et alertes"
- [ ] **IMPORTANT**: PAS de "Bienvenue, [nom]"

### Étape 3: Vérification KPI Cards (4 cartes)
- [ ] **Interventions totales**: nombre réel
- [ ] **En cours**: nombre réel
- [ ] **Complétées**: nombre réel
- [ ] **Alertes actives**: nombre réel

### Étape 4: Vérification Graphiques
- [ ] Graphique Doughnut: "Interventions par statut"
- [ ] Graphique Bar: "Interventions par priorité"

### Étape 5: Vérification Sections
- [ ] "Interventions récentes": min. 3 lignes
- [ ] "Alertes associées": min. 3 lignes
- [ ] Chaque ligne affiche: icône | titre | date | badge statut

### Étape 6: Aucune info technique
- [ ] ❌ PAS de profil utilisateur
- [ ] ✅ Focus exclusif interventions & alertes

### Résumé Agent
| Élément | OK? |
|---------|-----|
| KPI 4 cartes | [ ] |
| Interventions affichées | [ ] |
| Alertes affichées | [ ] |
| Graphiques | [ ] |

---

## TÂCHE #14: TEST CUSTOMER (SERVICE_CLIENT) DASHBOARD

### Étape 1: Déconnexion & Reconnexion
1. Déconnectez
2. Email: `customer@steg.tn` / [password]
3. Vérifiez redirection vers `/serviceclient/dashboard`

### Étape 2: Vérification Header
- [ ] Titre: "Dashboard Service Client"
- [ ] Sous-titre: "Gestion des réclamations et suivi"

### Étape 3: Vérification KPI Cards (4 cartes)
- [ ] **Total Réclamations**: nombre réel
- [ ] **Ouvertes**: nombre réel calculé
- [ ] **Critiques**: nombre réel
- [ ] **Résolues**: nombre réel

### Étape 4: Vérification Graphiques
- [ ] Doughnut: "Réclamations par statut" (6 segments)
- [ ] Bar: "Réclamations par priorité" (4 barres)

### Étape 5: Vérification 3 Sections Status
- [ ] **Statuts**: Nouvelle / En Analyse / En Traitement
- [ ] **Résolution**: Résolues / Fermées / Taux%
- [ ] **Priorités**: Critique / Haute / Moyenne

### Étape 6: Vérification Tableau Réclamations
- [ ] En-têtes: Référence | Description | Priorité | Statut | Créée
- [ ] Min. 5 lignes si données existent
- [ ] Badges de couleur: rouge (critique), orange (haute), etc.

### Résumé Customer
| Élément | OK? |
|---------|-----|
| KPI 4 cartes | [ ] |
| Graphiques | [ ] |
| Sections statut | [ ] |
| Tableau réclamations | [ ] |

---

## VÉRIFICATION FINALE

### Pour TOUS les dashboards:
- [ ] **Aucune erreur console** (F12 → Console)
- [ ] **Aucune erreur 404/500** (F12 → Network)
- [ ] **Responsive** (redimensionner → mobile OK)
- [ ] **Données réelles** (PAS de "0" arbitraires)
- [ ] **Cohérence visuelle** (mêmes couleurs, mêmes styles)
- [ ] **Pas d'infos techniques** (tokens, emails de profil, etc.)

### Tableau Récapitulatif des Données

| Dashboard | KPI 1 | KPI 2 | KPI 3 | KPI 4 | Données OK? |
|-----------|-------|-------|-------|-------|------------|
| ADMIN | Postes | Transf | Health | Alertes | [ ] |
| SUPERVISOR | Postes | Transf | Health | Alertes | [ ] |
| AGENT | Interv | EnCours | Complét | Alertes | [ ] |
| CUSTOMER | Total | Ouvertes | Critiques | Résolues | [ ] |

---

## RAPPORT D'OBSERVATION

### Problèmes rencontrés:
```
[À compléter pendant les tests]
```

### Données non affichées:
```
[À compléter pendant les tests]
```

### Comportement normal observé:
```
[À compléter pendant les tests]
```

---

## CONCLUSION

**TOUS les dashboards sont conformes**: ✅ / ❌

**Signature testeur**: ________________  
**Date test**: ________________

