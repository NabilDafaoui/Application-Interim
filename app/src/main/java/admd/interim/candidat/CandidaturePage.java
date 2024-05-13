package admd.interim.candidat;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import admd.interim.R;
import admd.interim.logic.Candidat;
import admd.interim.logic.Candidature;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;

public class CandidaturePage extends AppCompatActivity {
    private EditText editTextNom, editTextPrenom, editTextDateNaissance, editTextNationalite, editTextNumeroTelephone, editTextEmail, editTextVille, editTextCV;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature);

        Offre offre = (Offre) getIntent().getSerializableExtra("offre");
        afficherDetailsOffre(offre);

        // Récupérer les informations du candidat à partir de l'Intent
        String nom = getIntent().getStringExtra("candidat_nom");
        String prenom = getIntent().getStringExtra("candidat_prenom");
        String dateNaissance = getIntent().getStringExtra("candidat_date_naissance");
        int candidatId = getIntent().getIntExtra("candidat_id", 0);
        String nationalite = getIntent().getStringExtra("candidat_nationalite");
        String numeroTelephone = getIntent().getStringExtra("candidat_numero_telephone");
        String email = getIntent().getStringExtra("candidat_email");
        String ville = getIntent().getStringExtra("candidat_ville");
        String cv = getIntent().getStringExtra("candidat_cv");

        editTextNom = findViewById(R.id.editText_nom);
        editTextPrenom = findViewById(R.id.editText_prenom);
        editTextDateNaissance = findViewById(R.id.editText_date_naissance);
        editTextNationalite = findViewById(R.id.editText_nationalite);
        editTextNumeroTelephone = findViewById(R.id.editText_numero_telephone);
        editTextEmail = findViewById(R.id.editText_email);
        editTextVille = findViewById(R.id.editText_ville);
        editTextCV = findViewById(R.id.editText_cv);

        databaseHelper = new DatabaseHelper(this);

        // Afficher la boîte de dialogue pour pré-remplir le formulaire
        showPreFillDialog(nom, prenom, dateNaissance, nationalite, numeroTelephone, email, ville, cv);

        Button buttonCandidater = findViewById(R.id.button_candidater);
        buttonCandidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soumettreCandidat(candidatId);
                finish();
            }
        });
    }

    private void afficherDetailsOffre(Offre offre) {
        TextView textViewTitreOffre = findViewById(R.id.textView_titre_offre);
        TextView textViewMetierOffre = findViewById(R.id.textView_metier_offre);
        TextView textViewLieuOffre = findViewById(R.id.textView_lieu_offre);
        TextView textViewDescriptionOffre = findViewById(R.id.textView_description_offre);
        TextView textViewDateDebutOffre = findViewById(R.id.textView_date_debut_offre);
        TextView textViewDateFinOffre = findViewById(R.id.textView_date_fin_offre);

        textViewTitreOffre.setText(offre.getTitre());
        textViewMetierOffre.setText(offre.getMetier());
        textViewLieuOffre.setText(offre.getLieu());
        textViewDescriptionOffre.setText(offre.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateDebutFormatted = dateFormat.format(offre.getDateDebut());
        String dateFinFormatted = dateFormat.format(offre.getDateFin());
        textViewDateDebutOffre.setText(dateDebutFormatted);
        textViewDateFinOffre.setText(dateFinFormatted);
    }

    private void showPreFillDialog(String nom, String prenom, String dateNaissance, String nationalite, String numeroTelephone, String email, String ville, String cv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pré-remplir le formulaire");
        builder.setMessage("Vos informations ont été récupérées. Voulez-vous pré-remplir le formulaire de candidature ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Pré-remplir les champs du formulaire
                editTextNom.setText(nom);
                editTextPrenom.setText(prenom);
                editTextDateNaissance.setText(dateNaissance);
                editTextNationalite.setText(nationalite);
                editTextNumeroTelephone.setText(numeroTelephone);
                editTextEmail.setText(email);
                editTextVille.setText(ville);
                editTextCV.setText(cv);
            }
        });
        builder.setNegativeButton("Non", null);
        builder.show();
    }

    private void soumettreCandidat(long candidatId) {
        String nom = editTextNom.getText().toString();
        String prenom = editTextPrenom.getText().toString();
        String dateNaissance = editTextDateNaissance.getText().toString();
        String nationalite = editTextNationalite.getText().toString();
        String numeroTelephone = editTextNumeroTelephone.getText().toString();
        String email = editTextEmail.getText().toString();
        String ville = editTextVille.getText().toString();
        String cv = editTextCV.getText().toString();


        Offre offre = (Offre) getIntent().getSerializableExtra("offre");
        Candidature candidature = new Candidature(
                offre.getId(),
                candidatId,
                nom,
                prenom,
                email,
                cv,
                new Date(),
                "en attente"
        );
        databaseHelper.insertCandidature(candidature);

        // Envoyer une notification à l'employeur
        notifierEmployeur(offre, candidature);
        Toast.makeText(this, "Candidature envoyée avec succès", Toast.LENGTH_SHORT).show();
    }

    private void notifierEmployeur(Offre offre, Candidature candidature) {
        // Implémentez la logique d'envoi de notification à l'employeur
        // (e-mail, notification push, mise à jour dans une interface dédiée, etc.)
    }
}
