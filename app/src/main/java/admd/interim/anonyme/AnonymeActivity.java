package admd.interim.anonyme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import admd.interim.R;
import admd.interim.candidat.InscriptionCandidatActivity;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;

public class AnonymeActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listOffres;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private OffreAdapter adapter; // Ajout d'un attribut pour l'adapter

    // Références des EditText pour les filtres
    private EditText editTextMetier, editTextLieu, editTextDateDebut, editTextDateFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonyme);

        // Instancier la classe DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Récupérer la référence à la ListView
        listOffres = findViewById(R.id.list_offers);

        // Récupérer les références des EditText pour les filtres
        editTextMetier = findViewById(R.id.editText_metier);
        editTextLieu = findViewById(R.id.editText_lieu);
        editTextDateDebut = findViewById(R.id.editText_date_debut);
        editTextDateFin = findViewById(R.id.editText_date_fin);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getUserLocation();
        }

        Button buttonInscrireCandidat = findViewById(R.id.button_inscrire_candidat);
        buttonInscrireCandidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnonymeActivity.this, InscriptionCandidatActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter un OnClickListener pour le bouton "Filtrer"
        Button buttonFiltrer = findViewById(R.id.button_filtrer);
        buttonFiltrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metier = editTextMetier.getText().toString().trim();
                String lieu = editTextLieu.getText().toString().trim();
                String dateDebut = editTextDateDebut.getText().toString().trim();
                String dateFin = editTextDateFin.getText().toString().trim();

                List<Offre> offresFiltered = databaseHelper.getOffresFiltered(metier, lieu, dateDebut, dateFin);
                if (offresFiltered.isEmpty()) {
                    showNoOffersDialog();
                } else {
                    updateListView(offresFiltered);
                }
            }
        });
    }

    private void showNoOffersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aucune offre disponible.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // Réinitialiser la vue des offres
                        listOffres.setAdapter(null);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                // L'autorisation de localisation a été refusée
                // Afficher toutes les offres disponibles
                displayAllAds();
            }
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLocation = location;
                            displayLocationBasedAds();
                        } else {
                            // Impossible d'obtenir la localisation
                            // Afficher toutes les offres disponibles
                            displayAllAds();
                        }
                    }
                });
    }

    private void displayLocationBasedAds() {
        if (userLocation != null) {
            String userCity = getCityFromLocation(userLocation);
            Toast.makeText(this, "Ville récupérée : " + userCity, Toast.LENGTH_SHORT).show(); // Afficher la ville dans un Toast
            List<Offre> offres = databaseHelper.getOffresParLieu(userCity);
            updateListView(offres);
        } else {
            // Impossible d'obtenir la localisation
            // Afficher toutes les offres disponibles
            displayAllAds();
        }
    }

    private void displayAllAds() {
        List<Offre> offres = databaseHelper.getAllOffres();
        updateListView(offres);
    }

    private String getCityFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_offre, parent, false);
            }

            Offre offre = offres.get(position);

            TextView titreOffre = convertView.findViewById(R.id.titre_offre);
            TextView metierOffre = convertView.findViewById(R.id.metier_offre);
            TextView lieuOffre = convertView.findViewById(R.id.lieu_offre);
            ImageButton buttonPlus = convertView.findViewById(R.id.button_plus);

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

            // Formater les dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateDebutFormatted = dateFormat.format(offre.getDateDebut());
            String dateFinFormatted = dateFormat.format(offre.getDateFin());

            if (detailsVisible) {
                descriptionOffre.setText(Html.fromHtml("<b>Description :</b> " + offre.getDescription()));
                dateDebutOffre.setText(Html.fromHtml("<b>Date de début :</b> " + dateDebutFormatted));
                dateFinOffre.setText(Html.fromHtml("<b>Date de fin :</b> " + dateFinFormatted));
            }

            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDetailsVisibility(position);
                }
            });

            return convertView;
        }

        private void toggleDetailsVisibility(int position) {
            boolean detailsVisible = detailsVisibles.containsKey(position) && detailsVisibles.get(position);
            detailsVisibles.put(position, !detailsVisible);
            notifyDataSetChanged();
        }
    }
}
