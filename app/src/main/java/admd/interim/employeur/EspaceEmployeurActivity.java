package admd.interim.employeur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Employeur;

public class EspaceEmployeurActivity extends AppCompatActivity {

    private Button buttonCreerOffre, buttonGererOffre, buttonGererCandidatures, buttonGererCandidaturesAcceptes;
    private TextView textViewEmployeurNom, textViewEmployeurDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_employeur);

        setupButtons();
        displayEmployeurDetails();
    }

    private void displayEmployeurDetails() {
        long employeurId = getIntent().getLongExtra("EMPLOYEUR_ID", -1);
        if (employeurId != -1) {
            DatabaseHelper db = new DatabaseHelper(this);
            Employeur employeur = db.getEmployeurById(employeurId);
            if (employeur != null) {
                TextView tvNom = findViewById(R.id.textViewEmployeurNom);
                TextView tvDetails = findViewById(R.id.textViewEmployeurDetails);
                tvNom.setText(employeur.getNom());
                tvDetails.setText(String.format("Entreprise: %s\nEmail: %s\nTéléphone: %s",
                        employeur.getEntreprise(), employeur.getEmail(), employeur.getNumeroTelephone()));
            } else {
                Log.e("EspaceEmployeurActivity", "Aucune donnée pour l'employeur");
            }
        } else {
            Log.e("EspaceEmployeurActivity", "ID employeur non trouvé");
        }
    }



    private void setupButtons() {
        buttonCreerOffre = findViewById(R.id.buttonCreerOffre);
        buttonCreerOffre.setOnClickListener(v -> startActivity(new Intent(this, CreationOffreActivity.class)));

        buttonGererOffre = findViewById(R.id.buttonGererOffre);
        buttonGererOffre.setOnClickListener(v -> startActivity(new Intent(this, GestionOffreActivity.class)));

        buttonGererCandidatures = findViewById(R.id.buttonGererCandidatures);
        buttonGererCandidatures.setOnClickListener(v -> startActivity(new Intent(this, GestionCandidatureActivity.class)));

        buttonGererCandidaturesAcceptes = findViewById(R.id.buttonGererCandidaturesAcceptes);
        buttonGererCandidaturesAcceptes.setOnClickListener(v -> startActivity(new Intent(this, CandidaturesAccepteesActivity.class)));
    }
}
