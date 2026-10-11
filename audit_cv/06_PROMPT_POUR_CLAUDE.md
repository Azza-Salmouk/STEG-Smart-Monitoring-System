# PROMPT POUR CLAUDE - Rédaction Expérience de Stage

Ce document contient un prompt complet et autonome à copier directement dans Claude (chat.openai.com/Anthropic) pour rédiger une expérience de stage professionnelle basée sur l'audit du projet STEG Smart Monitoring System.

---

## INSTRUCTIONS COMPLÈTES POUR L'UTILISATEUR

1. **Ouvrez Claude** : https://claude.ai
2. **Créez une nouvelle conversation**
3. **Copiez INTÉGRALEMENT le prompt ci-dessous** (depuis "---BEGIN PROMPT---" jusqu'à "---END PROMPT---")
4. **Collez-le dans Claude**
5. **Attachez les fichiers d'audit** (optionnel mais recommandé) :
   - `01_resume_projet.md`
   - `02_audit_technique.md`
   - `03_realisations_cv.md`
   - `04_competences_techniques.md`
6. **Appuyez sur Entrée et laissez Claude générer la rédaction**
7. **Remplissez les champs [PLACEHOLDER]** avant d'intégrer dans votre CV

---

## ---BEGIN PROMPT---

### CONTEXTE

Je prépare une rubrique "Expérience de stage" pour mon CV basé sur un projet logiciel réel appelé **STEG Smart Monitoring System**. J'ai commandé un audit technique approfondi du projet qui a généré 4 documents détaillés (01_resume_projet.md, 02_audit_technique.md, 03_realisations_cv.md, 04_competences_techniques.md). Je vais maintenant te fournir cet audit et je te demande de l'analyser pour rédiger une expérience de stage professionnelle et convaincante.

### DOCUMENTS AUDIT À ANALYSER

**Les documents suivants contiennent l'analyse technique complète du projet :**

1. **01_resume_projet.md** : Présentation générale, stack technique, modules métier
2. **02_audit_technique.md** : Architecture détaillée, modules, technologies, sécurité, tableaux de bord, IA
3. **03_realisations_cv.md** : Réalisations techniques vérifiées avec preuves dans le code
4. **04_competences_techniques.md** : Compétences avec niveau de preuve

**Important** : Ces documents contiennent uniquement des faits vérifiés dans le code source réel. N'invente aucune information au-delà de ce qui est documenté dans ces fichiers.

### MON PROFIL ET CONTEXTE (À COMPLÉTER)

Je dois compléter les informations suivantes moi-même (voir fichier 05_informations_a_confirmer.md si besoin) :

- **Entreprise** : [À REMPLIR - Nom de l'organisation/STEG Tunisia/autre]
- **Dates du stage** : du [À REMPLIR] au [À REMPLIR]
- **Durée** : [À REMPLIR - 2 mois / 3 mois / autre]
- **Titre du poste** : [À REMPLIR - Stagiaire Développeur Full-Stack / autre]
- **Équipe** : [À REMPLIR - Seul développeur / équipe de X personnes / autre]
- **Mes responsabilités spécifiques** : [À REMPLIR - modules développés personnellement]
- **Degré d'autonomie** : [À REMPLIR - complète / supervision hebdo / daily standups]
- **Défis majeurs** : [À REMPLIR - principaux obstacles surmontés]
- **Résultats mesurables** : [À REMPLIR - endpoints implémentés / bugs résolus / utilisateurs / autre]
- **Déploiement** : [À REMPLIR - production / staging / POC / académique]

### TÂCHES À EFFECTUER

#### Tâche 1 : ANALYSE DE L'AUDIT

Analyse les documents d'audit pour :
- Identifier les technologies réelles utilisées (confirmées dans le code)
- Reconnaître les réalisations techniques majeures
- Distinguer ce qui a été vraiment implémenté vs documenté
- Noter les points forts de l'architecture
- Repérer les compétences démontrées

#### Tâche 2 : RÉDACTION VERSION COURTE (3-5 puces)

Rédige une description concise et punchy du stage, format LinkedIn/CV actuel :

- ~3-5 puces maximales
- Chaque puce = 1-2 lignes max
- Verbes d'action : conçu, développé, implémenté, sécurisé, intégré, optimisé
- Technos pertinentes mentionnées
- Impacts/résultats chiffrés si applicable
- Tone : professionnel, sans hyperbole

**Format demandé :**
```
**Stagiaire [Titre du stage] - [Entreprise] ([Dates])**

• [Réalisation 1 avec technos et impact]
• [Réalisation 2 avec technos et impact]
• [Réalisation 3 avec technos et impact]
• [Réalisation 4 si pertinent]
```

#### Tâche 3 : RÉDACTION VERSION TECHNIQUE (développeur full-stack)

Rédige une version plus détaillée (200-300 mots) pour un CV technique ciblant des postes full-stack :

- Niveau : Ingénieur débutant / développeur junior
- Audience : Recruteurs tech, développeurs seniors
- Structure :
  1. Contexte du projet (1-2 phrases)
  2. Réalisations frontend (Vue.js 3, Pinia, Axios, responsif)
  3. Réalisations backend (Spring Boot, API REST, JWT, RBAC)
  4. Sécurité et architecture (JPA, design patterns, gestion d'erreurs)
  5. Intégration IA (Ollama, prompt engineering, RBAC)
  6. Résultats et impact
- Mentionne les nombres réels du projet (70+ endpoints, 8 entités, 4 rôles RBAC, Qwen2.5:3b)
- Pas de mensonges : inclure uniquement les technologies réellement implémentées

**Format demandé :**
```
**Stagiaire [Titre] - [Entreprise] - [Dates]**

Contexte : ...

Responsabilités et réalisations :

Frontend : ...

Backend : ...

Sécurité et architecture : ...

Intelligence Artificielle : ...

Résultats : ...
```

#### Tâche 4 : RÉDACTION VERSION ANGLAISE (optionnel)

Si demandé, traduis la version courte en anglais (format concis, même structure) :

**Format demandé :**
```
**Internship [Title] - [Company] - [Dates]**

• [Achievement 1]
• [Achievement 2]
...
```

#### Tâche 5 : SÉLECTION TECHNOLOGIES POUR SECTION COMPÉTENCES

Fournis une liste d'environ 8-12 technologies du projet à mettre en avant dans la section "Compétences" ou "Technologies" du CV :

- **Classées par importance** (du plus fort au moins fort)
- **Justification brève** pour chaque (nombre d'endpoints, composants créés, etc.)
- **Éviter les compétences hypothétiques** (inclure uniquement les technologies confirmées dans le code)

**Format demandé :**
```
Compétences clés du projet (par ordre d'importance)

1. Vue.js 3 - [19 vues, 30+ composants, Pinia state management]
2. Spring Boot 3 - [70+ endpoints REST, architecture 3 couches]
3. ...
```

#### Tâche 6 : VÉRIFICATION DE CONFORMITÉ ATS

Optimise le texte pour la lisibilité des systèmes ATS (Applicant Tracking Systems) :

- Utilise des mots-clés standards (Vue.js, Spring Boot, REST API, JWT, RBAC, etc.)
- Évite les acronymes exotiques ou les jargon trop spécialisé
- Format sans table, colonnes ou éléments graphiques complexes
- Structure simple : titres clairs, puces, pas de mise en forme exotique
- Longueur optimale : 150-300 mots par expérience

### CRITÈRES DE QUALITÉ

Avant de soumettre ta réponse, assure-toi que :

✅ **Aucune information inventée** : Utilise UNIQUEMENT ce qui est dans l'audit
✅ **Verbes d'action réels** : "Conçu", "implémenté", "développé" (verifiable)
✅ **Pas d'exagération** : 0% tests n'est pas mentionné comme force, mais les 70+ endpoints et la sécurité oui
✅ **Technos précises** : Vue.js 3.3.4, Spring Boot 3.5.16, etc. (pas "Vue" ou "Spring")
✅ **Chiffres exacts** : 4 rôles RBAC, 8 entités, 150+ endpoints, 7 stores Pinia
✅ **Pas de métriques inventées** : (ex: évite "30% gain de performance" sans preuve)
✅ **Tone professionnel** : Pas de superlatives ("meilleur", "amazing"), factuel et humble
✅ **Format ATS-compatible** : Lisible par des bots

### PLACEMENTS DES CHAMPS À COMPLÉTER

Laisse les champs [À REMPLIR] dans le texte de sorte que l'utilisateur puisse les compléter facilement :

- [À REMPLIR - Titre du poste]
- [À REMPLIR - Date de début]
- [À REMPLIR - Date de fin]
- [À REMPLIR - Entreprise]

### ORDRE DE LIVRAISON

Livre ta réponse dans cet ordre :

1. **Analyse rapide** des documents audit (2-3 phrases)
2. **Version courte (3-5 puces)** - texte formaté prêt à copier-coller
3. **Version technique (200-300 mots)** - texte formaté prêt à copier-coller
4. **Version anglaise** (si demandé) - idem
5. **Liste technologies pour section Compétences** - avec justifications
6. **Recommandations ATS** - ajustements proposés
7. **Notes finales** - conseils pour intégration au CV final

### DOCUMENTS D'AUDIT SONT ATTACHÉS CI-DESSOUS

[Les documents seront attachés lors du prompt à Claude]

---

**Commence maintenant. Analyse les documents audit et procède à chacune des 6 tâches.**

---

## ---END PROMPT---

---

## APRÈS LA GÉNÉRATION PAR CLAUDE

### Étape 1 : Remplir les [À REMPLIR]

Une fois que Claude a généré le texte, remplace tous les champs `[À REMPLIR - ...]` par tes informations réelles :

```
# AVANT (généré par Claude)
Stagiaire [À REMPLIR - Titre du poste] - [À REMPLIR - Entreprise] ([À REMPLIR - Date début] à [À REMPLIR - Date fin])

# APRÈS (complété par toi)
Stagiaire Développeur Full-Stack - STEG Tunisia (juin 2025 à août 2025)
```

### Étape 2 : Adapter au ton et format de ton CV

- Ajuste la longueur si ton CV a une limite stricte
- Harmonise le style avec les autres expériences
- Utilise les mêmes verbes d'action que dans tes autres rubriques

### Étape 3 : Vérifier la cohérence

- Assure-toi que les technologies mentionnées correspondent à celles dans ta section "Compétences"
- Vérifie qu'aucune technologie n'est exagérée ou fictive
- Valide les dates et durations

### Étape 4 : Tester lisibilité ATS

- Copie-colle le texte final sur un simulateur ATS (ex : https://www.jobscan.co/)
- Score minimum recommandé : 70/100
- Ajuste les mots-clés si score bas

### Étape 5 : Faire relire

- Demande à un camarade ou mentor de relire pour :
  - Clarté et concision
  - Grammaire/orthographe
  - Pertinence pour le CV
  - Pas d'exagération

---

## EXEMPLE DE RÉSULTAT FINAL

```
**Stagiaire Développeur Full-Stack - STEG Tunisia (juin 2025 à août 2025)**

• Conçu et développé une application web complète (Vue.js 3 + Spring Boot 3) de supervision électrique avec 70+ endpoints REST, répartis sur 8 modules métier (postes, transformateurs, mesures, alertes, interventions, réclamations).

• Implémenté une architecture sécurisée avec authentification JWT (HS256), hachage BCrypt et contrôle d'accès par 4 rôles distincts (RBAC), protégeant tous les endpoints sensibles.

• Structuré l'état frontend avec Pinia (7 stores) et Axios pour 150+ appels API, mettant en place un service centralisé avec intercepteurs automatiques et gestion d'erreurs uniforme.

• Intégré un assistant conversationnel basé sur Ollama (modèle Qwen2.5:3b), construisant dynamiquement le contexte selon le rôle utilisateur pour fournir des insights intelligents sécurisés.

• Conçu 4 tableaux de bord personnalisés par rôle avec Chart.js pour la visualisation de données, tableaux paginés et statistiques d'agrégation réelles.
```

---

## QUESTIONS FRÉQUENTES

### Q : Dois-je mentionner que 0% des tests sont implémentés ?
**R :** Non recommandé pour un CV (points faibles à éviter). En entretien, sois honnête : "Le projet prioritize la fonctionnalité sur les tests, but j'aurais aimé en ajouter."

### Q : Puis-je exagérer ma contribution (ex : dire que j'ai tout fait seul) ?
**R :** Non. C'est contre-productif et détectable en entretien. Sois honnête sur ton rôle et merveille les recruteurs par la profondeur technique réelle.

### Q : Debo-je mentionner que le projet est académique / un POC ?
**R :** Pas d'importance. Les deux sont valorisables. Un POC démontre l'innovation, un projet académique démontre la maîtrise. La clé est la solidité technique.

### Q : Comment mettre en avant l'IA si c'est "juste" une intégration d'Ollama ?
**R :** Sois précis : "Intégré un modèle de langage existant (Ollama Qwen2.5:3b) avec contexte RBAC dynamique" = c'est déjà solide. Pas besoin de mentir ("entraîné un modèle custom").

### Q : Faut-il mentionner les librairies mineures (Lucide, Lombok) ?
**R :** Non. Focus sur les principales (Vue, Spring, JWT, Chart.js). Les mineures diluent le message.

### Q : Mon CV a déjà 2 pages, la version courte ou longue ?
**R :** **Courte (3-5 puces)**. Un CV doit rester à 1-2 pages. Si besoin, expand un peu mais garde la concision.

---

## PROCHAINES ÉTAPES

1. **Remplis les informations manquantes** (05_informations_a_confirmer.md)
2. **Copie le prompt dans Claude**
3. **Attache les 4 documents d'audit**
4. **Récupère les textes générés**
5. **Personnalise avec tes informations**
6. **Intègre au CV final**

---

## Support

Si Claude demande des clarifications :
- Fournis les détails manquants (entreprise, dates, équipe)
- Repose ta question avec plus de contexte
- Rappelle-lui que seules les informations d'audit sont fiables

Bonne chance ! 🚀

