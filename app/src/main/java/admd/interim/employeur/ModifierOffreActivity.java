package admd.interim.employeur;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;

public class ModifierOffreActivity extends AppCompatActivity {

    private Offre offre;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_offre);

        dbHelper = new DatabaseHelper(this);

        offre = getIntent().getParcelableExtra("offre");
        if (offre != null) {
            afficherDetailsOffre();
        }
    }

    private void afficherDetailsOffre() {
        EditText editTextTitre = findViewById(R.id.editTextTitre);
        editTextTitre.setText(offre.getTitre());

        // Affichez d'autres détails de l'offre ici en fonction de vos besoins
        // Exemple : editTextDescription.setText(offre.getDescription());
    }

    public void sauvegarderModifications(View view) {
        String nouveauTitre = ((EditText) findViewById(R.id.editTextTitre)).getText().toString();
        // Récupérez d'autres valeurs mises à jour ici si nécessaire

        // Mettez à jour l'objet Offre avec les nouvelles valeurs
        offre.setTitre(nouveauTitre);
        // Mettez à jour d'autres attributs d'offre ici si nécessaire

        // Enregistrez les modifications dans la base de données
        dbHelper.updateOffre(
                (int) offre.getId(),                    // Offre ID
                offre.getTitre(),                       // Nouveau titre
                offre.getDescription(),                 // Nouvelle description
                offre.getMetier(),                      // Nouveau métier
                offre.getLieu(),                        // Nouveau lieu
                offre.getDateDebut(),                   // Nouvelle date de début
                offre.getDateFin(),                     // Nouvelle date de fin
                (int) offre.getIdEmployeur()            // Nouvel ID employeur
        );


        Log.d("ModifierOffreActivity", "Mise à jour de l'offre réussie.");

// Ou bien, si vous souhaitez effectuer une action conditionnelle en fonction du succès de la mise à jour, vous pouvez le faire directement sans utiliser de variable intermédiaire
// Par exemple, afficher un message ou effectuer d'autres actions
        Toast.makeText(ModifierOffreActivity.this, "Mise à jour de l'offre réussie.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Assurez-vous de fermer la connexion à la base de données lorsque vous n'en avez plus besoin
    }
}
