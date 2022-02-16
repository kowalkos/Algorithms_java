package Projekt;
import java.util.ArrayList;

public class Pojazd {
     ArrayList<Integer> Punkty;
     Integer Capacity;
     Double distance;

    public Pojazd()
    {

    }
    @Override
    public String toString() {
        return "Pojazd [Punkty=" + Punkty + ", Capacity=" + Capacity + ", distance=" + distance + "] \n";
    }
    public Pojazd(ArrayList<Integer> lista,int Capacity,double distance) {
        this.setPunkty(lista);
        this.Capacity=Capacity;
        this.distance=distance;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public ArrayList<Integer> getPunkty() {
        return this.Punkty;
    }

    public void setPunkty(ArrayList<Integer> Punkty) {
        this.Punkty = Punkty;
    }

    public Integer getCapacity() {
        return this.Capacity;
    }

    public void setCapacity(Integer Capacity) {
        this.Capacity = Capacity;
    }

}