package Projekt;

import java.util.ArrayList;

public class Solution {
    ArrayList<Pojazd> listaPojazd=new ArrayList<Pojazd>();
    ArrayList<Double> punkty= new ArrayList<Double>();
    ArrayList<Double> vektor= new ArrayList<Double>();
    Double dlugosc_trasy=Double.MAX_VALUE;
    Double pbest=Double.MAX_VALUE;
    ArrayList<Pojazd> listaPbest=new ArrayList<Pojazd>();
    ArrayList<Double> punkty_Pbest= new ArrayList<Double>();

    public Solution() {
    }
    public String toString() {
        return "Solution [listaPojazd=" + listaPojazd + ", punkty=" + punkty + ", vektor=" + vektor + ", dlugosc_trasy="
                + dlugosc_trasy + "]\n";
    }

    public ArrayList<Double> getPunkty_Pbest() {
        return this.punkty_Pbest;
    }

    public void setPunkty_Pbest(ArrayList<Double> punkty_Pbest) {
        this.punkty_Pbest = punkty_Pbest;
    }

    public ArrayList<Pojazd> getListaPbest() {
        return this.listaPbest;
    }

    public void setListaPbest(ArrayList<Pojazd> listaPbest) {
        this.listaPbest = listaPbest;
    }

    public ArrayList<Pojazd> getListaPojazd() {
        return this.listaPojazd;
    }

    public void setListaPojazd(ArrayList<Pojazd> listaPojazd) {
        this.listaPojazd = listaPojazd;
    }

    public ArrayList<Double> getPunkty() {
        return this.punkty;
    }

    public void setPunkty(ArrayList<Double> punkty) {
        this.punkty = punkty;
    }

    public ArrayList<Double> getVektor() {
        return this.vektor;
    }

    public void setVektor(ArrayList<Double> vektor) {
        this.vektor = vektor;
    }

    public Double getDlugosc_trasy() {
        return this.dlugosc_trasy;
    }

    public void setDlugosc_trasy(Double dlugosc_trasy) {
        this.dlugosc_trasy = dlugosc_trasy;
    }

    public Double getPbest() {
        return this.pbest;
    }

    public void setPbest(Double pbest) {
        this.pbest = pbest;
    }

}