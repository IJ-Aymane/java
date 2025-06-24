package com.example.javafx_ghilani.model;

public class Eleve {
    private int id;
    private String code;
    private String nom;
    private String prenom;
    private String email;
    private int annee;
    private int niveau;
    private String filiere;

    // Champs manquants
    private int age;
    private String codeFil;

    public Eleve(int id, String code, String nom, String prenom, String email, int annee, int niveau, String filiere) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.annee = annee;
        this.niveau = niveau;
        this.filiere = filiere;
        this.codeFil = filiere; // si codeFil est identique à filiere
        this.age = 0; // valeur par défaut
    }

    // Getters
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public int getAnnee() { return annee; }
    public int getNiveau() { return niveau; }
    public String getFiliere() { return filiere; }
    public int getAge() { return age; }
    public String getCodeFil() { return codeFil; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setAnnee(int annee) { this.annee = annee; }
    public void setNiveau(int niveau) { this.niveau = niveau; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public void setAge(int age) { this.age = age; }
    public void setCodeFil(String codeFil) { this.codeFil = codeFil; }
}
