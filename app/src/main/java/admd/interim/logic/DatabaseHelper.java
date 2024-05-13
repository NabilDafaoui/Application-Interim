package admd.interim.logic;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "interim.db";
    private static final int DATABASE_VERSION = 1;

    // Définition des noms de tables et colonnes
    private static final String TABLE_CANDIDATS = "candidats";
    private static final String TABLE_EMPLOYEURS = "employeurs";
    private static final String TABLE_OFFRES = "offres";
    private static final String TABLE_CANDIDATURES = "candidatures";
    private static final String COLUMN_ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCandidatsTable = "CREATE TABLE " + TABLE_CANDIDATS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT," +
                "prenom TEXT," +
                "date_naissance TEXT," +
                "nationalite TEXT," +
                "numero_telephone TEXT," +
                "email TEXT," +
                "ville TEXT," +
                "cv TEXT" +
                ")";
        db.execSQL(createCandidatsTable);

        String createEmployeursTable = "CREATE TABLE " + TABLE_EMPLOYEURS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT," +
                "entreprise TEXT," +
                "numero_telephone TEXT," +
                "adresse TEXT," +
                "liens_public TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT" +
                ")";
        db.execSQL(createEmployeursTable);

        String createOffresTable = "CREATE TABLE " + TABLE_OFFRES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titre TEXT," +
                "description TEXT," +
                "metier TEXT," +
                "lieu TEXT," +
                "date_debut TEXT," +
                "date_fin TEXT," +
                "id_employeur INTEGER," +
                "FOREIGN KEY (id_employeur) REFERENCES " + TABLE_EMPLOYEURS + "(id)" +
                ")";
        db.execSQL(createOffresTable);

        String createCandidaturesTable = "CREATE TABLE " + TABLE_CANDIDATURES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_offre INTEGER," +
                "id_candidat INTEGER," +
                "nom_candidat TEXT," +
                "prenom_candidat TEXT," +
                "email_candidat TEXT," +
                "cv_candidat TEXT," +
                "date_candidature TEXT," +
                "statut_candidature TEXT," +
                "FOREIGN KEY (id_offre) REFERENCES " + TABLE_OFFRES + "(id)," +
                "FOREIGN KEY (id_candidat) REFERENCES " + TABLE_CANDIDATS + "(id)" +
                ")";
        db.execSQL(createCandidaturesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATURES);
        onCreate(db);
    }

    private boolean employeurExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM employeurs WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }

    @SuppressLint("Range")
    public Employeur getEmployeurById(long idEmployeur) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EMPLOYEURS + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(idEmployeur) });

        Employeur employeur = null;
        if (cursor.moveToFirst()) {
            employeur = new Employeur(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nom")),
                    cursor.getString(cursor.getColumnIndex("entreprise")),
                    cursor.getString(cursor.getColumnIndex("numero_telephone")),
                    cursor.getString(cursor.getColumnIndex("adresse")),
                    cursor.getString(cursor.getColumnIndex("liens_public")),
                    cursor.getString(cursor.getColumnIndex("email"))
            );
        }
        cursor.close();
        db.close();
        return employeur;
    }


    public long insertEmployeur(String nom, String entreprise, String numeroTelephone, String adresse, String liensPublic, String email, String password) {
        if (employeurExists(email)) {
            Log.d("DatabaseHelper", "Employeur avec l'email " + email + " existe déjà.");
            return -1;  // Indique que l'employeur existe déjà
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("entreprise", entreprise);
        values.put("numero_telephone", numeroTelephone);
        values.put("adresse", adresse);
        values.put("liens_public", liensPublic);
        values.put("email", email);
        values.put("password", password);

        long newRowId = db.insert("employeurs", null, values);
        db.close();
        return newRowId;
    }


    public boolean candidatExists(String nom, String prenom, String dateNaissance, String nationalite, String numeroTelephone, String email, String ville, String cv) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM candidats WHERE nom = ? AND prenom = ? AND date_naissance = ? AND nationalite = ? AND numero_telephone = ? AND email = ? AND ville = ? AND cv = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nom, prenom, dateNaissance, nationalite, numeroTelephone, email, ville, cv});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }

    private boolean offreExists(String titre, String description, String metier, String lieu, Date dateDebut, Date dateFin, int idEmployeur) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM offres WHERE titre = ? AND description = ? AND metier = ? AND lieu = ? AND date_debut = ? AND date_fin = ? AND id_employeur = ?";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateDebutStr = dateFormat.format(dateDebut);
        String dateFinStr = dateFormat.format(dateFin);
        Cursor cursor = db.rawQuery(query, new String[]{titre, description, metier, lieu, dateDebutStr, dateFinStr, String.valueOf(idEmployeur)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }


    public long insertCandidat(String nom, String prenom, String dateNaissance, String nationalite, String numeroTelephone, String email, String ville, String cv) {
        if (candidatExists(nom, prenom, dateNaissance, nationalite, numeroTelephone, email, ville, cv)) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("prenom", prenom);
        values.put("date_naissance", dateNaissance);
        values.put("nationalite", nationalite);
        values.put("numero_telephone", numeroTelephone);
        values.put("email", email);
        values.put("ville", ville);
        values.put("cv", cv);

        long newRowId = db.insert("candidats", null, values);
        db.close();

        return newRowId;
    }


    @SuppressLint("Range")
    public Offre getOffreById(int id) {
        Offre offre = null;
        // Assuming you have a method to get a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Offres", null, "id = ?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            offre = new Offre();
            offre.setId(cursor.getInt(cursor.getColumnIndex("id")));
            offre.setTitre(cursor.getString(cursor.getColumnIndex("titre")));
            offre.setMetier(cursor.getString(cursor.getColumnIndex("metier")));
            offre.setLieu(cursor.getString(cursor.getColumnIndex("lieu")));
            offre.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            offre.setDateDebut(new Date(cursor.getLong(cursor.getColumnIndex("date_debut"))));
            offre.setDateFin(new Date(cursor.getLong(cursor.getColumnIndex("date_fin"))));
            Log.d("DatabaseHelper", "Offre loaded: " + offre.toString()); // Make sure Offre has a proper toString() method
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return offre;
    }


    @SuppressLint("Range")
    public Candidat getCandidatByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM candidats WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        Candidat candidat = null;
        if (cursor.moveToFirst()) {
            candidat = new Candidat();
            candidat.setId(cursor.getInt(cursor.getColumnIndex("id")));
            candidat.setNom(cursor.getString(cursor.getColumnIndex("nom")));
            candidat.setPrenom(cursor.getString(cursor.getColumnIndex("prenom")));
            candidat.setDateNaissance(cursor.getString(cursor.getColumnIndex("date_naissance")));
            candidat.setNationalite(cursor.getString(cursor.getColumnIndex("nationalite")));
            candidat.setNumeroTelephone(cursor.getString(cursor.getColumnIndex("numero_telephone")));
            candidat.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            candidat.setVille(cursor.getString(cursor.getColumnIndex("ville")));
            candidat.setCv(cursor.getString(cursor.getColumnIndex("cv")));
        }
        cursor.close();
        return candidat;
    }

    public boolean verifyEmployeurCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT password FROM employeurs WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean isAuthenticated = false;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
            isAuthenticated = storedPassword.equals(password); // For actual use, replace with secure password verification
        }
        cursor.close();
        db.close();
        return isAuthenticated;
    }

    public long insertOffre(String titre, String description, String metier, String lieu, Date dateDebut, Date dateFin, int idEmployeur) {
        // Vérifier si une offre similaire existe déjà
        if (offreExists(titre, description, metier, lieu, dateDebut, dateFin, idEmployeur)) {
            // Offre existante, ne rien insérer et retourner -1 ou gérer l'erreur de votre manière
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titre", titre);
        values.put("description", description);
        values.put("metier", metier);
        values.put("lieu", lieu);

        // Formater les dates en chaînes de caractères
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateDebutStr = dateFormat.format(dateDebut);
        String dateFinStr = dateFormat.format(dateFin);

        values.put("date_debut", dateDebutStr);
        values.put("date_fin", dateFinStr);
        values.put("id_employeur", idEmployeur);

        long newRowId = db.insert("offres", null, values);
        db.close();

        return newRowId;
    }

    public long insertCandidature(Candidature candidature) {
        // Vérifier si la candidature n'a pas déjà été déposée
        if (candidatureExists(candidature.getIdOffre(), candidature.getIdCandidat())) {
            // La candidature existe déjà, ne rien insérer et retourner -1
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_offre", candidature.getIdOffre());
        values.put("id_candidat", candidature.getIdCandidat());
        values.put("nom_candidat", candidature.getNomCandidat());
        values.put("prenom_candidat", candidature.getPrenomCandidat());
        values.put("email_candidat", candidature.getEmailCandidat());
        values.put("cv_candidat", candidature.getCvCandidat());
        values.put("date_candidature", candidature.getDateCandidat().getTime());
        values.put("statut_candidature", candidature.getStatutCandidat());

        long newRowId = db.insert("candidatures", null, values);
        db.close();

        return newRowId;
    }

    private boolean candidatureExists(int idOffre, long idCandidat) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Candidatures",
                new String[]{"id"},
                "id_offre = ? AND id_candidat = ?",
                new String[]{String.valueOf(idOffre), String.valueOf(idCandidat)},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }




    @SuppressLint("Range")
    public List<Offre> getAllOffres() {
        List<Offre> offres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM offres";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Offre offre = new Offre();
                offre.setId(cursor.getInt(cursor.getColumnIndex("id")));
                offre.setTitre(cursor.getString(cursor.getColumnIndex("titre")));
                offre.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                offre.setMetier(cursor.getString(cursor.getColumnIndex("metier")));
                offre.setLieu(cursor.getString(cursor.getColumnIndex("lieu")));

                // Conversion des dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date dateDebut = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_debut")));
                    Date dateFin = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_fin")));
                    offre.setDateDebut(dateDebut);
                    offre.setDateFin(dateFin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                offre.setIdEmployeur(cursor.getInt(cursor.getColumnIndex("id_employeur")));

                offres.add(offre);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return offres;
    }

    @SuppressLint("Range")
    public List<Offre> getOffresFiltered(String metier, String lieu, String dateDebut, String dateFin) {
        List<Offre> offres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM offres WHERE ";
        List<String> conditions = new ArrayList<>();

        if (!metier.isEmpty()) {
            conditions.add("metier LIKE '%" + metier + "%'");
        }

        if (!lieu.isEmpty()) {
            conditions.add("lieu LIKE '%" + lieu + "%'");
        }

        if (!dateDebut.isEmpty() && !dateFin.isEmpty()) {
            conditions.add("date_debut >= '" + dateDebut + "' AND date_fin <= '" + dateFin + "'");
        } else if (!dateDebut.isEmpty()) {
            conditions.add("date_debut >= '" + dateDebut + "'");
        } else if (!dateFin.isEmpty()) {
            conditions.add("date_fin <= '" + dateFin + "'");
        }

        if (!conditions.isEmpty()) {
            query += String.join(" AND ", conditions);
        } else {
            query += "1";
        }

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Offre offre = new Offre();
                offre.setId(cursor.getInt(cursor.getColumnIndex("id")));
                offre.setTitre(cursor.getString(cursor.getColumnIndex("titre")));
                offre.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                offre.setMetier(cursor.getString(cursor.getColumnIndex("metier")));
                offre.setLieu(cursor.getString(cursor.getColumnIndex("lieu")));

                // Conversion des dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date dateDebutObj = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_debut")));
                    Date dateFinObj = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_fin")));
                    offre.setDateDebut(dateDebutObj);
                    offre.setDateFin(dateFinObj);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                offre.setIdEmployeur(cursor.getInt(cursor.getColumnIndex("id_employeur")));

                offres.add(offre);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return offres;
    }

    @SuppressLint("Range")
    public List<Offre> getOffresParLieu(String lieu) {
        List<Offre> offres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM offres WHERE lieu = ?";
        Cursor cursor = db.rawQuery(query, new String[]{lieu});

        if (cursor.moveToFirst()) {
            do {
                Offre offre = new Offre();
                offre.setId(cursor.getInt(cursor.getColumnIndex("id")));
                offre.setTitre(cursor.getString(cursor.getColumnIndex("titre")));
                offre.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                offre.setMetier(cursor.getString(cursor.getColumnIndex("metier")));
                offre.setLieu(cursor.getString(cursor.getColumnIndex("lieu")));

                // Conversion des dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date dateDebut = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_debut")));
                    Date dateFin = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date_fin")));
                    offre.setDateDebut(dateDebut);
                    offre.setDateFin(dateFin);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                offre.setIdEmployeur(cursor.getInt(cursor.getColumnIndex("id_employeur")));

                offres.add(offre);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return offres;
    }

    public String getOffreParId(int idOffre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Offres",
                new String[]{"titre"},
                "id = ?",
                new String[]{String.valueOf(idOffre)},
                null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String titreOffre = cursor.getString(cursor.getColumnIndex("titre"));
            cursor.close();
            db.close();
            return titreOffre;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public List<Candidature> getCandidaturesParCandidat(long candidatId) {
        List<Candidature> candidatures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Candidatures",
                new String[]{"id", "id_offre", "id_candidat", "nom_candidat", "prenom_candidat", "email_candidat", "cv_candidat", "date_candidature", "statut_candidature"},
                "id_candidat = ?",
                new String[]{String.valueOf(candidatId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Candidature candidature = new Candidature(
                        cursor.getInt(cursor.getColumnIndex("id_offre")),
                        cursor.getLong(cursor.getColumnIndex("id_candidat")),
                        cursor.getString(cursor.getColumnIndex("nom_candidat")),
                        cursor.getString(cursor.getColumnIndex("prenom_candidat")),
                        cursor.getString(cursor.getColumnIndex("email_candidat")),
                        cursor.getString(cursor.getColumnIndex("cv_candidat")),
                        new Date(cursor.getLong(cursor.getColumnIndex("date_candidature"))),
                        cursor.getString(cursor.getColumnIndex("statut_candidature"))
                );
                candidature.setId(cursor.getInt(cursor.getColumnIndex("id")));
                candidatures.add(candidature);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return candidatures;
    }




    public void deleteOffre(int offreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OFFRES, COLUMN_ID + " = ?",
                new String[]{String.valueOf(offreId)});
        db.close();
    }


    public void updateOffre(int offreId, String nouveauTitre, String nouvelleDescription, String nouveauMetier, String nouveauLieu, Date nouvelleDateDebut, Date nouvelleDateFin, int nouvelIdEmployeur) {
        // Le corps de la méthode reste le même que ce que vous avez déjà
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("titre", nouveauTitre);
        values.put("description", nouvelleDescription);
        values.put("metier", nouveauMetier);
        values.put("lieu", nouveauLieu);
        // Convertir les dates en format adapté pour la base de données
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        values.put("date_debut", dateFormat.format(nouvelleDateDebut));
        values.put("date_fin", dateFormat.format(nouvelleDateFin));
        values.put("id_employeur", nouvelIdEmployeur);

        // Clause WHERE pour identifier quelle offre mettre à jour
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(offreId) };

        // Effectuer la mise à jour
        int count = db.update("offres", values, selection, selectionArgs);
        Log.d("DatabaseHelper", "Nombre de lignes mises à jour : " + count);

        db.close(); // Fermer la connexion à la base de données
    }





}