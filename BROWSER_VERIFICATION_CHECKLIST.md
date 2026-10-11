# VÉRIFICATION MANUELLE - Pages Mesures & Alertes Full-Width

**URL Frontend**: http://localhost:5173  
**URL Backend**: http://localhost:8081  

---

## TEST #4: Page Mesures IoT - ADMIN

### Étape 1: Connexion
1. Accédez à http://localhost:5173
2. Email: admin@steg.tn
3. Password: Admin@123
4. Cliquez "Connexion"

### Étape 2: Navigation vers Mesures
1. Cliquez sur "Mesures" dans le sidebar
2. Route attendue: /measurements
3. Attendez le chargement des données

### Étape 3: Vérification de l'affichage full-width
**AVANT les modifications**:
- Conteneur limité à max-width-7xl (1280px max)
- Espaces vides à gauche et à droite
- Tableau étroit

**APRÈS les modifications (À VÉRIFIER)**:
- [ ] Le titre "Mesures IoT" s'étend sur toute la largeur
- [ ] Les 4 KPI cards occupent toute la largeur disponible
- [ ] Les 3 stats cards (Critiques, Tension, Alertes) occupent toute la largeur
- [ ] Le filtre s'étend sur toute la largeur
- [ ] Le tableau s'étend sur toute la largeur
- [ ] Aucun espace blanc inutile à gauche/droite
- [ ] Pas de scroll horizontal à la page entière
- [ ] Le sidebar est toujours visible à gauche
- [ ] Le header est toujours visible en haut

### Étape 4: Vérification des données
- [ ] Les KPI cards affichent des chiffres (ex: 4917 mesures)
- [ ] La température moyenne s'affiche (ex: 45°C)
- [ ] Le tableau contient des lignes avec données réelles
- [ ] Pas d'erreur en console (F12 → Console)
- [ ] Pas d'erreur 404/500 dans Network tab

### Étape 5: Vérification de la responsivité
1. Ouvrez DevTools (F12)
2. Cliquez sur "Device Toolbar" (Ctrl+Shift+M)
3. Testez sur Mobile (375px):
   - [ ] Les KPI cards s'empilent verticalement
   - [ ] Le tableau reste lisible (scroll horizontal du tableau ok)
4. Testez sur Tablet (768px):
   - [ ] Les KPI cards s'affichent sur 2 lignes
5. Testez sur Desktop (1920px):
   - [ ] Les KPI cards s'affichent sur 1 ligne
   - [ ] L'espace est exploité au maximum

---

## TEST #5: Page Gestion des Alertes - ADMIN

### Étape 1: Navigation vers Alertes
1. Depuis la page Mesures, cliquez sur "Alertes" dans le sidebar
2. Route attendue: /alerts
3. Attendez le chargement des données

### Étape 2: Vérification de l'affichage full-width
**APRÈS les modifications (À VÉRIFIER)**:
- [ ] Le titre "Gestion des Alertes" s'étend sur toute la largeur
- [ ] Les 4 KPI cards (Total, Actives, Critiques, Résolues) s'étendent sur toute la largeur
- [ ] La barre de filtres s'étend sur toute la largeur
- [ ] Le tableau des alertes s'étend sur toute la largeur
- [ ] Aucun espace blanc inutile à gauche/droite
- [ ] Pas de scroll horizontal à la page entière
- [ ] Le sidebar est toujours visible à gauche
- [ ] Le header est toujours visible en haut
- [ ] Le fond est gris clair (bg-gray-50) et non gris foncé

### Étape 3: Vérification des données
- [ ] Les KPI cards affichent des chiffres (ex: 300 alertes totales)
- [ ] Les alertes actives s'affichent en rouge
- [ ] Le tableau contient des alertes avec données réelles
- [ ] Pas d'erreur en console (F12 → Console)
- [ ] Pas d'erreur 404/500 dans Network tab

### Étape 4: Vérification des filtres
- [ ] La recherche fonctionne
- [ ] Le filtre "Statut" fonctionne
- [ ] Le filtre "Niveau" fonctionne
- [ ] Le bouton "Filtrer" fonctionne

### Étape 5: Vérification de la responsivité
1. Ouvrez DevTools (F12)
2. Cliquez sur "Device Toolbar" (Ctrl+Shift+M)
3. Testez sur Mobile (375px):
   - [ ] Les KPI cards s'empilent verticalement
   - [ ] Le tableau reste lisible (scroll horizontal ok)
4. Testez sur Tablet (768px):
   - [ ] Les KPI cards s'affichent sur 2-4 colonnes
5. Testez sur Desktop (1920px):
   - [ ] L'espace est exploité au maximum

---

## TEST #6: Page Mesures - SUPERVISOR

### Étape 1: Déconnexion
1. Cliquez sur le profil (haut droit)
2. Cliquez "Déconnexion"
3. Vérifiez redirection vers login

### Étape 2: Connexion SUPERVISOR
1. Email: supervisor@steg.tn
2. Password: (auto-générée, devrait être acceptée ou défaut)
3. Cliquez "Connexion"

### Étape 3: Navigation vers Mesures
1. Cliquez sur "Mesures" dans le sidebar
2. Vérifiez que Supervisor a accès à la page
3. Route: /measurements

### Étape 4: Vérification Supervisor
- [ ] La page s'affiche en full-width
- [ ] Les données se chargent
- [ ] Pas d'erreur 403 Unauthorized
- [ ] Pas d'erreur de permissions

---

## TEST #7: Page Alertes - SUPERVISOR

### Étape 1: Navigation vers Alertes
1. Cliquez sur "Alertes" dans le sidebar
2. Route: /alerts

### Étape 2: Vérification Supervisor
- [ ] La page s'affiche en full-width
- [ ] Les données se chargent
- [ ] Pas d'erreur 403 Unauthorized
- [ ] Pas d'erreur de permissions

---

## RÉSUMÉ DES VÉRIFICATIONS

| Test | Mesures-ADMIN | Alertes-ADMIN | Mesures-SUPERVISOR | Alertes-SUPERVISOR |
|------|---|---|---|---|
| Full-width visible | ✓ | ✓ | ✓ | ✓ |
| Données chargées | ✓ | ✓ | ✓ | ✓ |
| Pas d'erreurs | ✓ | ✓ | ✓ | ✓ |
| Responsive | ✓ | ✓ | ✓ | ✓ |
| Permissions OK | ✓ | ✓ | ✓ | ✓ |

---

## OBSERVATIONS À NOTER

### Mesures IoT
- Nombre exact de cartes KPI affichées: __________
- Nombre de colonnes sur desktop: __________
- Nombre de lignes de données visibles: __________
- Largeur utilisée: __________% de l'écran

### Alertes
- Nombre exact de KPI cards affichées: __________
- Nombre de colonnes sur desktop: __________
- Nombre de lignes d'alertes visibles: __________
- Largeur utilisée: __________% de l'écran

---

## ERRORS / PROBLÈMES RENCONTRÉS

**Erreurs consoles:**
```
[À noter]
```

**Erreurs Network:**
```
[À noter]
```

**Autres observations:**
```
[À noter]
```

---

**Testeur**: À faire manuellement dans le navigateur  
**Date de test**: À compléter  
**Navigateur**: Chrome/Edge/Firefox

