package admd.interim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import admd.interim.anonyme.AnonymeActivity;
import admd.interim.candidat.MenuCandidatActivity;
import admd.interim.employeur.InscriptionEmployeurActivity;
import admd.interim.employeur.MenuEmployeurActivity;
import admd.interim.logic.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private static final String PREF_LOCATION_ACCEPTED = "location_accepted";
    private boolean locationAccepted;

    public static String getPrefLocationAcceptedKey() {
        return PREF_LOCATION_ACCEPTED;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        locationAccepted = preferences.getBoolean(PREF_LOCATION_ACCEPTED, false);

        if (!locationAccepted) {
            // Afficher une boîte de dialogue ou une demande d'autorisation pour la localisation
            showLocationPermissionDialog();
        }

        Button buttonEspaceCandidatClick = findViewById(R.id.button_espace_candidat);
        buttonEspaceCandidatClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers l'activité EspaceCandidatActivity
                Intent intent = new Intent(MainActivity.this, MenuCandidatActivity.class);
                startActivity(intent);
            }
        });

        Button buttonEspaceEmployeurClick = findViewById(R.id.button_espace_employeur);
        buttonEspaceEmployeurClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers l'activité EspaceEmployeurActivity
                Intent intent = new Intent(MainActivity.this, MenuEmployeurActivity.class);
                startActivity(intent);
            }
        });

        TextView textViewContinuerAnonymement = findViewById(R.id.textView_continuer_anonymement);
        textViewContinuerAnonymement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers l'activité AnonymeActivity avec un indicateur
                Intent intent = new Intent(MainActivity.this, AnonymeActivity.class);
                intent.putExtra("ask_location_permission", true);
                startActivity(intent);
            }
        });

        // Instancier la classe DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Obtenir une instance de SQLiteDatabase en mode écriture
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Insérer un nouvel candidat
        long Candidat1 = databaseHelper.insertCandidat("ADAM", "D", "01/01/2001", "francaise", "0611111111", "adam@mail.fr", "Montpellier", "CV-Adam");

        if (Candidat1 == -1) {
            // Le candidat existe déjà, afficher un message
            Toast.makeText(this, "Le candidat existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
        } else {
            // Le candidat  a été inséré avec succès, afficher un message
            Toast.makeText(this, "Nouvel candidat inséré avec l'ID : " + Candidat1, Toast.LENGTH_SHORT).show();
        }

        long Candidat2 = databaseHelper.insertCandidat("Nabil", "Da", "10/10/2001", "marocain", "0622222222", "nabil@mail.fr", "Montpellier", "CV-Nabil");


        // Insérer un nouvel employeur
        long employeurId = databaseHelper.insertEmployeur(
                "John Doe",
                "Acme Inc.",
                "0612345678",
                "123 Main Street, Anytown USA",
                "https://www.acme.com",
                "john.doe@acme.com",
                "John"
        );

        if (employeurId == -1) {
            // L'employeur existe déjà, afficher un message
            Toast.makeText(this, "L'employeur existe déjà dans la base de données", Toast.LENGTH_LONG).show();
        } else {
            // L'employeur a été inséré avec succès, afficher un message
            Toast.makeText(this, "Nouvel employeur inséré avec l'ID : " + employeurId, Toast.LENGTH_LONG).show();
        }

        // Création des objets Date pour les dates de début et de fin de chaque offre
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

// Offre 1
        Date dateDebutOffre1;
        try {
            dateDebutOffre1 = sdf.parse("01/06/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date dateFinOffre1;
        try {
            dateFinOffre1 = sdf.parse("31/12/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

// Offre 2
        Date dateDebutOffre2;
        try {
            dateDebutOffre2 = sdf.parse("01/07/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date dateFinOffre2;
        try {
            dateFinOffre2 = sdf.parse("31/12/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

// Offre 3
        Date dateDebutOffre3;
        try {
            dateDebutOffre3 = sdf.parse("15/06/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date dateFinOffre3;
        try {
            dateFinOffre3 = sdf.parse("31/06/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

// Offre 4
        Date dateDebutOffre4;
        try {
            dateDebutOffre4 = sdf.parse("01/08/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date dateFinOffre4;
        try {
            dateFinOffre4 = sdf.parse("31/12/2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Insérer une nouvelle offre
        long offreId = databaseHelper.insertOffre(
                "Developpeur Web",
                "Nous recherchons un développeur web expérimenté pour rejoindre notre équipe dynamique. Vous serez responsable de la conception, du développement et de la maintenance de nos applications web.",
                "Développement Web",
                "Montpellier",
                dateDebutOffre1,
                dateFinOffre1,
                1
        );

        if (offreId == -1) {
            // L'offre existe déjà, afficher un message
            Toast.makeText(this, "L'offre existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
        } else {
            // L'offre a été insérée avec succès, afficher un message
            Toast.makeText(this, "Nouvelle offre insérée avec l'ID : " + offreId, Toast.LENGTH_SHORT).show();
        }

        // Insérer une nouvelle offre
        long offreId2 = databaseHelper.insertOffre(
                "Gestionnaire de Projet",
                "Nous recherchons un gestionnaire de projet expérimenté pour superviser et coordonner nos projets de développement logiciel. Vous serez responsable de la planification, de l'organisation et du suivi des projets, ainsi que de la gestion de l'équipe et de la communication avec les parties prenantes.",
                "Gestion de Projet",
                "Montpellier",
                dateDebutOffre2,
                dateFinOffre2,
                1 // ID de l'employeur Acme Inc.
        );

        if (offreId2 == -1) {
            // L'offre existe déjà, afficher un message
            Toast.makeText(this, "L'offre existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
        } else {
            // L'offre a été insérée avec succès, afficher un message
            Toast.makeText(this, "Nouvelle offre insérée avec l'ID : " + offreId2, Toast.LENGTH_SHORT).show();
        }
// Insérer une nouvelle offre
        long offreId3 = databaseHelper.insertOffre(
                "Analyste Commercial",
                "Nous recherchons un analyste commercial expérimenté pour rejoindre notre équipe de vente. Vous serez responsable de l'analyse des données de vente, de l'identification des tendances et des opportunités, ainsi que de la présentation des rapports aux équipes de direction et de vente.",
                "Analyse Commerciale",
                "Montpellier",
                dateDebutOffre3,
                dateFinOffre3,
                1 // ID de l'employeur Acme Inc.
        );

        if (offreId3 == -1) {
            // L'offre existe déjà, afficher un message
            Toast.makeText(this, "L'offre existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
        } else {
            // L'offre a été insérée avec succès, afficher un message
            Toast.makeText(this, "Nouvelle offre insérée avec l'ID : " + offreId3, Toast.LENGTH_SHORT).show();
        }

// Insérer une nouvelle offre
        long offreId4 = databaseHelper.insertOffre(
                "Responsable des Ressources Humaines",
                "Nous recherchons un responsable des ressources humaines expérimenté pour gérer et développer notre équipe. Vous serez responsable du recrutement, de la formation, de la gestion des performances et de la conformité aux réglementations en matière d'emploi.",
                "Ressources Humaines",
                "Paris",
                dateDebutOffre4,
                dateFinOffre4,
                1 // ID de l'employeur Acme Inc.
        );

        if (offreId4 == -1) {
            // L'offre existe déjà, afficher un message
            Toast.makeText(this, "L'offre existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
        } else {
            // L'offre a été insérée avec succès, afficher un message
            Toast.makeText(this, "Nouvelle offre insérée avec l'ID : " + offreId4, Toast.LENGTH_SHORT).show();
        }


    }

    private void showLocationPermissionDialog() {
        // Afficher une boîte de dialogue demandant l'autorisation de la localisation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Autoriser la localisation");
        builder.setMessage("Pour afficher des annonces pertinentes près de chez vous, nous avons besoin d'accéder à votre localisation.");
        builder.setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // L'utilisateur accepte, enregistrer l'acceptation dans les préférences
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putBoolean(PREF_LOCATION_ACCEPTED, true);
                editor.apply();
                locationAccepted = true;
                // Afficher les annonces en fonction de la localisation
                displayLocationBasedAds();
            }
        });
        builder.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // L'utilisateur refuse, afficher des annonces sélectionnées par un algorithme
                displayDefaultAds();
            }
        });
        builder.show();
    }


    private void displayLocationBasedAds() {
        // Afficher les annonces basées sur la localisation de l'utilisateur
        // Vous pouvez implémenter cette fonction en fonction de votre logique métier
    }

    private void displayDefaultAds() {
        // Afficher des annonces sélectionnées par défaut
        // Cela pourrait inclure des annonces récentes, les plus populaires, etc.
        // Implémentez cette fonction en fonction de votre algorithme de sélection
    }



}