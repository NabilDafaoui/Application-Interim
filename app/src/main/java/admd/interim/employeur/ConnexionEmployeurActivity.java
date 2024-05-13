package admd.interim.employeur;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;

public class ConnexionEmployeurActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private DatabaseHelper databaseHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_employeur);

        databaseHelper = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editTextPassword);
        TextView textViewToggle = findViewById(R.id.textViewToggle);
        Button buttonConnexion = findViewById(R.id.button_connexion);

        // Toggle visibility of password
        textViewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    textViewToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    isPasswordVisible = false;
                } else {
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    textViewToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    isPasswordVisible = true;
                }
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });

        // Handle the login process
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmployeur();
            }
        });
    }

    private void loginEmployeur() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (databaseHelper.verifyEmployeurCredentials(email, password)) {
            showSuccessDialog();
        } else {
            showInvalidCredentialsDialog();
        }
    }

    private void showInvalidCredentialsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informations incorrectes");
        builder.setMessage("Les informations de connexion saisies sont incorrectes. Veuillez réessayer.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connexion réussie");
        builder.setMessage("Vous allez être redirigé vers l'espace employeur.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the employer's name from the editText or any other source
                String nomEmployeur = editTextEmail.getText().toString().trim();  // Simplified for demonstration
                Intent intent = new Intent(ConnexionEmployeurActivity.this, EspaceEmployeurActivity.class);
                intent.putExtra("NOM_ENTREPRISE", nomEmployeur);  // Pass the employer's name to the next activity
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
