package Projekt;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PSOv2 {
    public static int MAX_ITER = 1000;
    public static int N = 1000; // number of particles in sub-swarm inaczej iloœæ pojazdów
    public static ArrayList<ArrayList<Integer>> customers = new ArrayList<ArrayList<Integer>>();
    public static int NumberOfCustomers; // 50
    public static Double BestKnownValue; // 524.61
    public static int VehicleCapacity; // 160
    public static int xdepot; // 30
    public static int ydepot; // 40
    public static int dimension_range = 100;
    public static ArrayList<Pojazd> gbestpath = new ArrayList<Pojazd>();
    public static double gbest = Double.MAX_VALUE;
    public static Random r = new Random();
    public static ArrayList<Solution> solution = new ArrayList<Solution>();
    public static double inertia = 0.47;
    public static double W1 = 1.5 * r.nextDouble();
    public static double W2 = 2.5 * r.nextDouble();
    public static ArrayList<Double> gbest_Points = new ArrayList<Double>();

    public static double odleglosc(double x1, double y1, double x2, double y2) {
        double d1 = x1 - x2;
        double d2 = y1 - y2;
        return Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2));
    }

    public static void drukuj(ArrayList<?> sciezka) {
        System.out.println(sciezka.toString());
    }

    public static void odczyt_pliku() throws NumberFormatException, IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("C:\\Users\\Krystian\\Downloads\\c50.txt"));
        String[] line = fileReader.readLine().split(" ");
        NumberOfCustomers = Integer.parseInt(line[0]);
        BestKnownValue = Double.parseDouble(line[1]);

        VehicleCapacity = Integer.parseInt(fileReader.readLine());

        line = fileReader.readLine().split(" ");

        xdepot = Integer.parseInt(line[0]);
        ydepot = Integer.parseInt(line[1]);

        System.out.println("Liczba klientów: " + NumberOfCustomers);
        System.out.println("Najlepszy wynik: " + BestKnownValue);
        System.out.println("Pojemnosc pojazdu: " + VehicleCapacity);
        System.out.println("punkt startowy X: " + xdepot);
        System.out.println("punkt startowy Y: " + ydepot);

        ArrayList<Integer> depot = new ArrayList<Integer>();
        depot.add(xdepot);
        depot.add(ydepot);
        depot.add(Integer.MAX_VALUE);
        customers.add(depot);
        for (int i = 0; i < NumberOfCustomers; i++) {
            ArrayList<Integer> customer = new ArrayList<Integer>();
            line = fileReader.readLine().split(" ");
            for (int k = 2; k < line.length; k++) {
                customer.add(Integer.parseInt(line[k]));
            }
            customers.add(customer);

            // drukuj(customer);
        }
        // System.out.println(customers.toString());
    }

    public static double funkcja(ArrayList<Integer> punkty) {
        double suma = 0.0;
        for (Integer integer = 0; integer < punkty.size() - 1; integer++) {
            suma += odleglosc(customers.get(punkty.get(integer)).get(0), customers.get(punkty.get(integer)).get(1),
                    customers.get(punkty.get(integer + 1)).get(0), customers.get(punkty.get(integer + 1)).get(1));
        }
        return suma;
    }

    public static void init() {
        ArrayList<Solution> particles = new ArrayList<Solution>();
        for (int k = 0; k < N; k++) {
            ArrayList<Integer> lottery_list = new ArrayList<Integer>();
            for (int j = 1; j < customers.size(); j++) {
                lottery_list.add(j);
            }

            Collections.shuffle(lottery_list);
            //System.out.println(lottery_list.toString());
            ArrayList<Pojazd> listaPojazdow = new ArrayList<Pojazd>();
            ArrayList<Integer> punkty = new ArrayList<Integer>();
            int capacity = 0;
            for (int i = 0; i < customers.size() - 1; i++) {
                if (VehicleCapacity - (capacity + customers.get(lottery_list.get(i)).get(2)) < 0) {
                    punkty.add(0, 0);
                    punkty.add(punkty.size(), 0);

                    Double odleglosc = funkcja(punkty);
                    Pojazd pojazd = new Pojazd(punkty, VehicleCapacity - capacity, odleglosc);
                    listaPojazdow.add(pojazd);
                    punkty = new ArrayList<Integer>();
                    capacity = 0;
                    punkty.add(lottery_list.get(i));
                    capacity += customers.get(lottery_list.get(i)).get(2);
                } else {
                    punkty.add(lottery_list.get(i));
                    capacity += customers.get(lottery_list.get(i)).get(2);
                }
                if (i == customers.size() - 2) {
                    if (VehicleCapacity - (capacity + customers.get(lottery_list.get(i)).get(2)) < 0) {
                        punkty.add(0, 0);
                        punkty.add(punkty.size(), 0);

                        Double odleglosc = funkcja(punkty);
                        Pojazd pojazd = new Pojazd(punkty, VehicleCapacity - capacity, odleglosc);
                        listaPojazdow.add(pojazd);
                        punkty = new ArrayList<Integer>();
                        capacity = 0;
                        punkty.add(lottery_list.get(i));
                        capacity += customers.get(lottery_list.get(i)).get(2);
                        punkty.add(0, 0);
                        punkty.add(punkty.size(), 0);
                        odleglosc = funkcja(punkty);
                        pojazd = new Pojazd(punkty, VehicleCapacity - capacity, odleglosc);
                        listaPojazdow.add(pojazd);
                    } else {

                        capacity += customers.get(lottery_list.get(i)).get(2);
                        punkty.add(0, 0);
                        punkty.add(punkty.size(), 0);
                        Double odleglosc = funkcja(punkty);
                        Pojazd pojazd = new Pojazd(punkty, VehicleCapacity - capacity, odleglosc);
                        listaPojazdow.add(pojazd);
                    }
                    break;
                }
            }
            //System.out.println(listaPojazdow.toString());
            //for (Pojazd pojazd : listaPojazdow) {
            //    System.out.println(pojazd.getPunkty().toString());
            //}
            Solution particle = new Solution();
            particle.setListaPojazd(listaPojazdow);
            double suma = 0.0;
            for (Pojazd pojazd : listaPojazdow) {
                suma += pojazd.getDistance();

            }
            particle.setDlugosc_trasy(suma);
            particle.setListaPbest(listaPojazdow);
            particle.setPbest(suma);
            double x = 0 + (dimension_range - 0) * r.nextDouble();
            double y = 0 + (dimension_range - 0) * r.nextDouble();
            ArrayList<Double> location = new ArrayList<>();
            location.add(x);
            location.add(y);
            particle.setPunkty(location);
            double xVector = 0.0;
            double yVector = 0.0;
            ArrayList<Double> vector = new ArrayList<>();
            vector.add(xVector);
            vector.add(yVector);
            particle.setVektor(vector);
            particle.setPunkty_Pbest(location);
            if (suma < gbest) {
                gbest = suma;
                gbestpath = listaPojazdow;
                gbest_Points = location;
            }

            particles.add(particle);

        }
        solution = particles;
    }

    public static ArrayList<Pojazd> TOE(ArrayList<Pojazd> particle_list) {
        // System.out.println(particle_list.toString());
        ArrayList<Integer> Vehicle_Lottery = new ArrayList<>();
        for (int i = 0; i < particle_list.size(); i++) {
            Vehicle_Lottery.add(i);
        }
        while (true) {
            int pojazd1 = 0;
            int pojazd2 = 0;
            while (pojazd1 == pojazd2) {
                pojazd1 = Vehicle_Lottery.get(r.nextInt(Vehicle_Lottery.size() - 1));
                pojazd2 = Vehicle_Lottery.get(r.nextInt(Vehicle_Lottery.size() - 1));
            }
            int miasto1 = 0;
            int miasto2 = 0;
            while (miasto1 == miasto2 || miasto1 == 0 || miasto2 == 0) {
                miasto1 = particle_list.get(pojazd1).getPunkty()
                        .get(r.nextInt(particle_list.get(pojazd1).getPunkty().size() - 1));
                miasto2 = particle_list.get(pojazd2).getPunkty()
                        .get(r.nextInt(particle_list.get(pojazd2).getPunkty().size() - 1));
            }
            if (particle_list.get(pojazd1).getCapacity() + customers.get(miasto1).get(2)
                    - customers.get(miasto2).get(2) >= 0 &&
                    particle_list.get(pojazd2).getCapacity() + customers.get(miasto2).get(2)
                            - customers.get(miasto1).get(2) >= 0) {
                // System.out.println("zamienione miasta: "+miasto1+" i "+miasto2);
                int indek1 = particle_list.get(pojazd1).getPunkty().indexOf(miasto1);
                int indek2 = particle_list.get(pojazd2).getPunkty().indexOf(miasto2);
                particle_list.get(pojazd1).setCapacity(particle_list.get(pojazd1).getCapacity()
                        + customers.get(miasto1).get(2) - customers.get(miasto2).get(2));
                particle_list.get(pojazd2).setCapacity(particle_list.get(pojazd2).getCapacity()
                        + customers.get(miasto2).get(2) - customers.get(miasto1).get(2));
                particle_list.get(pojazd1).getPunkty().set(indek1, miasto2);
                particle_list.get(pojazd2).getPunkty().set(indek2, miasto1);
                particle_list.get(pojazd1).setDistance(funkcja(particle_list.get(pojazd1).getPunkty()));
                particle_list.get(pojazd2).setDistance(funkcja(particle_list.get(pojazd2).getPunkty()));
                // System.out.println(particle_list.toString());
                break;
            }
        }
        return particle_list;
    }

    public static ArrayList<Pojazd> TOI(ArrayList<Pojazd> particle_list) {
        // System.out.println(particle_list.toString());
        ArrayList<Integer> Vehicle_Lottery = new ArrayList<>();
        for (int i = 0; i < particle_list.size(); i++) {
            Vehicle_Lottery.add(i);
        }
        int ile = 0;
        while (true) {
            int pojazd1 = 0;
            int pojazd2 = 0;
            while (pojazd1 == pojazd2) {
                pojazd1 = Vehicle_Lottery.get(r.nextInt(Vehicle_Lottery.size() - 1));
                pojazd2 = Vehicle_Lottery.get(r.nextInt(Vehicle_Lottery.size() - 1));
            }
            int miasto1 = particle_list.get(pojazd1).getPunkty()
                    .get(r.nextInt(particle_list.get(pojazd1).getPunkty().size() - 1));
            if (particle_list.get(pojazd2).getCapacity() - customers.get(miasto1).get(2) >= 0) {
                Double smallestDistance = Double.MAX_VALUE;
                int smallestdistancePoint = 0;
                for (int i = 0; i < particle_list.get(pojazd2).getPunkty().size() - 1; i++) {
                    if (odleglosc(customers.get(miasto1).get(0), customers.get(miasto1).get(1),
                            customers.get(particle_list.get(pojazd2).getPunkty().get(i)).get(0),
                            customers.get(particle_list.get(pojazd2).getPunkty().get(i)).get(1)) < smallestDistance) {
                        smallestDistance = odleglosc(customers.get(miasto1).get(0), customers.get(miasto1).get(1),
                                customers.get(particle_list.get(pojazd2).getPunkty().get(i)).get(0),
                                customers.get(particle_list.get(pojazd2).getPunkty().get(i)).get(1));
                        smallestdistancePoint = i;
                    }
                }
                particle_list.get(pojazd1).getPunkty().remove(particle_list.get(pojazd1).getPunkty().indexOf(miasto1));
                particle_list.get(pojazd1)
                        .setCapacity(particle_list.get(pojazd1).getCapacity() + customers.get(miasto1).get(2));
                particle_list.get(pojazd2).getPunkty().add(smallestdistancePoint + 1, miasto1);
                particle_list.get(pojazd2)
                        .setCapacity(particle_list.get(pojazd2).getCapacity() - customers.get(miasto1).get(2));
                particle_list.get(pojazd1).setDistance(funkcja(particle_list.get(pojazd1).getPunkty()));
                particle_list.get(pojazd2).setDistance(funkcja(particle_list.get(pojazd2).getPunkty()));
                // System.out.println("Miasto: " + miasto1 + " z pojazdu: " + pojazd1 + " do
                // pojazdu: " + pojazd2);
                // System.out.println(particle_list.toString());
                ArrayList<Integer> pusta_lista = new ArrayList<>();
                pusta_lista.add(0);
                pusta_lista.add(0);
                for (Pojazd pojazd : particle_list) {
                    if (pojazd.getPunkty().equals(pusta_lista)) {
                        particle_list.remove(pojazd);
                    }

                }
                break;
            }
            ile++;
            if(ile == 200) {
            	break;
            }
        }
        return particle_list;
    }

    public static ArrayList<Pojazd> TSPOE(ArrayList<Pojazd> particle_list) {
        // System.out.println("STARE ROZIWAZANIE");
        // System.out.println(particle_list.toString());
        for (int i = 0; i < particle_list.size(); i++) {
            ArrayList<Integer> punktyPojazdu = new ArrayList<>();
            for (int j = 1; j < particle_list.get(i).getPunkty().size() - 1; j++) {
                punktyPojazdu.add(particle_list.get(i).getPunkty().get(j));
            }
            ArrayList<Integer> rozwiazaniePojazdu = new ArrayList<>();
            rozwiazaniePojazdu.add(0);

            for (int j = 0; j < punktyPojazdu.size(); j++) {
                Double smallestdistance = Double.MAX_VALUE;
                int point = 0;
                for (int k = 0; k < punktyPojazdu.size(); k++) {
                    if (!rozwiazaniePojazdu.contains(punktyPojazdu.get(k))) {
                        int x = customers.get(rozwiazaniePojazdu.get(j)).get(0);
                        int y = customers.get(rozwiazaniePojazdu.get(j)).get(1);
                        int x2 = customers.get(punktyPojazdu.get(k)).get(0);
                        int y2 = customers.get(punktyPojazdu.get(k)).get(1);
                        Double odleglosc = odleglosc(x, y, x2, y2);
                        if (smallestdistance > odleglosc) {
                            smallestdistance = odleglosc;
                            point = punktyPojazdu.get(k);
                        }
                    }
                }
                rozwiazaniePojazdu.add(point);
            }
            rozwiazaniePojazdu.add(0);
            particle_list.get(i).setPunkty(rozwiazaniePojazdu);
            particle_list.get(i).setDistance(funkcja(particle_list.get(i).getPunkty()));

        }
        // System.out.println(particle_list.toString());
        return particle_list;
    }

    public static void alg() {
        for (int i = 0; i < MAX_ITER; i++) {
            for (Solution particleSolution : solution) {
                Double particle_x = particleSolution.getPunkty().get(0);
                Double particle_y = particleSolution.getPunkty().get(1);
                Double particle_x_Pbest = particleSolution.getPunkty_Pbest().get(0);
                Double particle_y_Pbest = particleSolution.getPunkty_Pbest().get(1);
                Double particle_xVelocity = particleSolution.getVektor().get(0);
                Double particle_yVelocity = particleSolution.getVektor().get(1);
                Double new_particle_xVelocity = inertia * particle_xVelocity + (W1 * (particle_x_Pbest - particle_x))
                        + (W2 * (gbest_Points.get(0) - particle_x));
                Double new_particle_yVelocity = inertia * particle_yVelocity + (W1 * (particle_y_Pbest - particle_y))
                        + (W2 * (gbest_Points.get(1) - particle_y));
                Double new_particle_X = new_particle_xVelocity + particle_x;
                Double new_particle_Y = new_particle_yVelocity + particle_y;
                ArrayList<Double> newPoints = new ArrayList<Double>();
                newPoints.add(new_particle_X);
                newPoints.add(new_particle_Y);
                Solution closesSolution = new Solution();
                Double closest_distance = Double.MAX_VALUE;
                for (Solution particle : solution) {
                    if (particle.equals(particleSolution))
                        continue;
                    if (closest_distance > odleglosc(particle.getPunkty().get(0), particle.getPunkty().get(1),
                            new_particle_X, new_particle_Y)) {
                        closest_distance = odleglosc(particle.getPunkty().get(0), particle.getPunkty().get(1),
                                new_particle_X, new_particle_Y);
                        closesSolution = particle;
                    }
                }
                ArrayList<Pojazd> solution_neighbour = TOE(closesSolution.listaPojazd);
                solution_neighbour = TOI(closesSolution.listaPojazd);
                solution_neighbour = TSPOE(closesSolution.listaPojazd);
                double distance_current = 0;
                double distance_of_neighbour = 0;
                for (Pojazd pojazd : closesSolution.getListaPojazd()) {
                    distance_current += funkcja(pojazd.getPunkty());
                }
                for (Pojazd pojazd : solution_neighbour) {
                    distance_of_neighbour += funkcja(pojazd.getPunkty());
                }
                if (distance_current > distance_of_neighbour) {
                    closesSolution.setListaPojazd(solution_neighbour);
                    closesSolution.setPunkty(newPoints);
                    closesSolution.setDlugosc_trasy(distance_of_neighbour);
                }
            }
            for (Solution particle : solution) {
                Double odleglosc_trasy = 0.0;
                for (Pojazd pojazd : particle.getListaPojazd()) {
                    odleglosc_trasy += pojazd.getDistance();
                }
                particle.setDlugosc_trasy(odleglosc_trasy);
                if (particle.getPbest() > particle.getDlugosc_trasy()) {
                    particle.setPbest(particle.getDlugosc_trasy());
                    particle.setListaPbest(particle.getListaPojazd());
                    particle.setPunkty_Pbest(particle.getPunkty());
                }
                if (particle.getDlugosc_trasy() < gbest) {

                    double suma = 0;
                    for (Pojazd pojazd : particle.getListaPojazd()) {
                        suma += funkcja(pojazd.getPunkty());
                    }
                    gbest = suma;
                    gbest_Points = particle.getPunkty();
                    gbestpath = particle.getListaPojazd();
                }
            }

        }
        System.out.println("GBEST: " + gbest);
        System.out.println(gbestpath.toString());
        System.out.println();
    } 

    public static void main(String[] args) throws NumberFormatException, IOException {
        odczyt_pliku();
        init();
        alg();
    }

}