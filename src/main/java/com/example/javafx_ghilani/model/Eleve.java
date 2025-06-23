// Eleve.java
package com.example.javafx_ghilani.model;

public class Eleve {
    private int id;
    private String code;
    private String nom;
    private String prenom;
    private int niveau;
    private String codeFil;

    public Eleve() {}

    public Eleve(String code, String nom, String prenom, int niveau, String codeFil) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.niveau = niveau;
        this.codeFil = codeFil;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }

    public String getCodeFil() { return codeFil; }
    public void setCodeFil(String codeFil) { this.codeFil = codeFil; }

    @Override
    public String toString() {
        return code + " - " + nom + " " + prenom;
    }
}
