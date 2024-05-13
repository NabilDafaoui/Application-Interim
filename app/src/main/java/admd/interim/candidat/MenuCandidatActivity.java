package admd.interim.candidat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;

public class MenuCandidatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_candidat);

        Button buttonConnexion = findViewById(R.id.button_connexion);
        Button buttonInscription = findViewById(R.id.button_inscription);

        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuCandidatActivity.this, ConnexionCandidatActivity.class);
                startActivity(intent);
            }
        });

        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuCandidatActivity.this, InscriptionCandidatActivity.class);
                startActivity(intent);
            }
        });
    }
}
