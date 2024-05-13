package admd.interim.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import admd.interim.R;

public class CandidatureAdapter extends RecyclerView.Adapter<CandidatureAdapter.ViewHolder> {

    private List<Candidat> candidatures;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
        void onRespondClick(int position);
    }

    public CandidatureAdapter(List<Candidat> candidatures) {
        this.candidatures = candidatures;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Candidat candidature = candidatures.get(position);
        holder.bind(candidature);
    }

    @Override
    public int getItemCount() {
        return candidatures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNom;
        private TextView textViewPrenom;
        private TextView textViewEmail;
        private Button buttonAccepter;
        private Button buttonRefuser;
        private Button buttonRepondre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.textViewNom);
            textViewPrenom = itemView.findViewById(R.id.textViewPrenom);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            buttonAccepter = itemView.findViewById(R.id.buttonAccepter);
            buttonRefuser = itemView.findViewById(R.id.buttonRefuser);
            buttonRepondre = itemView.findViewById(R.id.buttonRepondre);

            buttonAccepter.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAcceptClick(position);
                    }
                }
            });

            buttonRefuser.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRejectClick(position);
                    }
                }
            });

            buttonRepondre.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRespondClick(position);
                    }
                }
            });


        }



        public void bind(Candidat candidature) {
            textViewNom.setText(candidature.getNom());
            textViewPrenom.setText(candidature.getPrenom());
            textViewEmail.setText(candidature.getEmail());
        }
    }
}

