package admd.interim.employeur;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import admd.interim.R;
import admd.interim.logic.DatabaseHelper;

public class InscriptionEmployeurActivity extends AppCompatActivity {

    private EditText editTextNom, editTextEntreprise, editTextEmail, editTextTelephone, editTextAdresse, editTextLienPublic;
    private EditText editTextPassword, editTextConfirmPassword;
    private TextView textViewToggle;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_employeur);

        // Référencer les vues
        setupViews();

        // Setup listeners
        setupListeners();
    }

    private void setupViews() {
        editTextNom = findViewById(R.id.editTextNom);
        editTextEntreprise = findViewById(R.id.editTextEntreprise);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextLienPublic = findViewById(R.id.editTextLienPublic);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        textViewToggle = findViewById(R.id.textViewToggle);
    }

    private void setupListeners() {
        Button buttonEnregistrer = findViewById(R.id.buttonEnregistrer);
        buttonEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    enregistrerEmployeur();
                }
            }
        });

        textViewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
                    editTextConfirmPassword.setError("Les mots de passe ne correspondent pas");
                }
            }
        });
    }

    private boolean validateForm() {
        // Add validation for each field
        if (editTextNom.getText().toString().trim().isEmpty() ||
                editTextEntreprise.getText().toString().trim().isEmpty() ||
                editTextAdresse.getText().toString().trim().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches() ||
                !Patterns.PHONE.matcher(editTextTelephone.getText().toString().trim()).matches() ||
                !Patterns.WEB_URL.matcher(editTextLienPublic.getText().toString().trim()).matches() ||
                editTextPassword.getText().toString().trim().isEmpty() ||
                !editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            showMissingFieldsDialog();
            return false;
        }
        return true;
    }

    private void enregistrerEmployeur() {
        String nom = editTextNom.getText().toString().trim();
        String entreprise = editTextEntreprise.getText().toString().trim();
        String telephone = editTextTelephone.getText().toString().trim();
        String adresse = editTextAdresse.getText().toString().trim();
        String lienPublic = editTextLienPublic.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        long employeurId = databaseHelper.insertEmployeur(nom, entreprise, telephone, adresse, lienPublic, email, password);
        if (employeurId != -1) {
            showSuccessDialog(employeurId);
        } else {
            showFailedRegistrationDialog();
        }
    }

    private void togglePasswordVisibility() {
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

    private void showMissingFieldsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Champs obligatoires manquants");
        builder.setMessage("Veuillez remplir tous les champs requis et vous assurer que les mots de passe correspondent.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSuccessDialog(final long employeurId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Inscription réussie");
        builder.setMessage("Votre inscription en tant qu'employeur a été effectuée avec succès. Vous allez être redirigé vers l'espace employeur.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InscriptionEmployeurActivity.this, EspaceEmployeurActivity.class);
                intent.putExtra("EMPLOYEUR_ID", employeurId); // Passer l'ID de l'employeur
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showFailedRegistrationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Échec de l'inscription");
        builder.setMessage("Une erreur est survenue lors de l'enregistrement. Veuillez réessayer.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
