package admd.interim.logic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;

public class DetailsCandidatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_candidat);

        // Récupérez les informations du candidat depuis l'intent
        String nom = getIntent().getStringExtra("NOM");
        String prenom = getIntent().getStringExtra("PRENOM");
        String email = getIntent().getStringExtra("EMAIL");

        // Affichez les informations du candidat dans votre layout dédié
        TextView textViewNomPrenom = findViewById(R.id.textViewNomPrenom);
        TextView textViewEmail = findViewById(R.id.textViewEmail);

        textViewNomPrenom.setText(nom + " " + prenom);
        textViewEmail.setText(email);
    }
}
