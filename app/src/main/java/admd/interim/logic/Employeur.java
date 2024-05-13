package admd.interim.logic;

public class Employeur {
    private int id;
    private String nom;
    private String entreprise;

    private String numeroTelephone;
    private String adresse;
    private String liensPublic;
    private String email;
    private String password;

    // Constructeurs

    public Employeur(int id,String nom, String entreprise, String numeroTelephone, String adresse, String liensPublic,String email, String password) {
        this.id=id;
        this.nom = nom;
        this.entreprise = entreprise;
        this.numeroTelephone = numeroTelephone;
        this.adresse = adresse;
        this.liensPublic = liensPublic;
        this.email = email;
        this.password=password;
    }

    public Employeur() {

    }

    public Employeur(int id, String nom, String entreprise, String numeroTelephone, String adresse, String liensPublic, String email) {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }


    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getLiensPublic() {
        return liensPublic;
    }

    public void setLiensPublic(String liensPublic) {
        this.liensPublic = liensPublic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}