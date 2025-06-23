
// Moyenne.java
package com.example.javafx_ghilani.model;

public class Moyenne {
    private int id;
    private String codeEleve;
    private String codeFil;
    private int niveau;
    private double moyenne;

    public Moyenne() {}

    public Moyenne(String codeEleve, String codeFil, int niveau, double moyenne) {
        this.codeEleve = codeEleve;
        this.codeFil = codeFil;
        this.niveau = niveau;
        this.moyenne = moyenne;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodeEleve() { return codeEleve; }
    public void setCodeEleve(String codeEleve) { this.codeEleve = codeEleve; }

    public String getCodeFil() { return codeFil; }
    public void setCodeFil(String codeFil) { this.codeFil = codeFil; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }

    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }
}
