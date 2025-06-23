// Matiere.java
package com.example.javafx_ghilani.model;

public class Matiere {
    private int id;
    private String code;
    private String designation;
    private int vh;
    private String codeModule;

    public Matiere() {}

    public Matiere(String code, String designation, int vh, String codeModule) {
        this.code = code;
        this.designation = designation;
        this.vh = vh;
        this.codeModule = codeModule;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public int getVh() { return vh; }
    public void setVh(int vh) { this.vh = vh; }

    public String getCodeModule() { return codeModule; }
    public void setCodeModule(String codeModule) { this.codeModule = codeModule; }

    @Override
    public String toString() {
        return code + " - " + designation;
    }
}
