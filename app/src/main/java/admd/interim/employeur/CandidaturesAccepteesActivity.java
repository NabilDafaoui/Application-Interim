package admd.interim.employeur;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import admd.interim.R;
import admd.interim.logic.DetailsCandidatActivity;


public class CandidaturesAccepteesActivity extends AppCompatActivity {

    private TextView textViewNomPrenom;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_acceptees);

        textViewNomPrenom = findViewById(R.id.textViewNomPrenom);
        textViewEmail = findViewById(R.id.textViewEmail);

        // Récupérer les informations du candidat accepté (à remplacer par vos données)
        String nomPrenom = "Nabil Dafaoui";
        String email = "Nabil.da@gmail.com";

        // Afficher les informations du candidat dans les TextView
        textViewNomPrenom.setText(nomPrenom);
        textViewEmail.setText(email);
    }

    public void consulterCandidat(View view) {
        Intent intent = new Intent(this, DetailsCandidatActivity.class);
        intent.putExtra("NOM", "John");
        intent.putExtra("PRENOM", "Doe");
        intent.putExtra("EMAIL", "john.doe@example.com");
        startActivity(intent);
    }

    public void afficherOptionsContact(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir un moyen de contact");

        // Options de contact
        String[] options = {"Email", "Téléphone", "SMS"};

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Option Email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"john.doe@example.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sujet de votre email");
                    startActivity(Intent.createChooser(emailIntent, "Choisir une application pour envoyer l'email"));
                    break;
                case 1:
                    // Option Téléphone
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:0123456789"));
                    startActivity(phoneIntent);
                    break;
                case 2:
                    // Option SMS
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse("smsto:0123456789"));
                    smsIntent.putExtra("sms_body", "Message SMS à envoyer");
                    startActivity(smsIntent);
                    break;
            }
        });

        builder.show();
    }

}
