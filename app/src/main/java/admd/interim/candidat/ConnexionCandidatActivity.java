package admd.interim.candidat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.Candidat;
import admd.interim.logic.DatabaseHelper;

public class ConnexionCandidatActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_candidat);

        editTextEmail = findViewById(R.id.editText_email);
        Button buttonConnexion = findViewById(R.id.button_connexion);

        databaseHelper = new DatabaseHelper(this);

        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                // Récupérer les informations du candidat depuis la base de données
                Candidat candidat = databaseHelper.getCandidatByEmail(email);

                if (candidat != null) {
                    // Connexion réussie
                    showSuccessDialog(candidat);
                } else {
                    // Adresse email invalide
                    showInvalidCredentialsDialog();
                }
            }
        });
    }

    // Affiche une boîte de dialogue pour informer l'utilisateur que les informations de connexion saisies sont incorrectes
    private void showInvalidCredentialsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informations incorrectes");
        builder.setMessage("Les informations de connexion saisies sont incorrectes. Veuillez réessayer.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Affiche une boîte de dialogue pour informer l'utilisateur que la connexion a réussi
    private void showSuccessDialog(Candidat candidat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connexion réussie");
        builder.setMessage("Bienvenue " + candidat.getPrenom() + " " + candidat.getNom() + ", vous allez être redirigé vers l'espace candidat.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Démarrer EspaceCandidatActivity
                Intent intent = new Intent(ConnexionCandidatActivity.this, EspaceCandidatActivity.class);
                intent.putExtra("candidat_id", candidat.getId());
                intent.putExtra("candidat_nom", candidat.getNom());
                intent.putExtra("candidat_prenom", candidat.getPrenom());
                intent.putExtra("candidat_date_naissance", candidat.getDateNaissance());
                intent.putExtra("candidat_nationalite", candidat.getNationalite());
                intent.putExtra("candidat_numero_telephone", candidat.getNumeroTelephone());
                intent.putExtra("candidat_email", candidat.getEmail());
                intent.putExtra("candidat_ville", candidat.getVille());
                intent.putExtra("candidat_cv", candidat.getCv());
                startActivity(intent);
            }
        });
        builder.setCancelable(false); // Empêcher la fermeture du dialogue en cliquant à l'extérieur
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
