package admd.interim.employeur;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import admd.interim.R;
import admd.interim.logic.Candidat;
import admd.interim.logic.CandidatureAdapter;

public class GestionCandidatureActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCandidatures;
    private CandidatureAdapter candidatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_candidature);

        recyclerViewCandidatures = findViewById(R.id.recyclerViewCandidatures);
        recyclerViewCandidatures.setLayoutManager(new LinearLayoutManager(this));

        // Exemple de liste de candidatures non traitées (à remplacer par vos données réelles)
        List<Candidat> candidaturesNonTraitees = new ArrayList<>();
        candidaturesNonTraitees.add(new Candidat("Doe", "John", "01/01/1990", "Française", "0123456789", "john.doe@example.com", "Paris", "CV.pdf"));
        candidaturesNonTraitees.add(new Candidat("Smith", "Alice", "15/05/1985", "Américaine", "0987654321", "alice.smith@example.com", "New York", "CV.pdf"));

        candidatureAdapter = new CandidatureAdapter(candidaturesNonTraitees);
        recyclerViewCandidatures.setAdapter(candidatureAdapter);

        candidatureAdapter.setOnItemClickListener(new CandidatureAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(int position) {
                // Traitement pour accepter la candidature à la position donnée
                // Vous pouvez mettre en œuvre la logique nécessaire ici
                Candidat candidatureAcceptee = candidaturesNonTraitees.get(position);
            }

            @Override
            public void onRejectClick(int position) {
                // Traitement pour refuser la candidature à la position donnée
                // Vous pouvez mettre en œuvre la logique nécessaire ici
                Candidat candidatureRejetee = candidaturesNonTraitees.get(position);
            }

            @Override
            public void onRespondClick(int position) {
                // Traitement pour répondre à la candidature à la position donnée
                // Vous pouvez mettre en œuvre la logique nécessaire ici
                Candidat candidatureARespondre = candidaturesNonTraitees.get(position);
            }
        });
    }
}
