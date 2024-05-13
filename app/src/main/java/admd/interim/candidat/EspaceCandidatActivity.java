package admd.interim.candidat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;
import admd.interim.logic.Candidat;

public class EspaceCandidatActivity extends AppCompatActivity {
    private EditText editTextMetier, editTextLieu, editTextDateDebut, editTextDateFin;
    private Button buttonFiltrer;
    private ImageButton buttonMesCandidatures ;
    private ListView listOffres;
    private DatabaseHelper databaseHelper;
    private OffreAdapter adapter;

    private String nom;
    private String prenom;
    private String dateNaissance;
    private int candidatId;
    private String nationalite;
    private String numeroTelephone;
    private String email;
    private String ville;
    private String cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_candidat);

        editTextMetier = findViewById(R.id.editText_metier);
        editTextLieu = findViewById(R.id.editText_lieu);
        editTextDateDebut = findViewById(R.id.editText_date_debut);
        editTextDateFin = findViewById(R.id.editText_date_fin);
        buttonFiltrer = findViewById(R.id.button_filtrer);
        listOffres = findViewById(R.id.list_offers);

        databaseHelper = new DatabaseHelper(this);

        // Afficher toutes les offres avant de filtrer
        displayAllOffres();

        buttonFiltrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metier = editTextMetier.getText().toString().trim();
                String lieu = editTextLieu.getText().toString().trim();
                String dateDebut = editTextDateDebut.getText().toString().trim();
                String dateFin = editTextDateFin.getText().toString().trim();

                List<Offre> offres = databaseHelper.getOffresFiltered(metier, lieu, dateDebut, dateFin);
                updateListView(offres);
            }
        });

        ImageButton buttonMesCandidatures = findViewById(R.id.buttonMesCandidatures);
        buttonMesCandidatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceCandidatActivity.this, MesCandidaturesActivity.class);
                intent.putExtra("candidat_id", candidatId);
                startActivity(intent);
            }
        });


        // Récupérer les informations du candidat à partir de l'Intent
        nom = getIntent().getStringExtra("candidat_nom");
        prenom = getIntent().getStringExtra("candidat_prenom");
        dateNaissance = getIntent().getStringExtra("candidat_date_naissance");
        candidatId = getIntent().getIntExtra("candidat_id", 0);
        nationalite = getIntent().getStringExtra("candidat_nationalite");
        numeroTelephone = getIntent().getStringExtra("candidat_numero_telephone");
        email = getIntent().getStringExtra("candidat_email");
        ville = getIntent().getStringExtra("candidat_ville");
        cv = getIntent().getStringExtra("candidat_cv");

    }

    private void displayAllOffres() {
        List<Offre> offres = databaseHelper.getAllOffres();
        updateListView(offres);
    }

    private void updateListView(List<Offre> offres) {
        adapter = new OffreAdapter(this, offres);
        listOffres.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermer la connexion à la base de données
        databaseHelper.close();
    }

    public class OffreAdapter extends ArrayAdapter<Offre> {
        private Map<Integer, Boolean> detailsVisibles = new HashMap<>();
        private Context context;
        private List<Offre> offres;

        public OffreAdapter(Context context, List<Offre> offres) {
            super(context, 0, offres);
            this.context = context;
            this.offres = offres;
        }

        @Override
        public int getCount() {
            return offres.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_offre, parent, false);
            }

            Offre offre = offres.get(position);

            TextView titreOffre = convertView.findViewById(R.id.titre_offre);
            TextView metierOffre = convertView.findViewById(R.id.metier_offre);
            TextView lieuOffre = convertView.findViewById(R.id.lieu_offre);
            ImageButton buttonPlus = convertView.findViewById(R.id.button_plus);
            Button buttonCandidater = convertView.findViewById(R.id.button_candidater);

            TextView descriptionOffre = convertView.findViewById(R.id.description_offre);
            TextView dateDebutOffre = convertView.findViewById(R.id.date_debut_offre);
            TextView dateFinOffre = convertView.findViewById(R.id.date_fin_offre);

            titreOffre.setText(offre.getTitre());
            metierOffre.setText(Html.fromHtml("<b>Métier :</b> " + offre.getMetier()));
            lieuOffre.setText(Html.fromHtml("<b>Lieu :</b> " + offre.getLieu()));

            boolean detailsVisible = detailsVisibles.containsKey(position) && detailsVisibles.get(position);

            descriptionOffre.setVisibility(detailsVisible ? View.VISIBLE : View.GONE);
            dateDebutOffre.setVisibility(detailsVisible ? View.VISIBLE : View.GONE);
            dateFinOffre.setVisibility(detailsVisible ? View.VISIBLE : View.GONE);
            buttonCandidater.setVisibility(detailsVisible ? View.VISIBLE : View.GONE);

            if (detailsVisible) {
                descriptionOffre.setText(Html.fromHtml("<b>Description :</b> " + offre.getDescription()));

                // Formater la date de début
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dateDebutFormatted = dateFormat.format(offre.getDateDebut());
                dateDebutOffre.setText(Html.fromHtml("<b>Date de début :</b> " + dateDebutFormatted));

                // Formater la date de fin
                String dateFinFormatted = dateFormat.format(offre.getDateFin());
                dateFinOffre.setText(Html.fromHtml("<b>Date de fin :</b> " + dateFinFormatted));
            }

            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDetailsVisibility(position);
                }
            });

            buttonCandidater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ouvrir la page de candidature pour l'offre
                    openCandidaturePage(offre);
                }
            });

            // Supprimer la ligne vide à la fin de la ListView
            convertView.setMinimumHeight(0);

            return convertView;
        }

        private void toggleDetailsVisibility(int position) {
            boolean detailsVisible = detailsVisibles.containsKey(position) && detailsVisibles.get(position);
            detailsVisibles.put(position, !detailsVisible);
            notifyDataSetChanged();
        }

        private void openCandidaturePage(Offre offre) {
            // Ouvrir la page de candidature pour l'offre
            Intent intent = new Intent(context, CandidaturePage.class);
            intent.putExtra("offre", offre);
            intent.putExtra("candidat_nom", nom);
            intent.putExtra("candidat_prenom", prenom);
            intent.putExtra("candidat_date_naissance", dateNaissance);
            intent.putExtra("candidat_id", candidatId);
            intent.putExtra("candidat_nationalite", nationalite);
            intent.putExtra("candidat_numero_telephone", numeroTelephone);
            intent.putExtra("candidat_email", email);
            intent.putExtra("candidat_ville", ville);
            intent.putExtra("candidat_cv", cv);
            context.startActivity(intent);
        }
    }
}