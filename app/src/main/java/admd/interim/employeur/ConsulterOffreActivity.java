package admd.interim.employeur;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;

public class ConsulterOffreActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_offre);

        // Initialize TextViews
        TextView textOfferTitle = findViewById(R.id.textOfferTitle);
        TextView metierTextView = findViewById(R.id.metier_offre);
        TextView lieuTextView = findViewById(R.id.lieu_offre);
        TextView descriptionTextView = findViewById(R.id.description_offre);
        TextView dateDebutTextView = findViewById(R.id.date_debut_offre);
        TextView dateFinTextView = findViewById(R.id.date_fin_offre);

        // Retrieve the offer ID passed through the Intent
        int offreId = getIntent().getIntExtra("offre_id", -1);
        if (offreId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Offre offre = dbHelper.getOffreById(offreId);

            // Check if the Offre object is not null and update TextViews
            if (offre != null) {
                textOfferTitle.setText(offre.getTitre() != null ? offre.getTitre() : "Title Unavailable");

                if (offre.getMetier() != null) {
                    metierTextView.setText(offre.getMetier());
                } else {
                    Log.d("ConsulterOffreActivity", "Métier is null for Offre ID: " + offreId);
                    metierTextView.setText("Métier Unavailable");
                }

                lieuTextView.setText(offre.getLieu() != null ? offre.getLieu() : "Lieu Unavailable");
                descriptionTextView.setText(offre.getDescription() != null ? offre.getDescription() : "Description Unavailable");
                dateDebutTextView.setText(offre.getDateDebut() != null ? offre.getDateDebut().toString() : "Start Date Unavailable");
                dateFinTextView.setText(offre.getDateFin() != null ? offre.getDateFin().toString() : "End Date Unavailable");
            } else {
                Log.d("ConsulterOffreActivity", "No offer details found for ID: " + offreId);
            }
        } else {
            Log.d("ConsulterOffreActivity", "Invalid or missing offer ID in Intent extras");
        }
    }
}