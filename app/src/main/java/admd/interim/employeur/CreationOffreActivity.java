package admd.interim.employeur;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import admd.interim.logic.DatabaseHelper;
import admd.interim.R;

public class CreationOffreActivity extends AppCompatActivity {

    private EditText editTextTitre, editTextDescription, editTextMetier, editTextLieu, editTextDateDebut, editTextDateFin;
    private Button buttonEnregistrerOffre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_offre);

        // Récupération des références des vues
        editTextTitre = findViewById(R.id.editTextTitre);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextMetier = findViewById(R.id.editTextMetier);
        editTextLieu = findViewById(R.id.editTextLieu);
        editTextDateDebut = findViewById(R.id.editTextDateDebut);
        editTextDateFin = findViewById(R.id.editTextDateFin);
        buttonEnregistrerOffre = findViewById(R.id.buttonEnregistrerOffre);

        buttonEnregistrerOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs
                String titre = editTextTitre.getText().toString();
                String description = editTextDescription.getText().toString();
                String metier = editTextMetier.getText().toString();
                String lieu = editTextLieu.getText().toString();
                String dateDebutStr = editTextDateDebut.getText().toString();
                String dateFinStr = editTextDateFin.getText().toString();

                // Convertir les dates de type String en objets Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateDebut, dateFin;
                try {
                    dateDebut = dateFormat.parse(dateDebutStr);
                    dateFin = dateFormat.parse(dateFinStr);

                    // Enregistrer l'offre dans la base de données
                    int idEmployeur = 1; // Exemple : ID de l'employeur actuellement connecté
                    long result = insertOffre(titre, description, metier, lieu, dateDebut, dateFin, idEmployeur);

                    if (result != -1) {
                        Toast.makeText(CreationOffreActivity.this, "Offre créée avec succès", Toast.LENGTH_SHORT).show();
                        // Rediriger ou effectuer une action après la création de l'offre
                        finish(); // Terminer l'activité après la création de l'offre
                    } else {
                        Toast.makeText(CreationOffreActivity.this, "Échec de la création de l'offre", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(CreationOffreActivity.this, "Erreur de format de date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private long insertOffre(String titre, String description, String metier, String lieu, Date dateDebut, Date dateFin, int idEmployeur) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.insertOffre(titre, description, metier, lieu, dateDebut, dateFin, idEmployeur);
    }
}
