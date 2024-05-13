package admd.interim.logic;

import java.io.Serializable;
import java.util.Date;

public class Offre implements Serializable {
    private int id;
    private String titre;
    private String description;
    private String metier;
    private String lieu;
    private Date dateDebut;
    private Date dateFin;
    private int idEmployeur;

    // Constructeur
    public Offre(String titre, String description, String metier, String lieu, Date dateDebut, Date dateFin, int idEmployeur) {
        this.titre = titre;
        this.description = description;
        this.metier = metier;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idEmployeur = idEmployeur;
    }

    public Offre() {

    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(int idEmployeur) {
        this.idEmployeur = idEmployeur;
    }
}