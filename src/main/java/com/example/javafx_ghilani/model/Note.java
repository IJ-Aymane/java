

// Note.java
package com.example.javafx_ghilani.model;

public class Note {
    private int id;
    private String codeEleve;
    private String codeMat;
    private double note;

    public Note() {}

    public Note(String codeEleve, String codeMat, double note) {
        this.codeEleve = codeEleve;
        this.codeMat = codeMat;
        this.note = note;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodeEleve() { return codeEleve; }
    public void setCodeEleve(String codeEleve) { this.codeEleve = codeEleve; }

    public String getCodeMat() { return codeMat; }
    public void setCodeMat(String codeMat) { this.codeMat = codeMat; }

    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }
}
