package admd.interim.employeur;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import admd.interim.R;
import admd.interim.logic.DatabaseHelper;
import admd.interim.logic.Offre;
import admd.interim.logic.OffreAdapter;

public class GestionOffreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OffreAdapter offreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_offre);

        recyclerView = findViewById(R.id.recyclerViewOffers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer les offres depuis la base de données
        List<Offre> offres = retrieveOffresFromDatabase();

        // Afficher les offres dans la RecyclerView
        offreAdapter = new OffreAdapter(offres);
        recyclerView.setAdapter(offreAdapter);
    }

    private List<Offre> retrieveOffresFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.getAllOffres();
    }
}
