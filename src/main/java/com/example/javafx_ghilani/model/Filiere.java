// Filiere.java
package com.example.javafx_ghilani.model;

public class Filiere {
    private int id;
    private String code;
    private String designation;

    public Filiere() {}

    public Filiere(String code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    @Override
    public String toString() {
        return code + " - " + designation;
    }
}
