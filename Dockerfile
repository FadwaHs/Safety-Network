# Utiliser une image Node.js pour les services Node.js
FROM node:14

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers package.json et package-lock.json pour installer les dépendances
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste des fichiers de l'application
COPY . .

# Exposer le port nécessaire par le service 
EXPOSE 3000
EXPOSE 3001
EXPOSE 3002

# Commande par défaut pour démarrer le service (à ajuster en fonction du service)
CMD [ "npm", "start" ]
