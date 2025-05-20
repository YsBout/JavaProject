package model;

import java.io.File;

public class Personne {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    public Personne(String nom, String prenom, String email, String telephone, String photoPath) {
        this.nom = (nom != null) ? nom : "";
        this.prenom = (prenom != null) ? prenom : "";
        this.email = (email != null) ? email : "";
        this.telephone = (telephone != null) ? telephone : "";
    }

    // Getters (les setters restent inchangés mais pourraient aussi être protégés)
    public String getNom() { return (nom != null) ? nom : ""; }
    public String getPrenom() { return (prenom != null) ? prenom : ""; }
    public String getEmail() { return (email != null) ? email : ""; }
    public String getTelephone() { return (telephone != null) ? telephone : ""; }

    // Setters (ajout de la protection null)
    public void setNom(String nom) { this.nom = (nom != null) ? nom : ""; }
    public void setPrenom(String prenom) { this.prenom = (prenom != null) ? prenom : ""; }
    public void setEmail(String email) { this.email = (email != null) ? email : ""; }
    public void setTelephone(String telephone) { this.telephone = (telephone != null) ? telephone : ""; }

    
}