const express = require('express');
const pgp = require('pg-promise')();
const app = express();
const port = 3000;
const cors = require('cors');

const dbConfig = {
  host: 'db',
  port: '15432',
  database: 'locations',
  user: 'ilisi',
  password: 'ilisi',
};


const db = pgp(dbConfig);

app.use(express.json());
app.use(cors());

app.post('/postcitizenlocation', async (req, res) => {
  try {
    const { user_id, latitude, longitude, latitudeDelta, longitudeDelta, type_user } = req.body;

    const insertQuery = `
  INSERT INTO LocationAtTime (user_id, location, latitudeDelta, longitudeDelta, type_user)
  VALUES ($1, ST_SetSRID(ST_MakePoint($2, $3), 4326), $4, $5, $6)
  RETURNING locationidentifier;
`;

    const result = await db.one(insertQuery, [user_id, latitude, longitude, latitudeDelta, longitudeDelta, type_user]);

    res.status(200).json({ message: 'Données insérées avec succès', locationidentifier: result.locationidentifier });
  } catch (error) {
    console.error('Erreur lors de l\'insertion de la personne', error);
    res.status(500).json({ error: 'Erreur lors de l\'insertion de la personne' });
  }
});

// Route pour obtenir la dernière emplacement du jour pour un utilisateur spécifique
app.get('/getlatestcitizenlocation/:user_id', async (req, res) => {
  try {
    const { user_id } = req.params;

    const selectQuery = `
      SELECT user_id, ST_X(location) as latitude, ST_Y(location) as longitude, latitudeDelta, longitudeDelta, date, time
      FROM LocationAtTime
      WHERE user_id = $1
      AND date = current_date
      ORDER BY date DESC, time DESC
      LIMIT 1
    `;
    const data = await db.oneOrNone(selectQuery, [user_id]);

    if (data) { 
      res.status(200).json(data);
    } else {
      res.status(404).json({ error: 'Aucun emplacement trouvée pour cet utilisateur aujourd\'hui' });
    }
  } catch (error) {
    console.error('Erreur lors de la récupération de la dernière emplacement', error);
    res.status(500).json({ error: 'Erreur lors de la récupération de la dernière emplacement' });
  }
});

//Route pour obtenir les plus proches helpers
app.get('/getnearestcitizens/:latitude/:longitude/:type_user', async (req, res) => {
  try {
    const { latitude, longitude, type_user } = req.params;

    const selectQuery = `
      SELECT ST_X(location) as latitude, ST_Y(location) as longitude, type_user, user_id
      FROM LocationAtTime
      WHERE type_user = $1
      ORDER BY ST_Distance(ST_MakePoint($2, $3), location) ASC
      LIMIT 5
    `;
    const data = await db.manyOrNone(selectQuery, [type_user, latitude, longitude]);

    if (data.length > 0) {
      res.status(200).json(data);
    } else {
      res.status(404).json({ error: 'Aucun utilisateur trouvé avec le même type_user' });
    }
  } catch (error) {
    console.error('Erreur lors de la récupération des utilisateurs les plus proches', error);
    res.status(500).json({ error: 'Erreur lors de la récupération des utilisateurs les plus proches' });
  }
});

// Route pour obtenir toutes les emplacements d'un utilisateur à une date spécifique
app.get('/getallcitizenlocations/:user_id/:date', async (req, res) => {
  try {
    const { user_id, date } = req.params;

    // Convertir la date au format "jourmoisannee" en date PostgreSQL
    const formattedDate = `${date.substring(4, 8)}-${date.substring(2, 4)}-${date.substring(0, 2)}`;

    const selectQuery = `
      SELECT user_id, ST_X(location) as latitude, ST_Y(location) as longitude, latitudeDelta, longitudeDelta, date, time
      FROM LocationAtTime
      WHERE user_id = $1
      AND date = $2
    `;
    const data = await db.manyOrNone(selectQuery, [user_id, formattedDate]);

    if (data.length > 0) {
      res.status(200).json(data);
    } else {
      res.status(404).json({ error: 'Aucune emplacement trouvé pour cet utilisateur à la date spécifiée' });
    }
  } catch (error) {
    console.error('Erreur lors de la récupération des emplacements à la date spécifiée', error);
    res.status(500).json({ error: 'Erreur lors de la récupération des emplacements à la date spécifiée' });
  }
});

//Cette fonction n'est pas sensée intervenir, on ne met pas à jour une location, on rajoute
//Cette fonction met pas à jour toutes les locations de la journée en une seule location
//Je la laisse avant de mieux réfléchir, mais je la suprimerais probablement plus tard 
app.put('/updatecitizenlocation/:user_id', async (req, res) => {
  try {
    const { user_id } = req.params;
    const { latitude, longitude, latitudeDelta, longitudeDelta } = req.body;

    const updateQuery = `
      UPDATE LocationAtTime
      SET location = ST_MakePoint($2, $3), latitudeDelta = $4, longitudeDelta = $5
      WHERE user_id = $1
    `;
    await db.none(updateQuery, [user_id, latitude, longitude, latitudeDelta, longitudeDelta]);

    res.status(200).json({ message: 'Données mises à jour avec succès' });
  } catch (error) {
    console.error('Erreur lors de la mise à jour de la personne', error);
    res.status(500).json({ error: 'Erreur lors de la mise à jour de la personne' });
  }
});

//Supprime toute les locations de quelqu'un, un trigger peut se déclencher 
//chaque deux jours sur les locations vieilles de trois jours et les supprimer
app.delete('/deletecitizenlocation/:user_id', async (req, res) => {
  try {
    const { user_id } = req.params;

    const deleteQuery = `
      DELETE FROM LocationAtTime
      WHERE user_id = $1
    `;
    await db.none(deleteQuery, [user_id]);

    res.status(200).json({ message: 'Données supprimées avec succès' });
  } catch (error) {
    console.error('Erreur lors de la suppression des données', error);
    res.status(500).json({ error: 'Erreur lors de la suppression des données' });
  }
});

app.listen(port, () => {
  console.log(`Serveur Express en cours d'exécution sur le port ${port}`);
});
