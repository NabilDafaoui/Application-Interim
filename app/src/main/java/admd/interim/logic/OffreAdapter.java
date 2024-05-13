package admd.interim.logic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import admd.interim.R;
import admd.interim.employeur.ConsulterOffreActivity;
import admd.interim.employeur.ModifierOffreActivity;

public class OffreAdapter extends RecyclerView.Adapter<OffreAdapter.OffreViewHolder> {
    private List<Offre> offres; // Complete list of offers with details

    public OffreAdapter(List<Offre> offres) {
        this.offres = offres;
    }

    @NonNull
    @Override
    public OffreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemm_offre, parent, false);
        return new OffreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffreViewHolder holder, int position) {
        Offre offre = offres.get(position);
        holder.bind(offre);
    }

    @Override
    public int getItemCount() {
        return offres.size();
    }

    static class OffreViewHolder extends RecyclerView.ViewHolder {
        private TextView textOfferTitle;
        private Button buttonConsulter;
        private Button buttonModifier;
        private Button buttonSupprimer;

        OffreViewHolder(@NonNull View itemView) {
            super(itemView);
            textOfferTitle = itemView.findViewById(R.id.textOfferTitle);
            buttonConsulter = itemView.findViewById(R.id.buttonConsulter);
            buttonModifier = itemView.findViewById(R.id.buttonModifier);
            buttonSupprimer = itemView.findViewById(R.id.buttonSupprimer);
        }

        void bind(Offre offre) {
            textOfferTitle.setText(offre.getTitre());

            buttonConsulter.setOnClickListener(v -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, ConsulterOffreActivity.class);
                intent.putExtra("offre_id", offre.getId()); // Ensure this data is used in the activity
                context.startActivity(intent);
            });


            buttonModifier.setOnClickListener(v -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, ModifierOffreActivity.class);
                intent.putExtra("offre", offre); // Ensure Offer is Parcelable or Serializable
                context.startActivity(intent);
            });

            buttonSupprimer.setOnClickListener(v -> {
                Context context = itemView.getContext();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.deleteOffre(offre.getId());
                Toast.makeText(context, "Offre supprimée: " + offre.getTitre(), Toast.LENGTH_SHORT).show();
                // Implement a method to refresh the list after deletion
                // e.g., listener.onItemDeleted(getAdapterPosition());
            });
        }
    }
}
