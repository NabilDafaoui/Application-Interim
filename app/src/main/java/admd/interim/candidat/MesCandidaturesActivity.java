package admd.interim.candidat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import admd.interim.R;
import admd.interim.logic.Candidature;
import admd.interim.logic.DatabaseHelper;

public class MesCandidaturesActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewCandidatures;
    private CandidatureAdapter candidatureAdapter;
    private TextView textViewCandidatId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidat_mes_candidatures);

        databaseHelper = new DatabaseHelper(this);

        // Récupérer l'ID du candidat depuis l'Intent
        int candidatId = getIntent().getIntExtra("candidat_id", 0);

        List<Candidature> candidatures = databaseHelper.getCandidaturesParCandidat(candidatId);

        recyclerViewCandidatures = findViewById(R.id.recyclerViewCandidatures);
        recyclerViewCandidatures.setLayoutManager(new LinearLayoutManager(this));
        candidatureAdapter = new CandidatureAdapter(candidatures);
        recyclerViewCandidatures.setAdapter(candidatureAdapter);


    }

    private class CandidatureAdapter extends RecyclerView.Adapter<CandidatureViewHolder> {
        private List<Candidature> candidatures;

        public CandidatureAdapter(List<Candidature> candidatures) {
            this.candidatures = candidatures;
        }

        @Override
        public CandidatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidat_mescandidatures, parent, false);
            return new CandidatureViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CandidatureViewHolder holder, int position) {
            Candidature candidature = candidatures.get(position);
            holder.bind(candidature);
        }

        @Override
        public int getItemCount() {
            return candidatures.size();
        }
    }

    public class CandidatureViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOffreNumero;
        private TextView textViewOffreTitre;
        private TextView textViewNomCandidat;
        private TextView textViewPrenomCandidat;
        private TextView textViewEmailCandidat;
        private TextView textViewCV;
        private TextView textViewStatut;
        private TextView textViewDate;

        public CandidatureViewHolder(View itemView) {
            super(itemView);
            textViewOffreNumero = itemView.findViewById(R.id.textViewOffreNumero);
            textViewOffreTitre = itemView.findViewById(R.id.textViewOffreTitre);
            textViewNomCandidat = itemView.findViewById(R.id.textViewNomCandidat);
            textViewPrenomCandidat = itemView.findViewById(R.id.textViewPrenomCandidat);
            textViewEmailCandidat = itemView.findViewById(R.id.textViewEmailCandidat);
            textViewCV = itemView.findViewById(R.id.textViewCV);
            textViewStatut = itemView.findViewById(R.id.textViewStatut);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }

        public void bind(Candidature candidature) {
            textViewOffreNumero.setText("Offre n°" + candidature.getIdOffre());
            textViewOffreTitre.setText(databaseHelper.getOffreParId(candidature.getIdOffre()));
            textViewNomCandidat.setText(candidature.getNomCandidat());
            textViewPrenomCandidat.setText(candidature.getPrenomCandidat());
            textViewEmailCandidat.setText(candidature.getEmailCandidat());
            textViewCV.setText(candidature.getCvCandidat());
            textViewStatut.setText(candidature.getStatutCandidat());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateFormatted = dateFormat.format(candidature.getDateCandidat());
            textViewDate.setText(dateFormatted);
        }
    }
}