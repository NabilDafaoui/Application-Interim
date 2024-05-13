package admd.interim.logic;

import java.util.Date;

public class Candidature {
    private int id;
    private int idOffre;
    private long idCandidat;
    private String nomCandidat;
    private String prenomCandidat;
    private String emailCandidat;
    private String cvCandidat;
    private Date dateCandidat;
    private String statutCandidat;

    public Candidature(int idOffre, long idCandidat, String nomCandidat, String prenomCandidat, String emailCandidat, String cvCandidat, Date dateCandidat, String statutCandidat) {
        this.idOffre = idOffre;
        this.idCandidat = idCandidat;
        this.nomCandidat = nomCandidat;
        this.prenomCandidat = prenomCandidat;
        this.emailCandidat = emailCandidat;
        this.cvCandidat = cvCandidat;
        this.dateCandidat = dateCandidat;
        this.statutCandidat = statutCandidat;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public long getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }

    public String getNomCandidat() {
        return nomCandidat;
    }

    public void setNomCandidat(String nomCandidat) {
        this.nomCandidat = nomCandidat;
    }

    public String getPrenomCandidat() {
        return prenomCandidat;
    }

    public void setPrenomCandidat(String prenomCandidat) {
        this.prenomCandidat = prenomCandidat;
    }

    public String getEmailCandidat() {
        return emailCandidat;
    }

    public void setEmailCandidat(String emailCandidat) {
        this.emailCandidat = emailCandidat;
    }

    public String getCvCandidat() {
        return cvCandidat;
    }

    public void setCvCandidat(String cvCandidat) {
        this.cvCandidat = cvCandidat;
    }

    public Date getDateCandidat() {
        return dateCandidat;
    }

    public void setDateCandidat(Date dateCandidat) {
        this.dateCandidat = dateCandidat;
    }

    public String getStatutCandidat() {
        return statutCandidat;
    }

    public void setStatutCandidat(String statutCandidat) {
        this.statutCandidat = statutCandidat;
    }
}
