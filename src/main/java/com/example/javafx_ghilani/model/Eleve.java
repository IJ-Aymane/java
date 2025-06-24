package com.example.javafx_ghilani.model;

import javafx.beans.property.*;

public class Eleve {
    private final IntegerProperty id;
    private final StringProperty code;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final IntegerProperty niveau;
    private final StringProperty codeFil;

    public Eleve(int id, String code, String nom, String prenom, int niveau, String codeFil) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.niveau = new SimpleIntegerProperty(niveau);
        this.codeFil = new SimpleStringProperty(codeFil);
    }

    // Getters et setters JavaFX

    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getCode() { return code.get(); }
    public void setCode(String value) { code.set(value); }
    public StringProperty codeProperty() { return code; }

    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }
    public StringProperty nomProperty() { return nom; }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String value) { prenom.set(value); }
    public StringProperty prenomProperty() { return prenom; }

    public int getNiveau() { return niveau.get(); }
    public void setNiveau(int value) { niveau.set(value); }
    public IntegerProperty niveauProperty() { return niveau; }

    public String getCodeFil() { return codeFil.get(); }
    public void setCodeFil(String value) { codeFil.set(value); }
    public StringProperty codeFilProperty() { return codeFil; }
}
