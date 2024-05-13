package admd.interim.candidat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;

public class InscriptionCandidatActivity extends AppCompatActivity {

    private EditText editTextNom, editTextPrenom, editTextDateNaissance, editTextNationalite, editTextNumeroTelephone, editTextEmail, editTextVille, editTextCV;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_candidat);

        editTextNom = findViewById(R.id.editText_nom);
        editTextPrenom = findViewById(R.id.editText_prenom);
        editTextDateNaissance = findViewById(R.id.editText_date_naissance);
        editTextNationalite = findViewById(R.id.editText_nationalite);
        editTextNumeroTelephone = findViewById(R.id.editText_numero_telephone);
        editTextEmail = findViewById(R.id.editText_email);
        editTextVille = findViewById(R.id.editText_ville);
        editTextCV = findViewById(R.id.editText_cv);

        Button buttonInscrire = findViewById(R.id.button_inscrire);
        buttonInscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inscrireCandidat();
            }
        });

        databaseHelper = new DatabaseHelper(this);
    }

    private void inscrireCandidat() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String dateNaissance = editTextDateNaissance.getText().toString().trim();
        String nationalite = editTextNationalite.getText().toString().trim();
        String numeroTelephone = editTextNumeroTelephone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String ville = editTextVille.getText().toString().trim();
        String cv = editTextCV.getText().toString().trim();

        // Vérifier si les champs obligatoires sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showMissingFieldsDialog();
            return;
        }

        long candidatId = databaseHelper.insertCandidat(nom, prenom, dateNaissance, nationalite, numeroTelephone, email, ville, cv);

        if (candidatId == -1) {
            showCandidatExistsDialog();
        } else {
            showSuccessDialog();
        }
    }

    // Affiche une boîte de dialogue pour informer l'utilisateur que les champs obligatoires ne sont pas remplis
    private void showMissingFieldsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Champs obligatoires manquants");
        builder.setMessage("Veuillez remplir les champs Nom, Prénom et Email.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Affiche une boîte de dialogue pour informer l'utilisateur que le candidat existe déjà
    private void showCandidatExistsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Candidat existant");
        builder.setMessage("Un candidat avec les mêmes informations existe déjà dans la base de données.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Affiche une boîte de dialogue pour informer l'utilisateur que l'inscription a réussi
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Inscription réussie");
        builder.setMessage("Votre inscription en tant que candidat a été effectuée avec succès. Vous allez être redirigé vers la page d'accueil.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Rediriger vers MenuCandidatActivity
                Intent intent = new Intent(InscriptionCandidatActivity.this, MenuCandidatActivity.class);
                startActivity(intent);
                finish(); // Fermer l'activité InscriptionCandidatActivity
            }
        });
        builder.setCancelable(false); // Empêcher la fermeture du dialogue en cliquant à l'extérieur
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
