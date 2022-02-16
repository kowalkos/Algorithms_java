
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class mrowka2 {

    static double[][] V;
    static double[][][] E;
    static int liczbaMiast = 0;
    static ArrayList<ArrayList<Integer>> sciezkiMrowek = new ArrayList<ArrayList<Integer>>();
    static boolean[][] odwiedzone;

    public static void drukuj(ArrayList<?> sciezka) {
        System.out.print("\n[");
        for (int i = 0; i < sciezka.size(); i++) {
            System.out.print(sciezka.get(i) + " ");
        }
        System.out.print("]\n");
    }

    public static void drukuj(List<?> sciezka) {
        System.out.print("\n[");
        for (int i = 0; i < sciezka.size(); i++) {
            System.out.print(sciezka.get(i) + " ");
        }
        System.out.print("]\n");
    }

    public static double odleglosc(double[] a, double[] b) {
        double d1 = a[1] - b[1];
        double d2 = a[2] - b[2];
        return Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2));
    }

    public static double dlugoscSciezki(ArrayList<Integer> sciezka) {
        double droga = 0;
        for (int i = 0; i < sciezka.size() - 1; i++) {
            double droga1 = odleglosc(V[sciezka.get(i)], V[sciezka.get(i + 1)]);
            droga += droga1;
        }
        double droga1 = odleglosc(V[sciezka.get(0)], V[sciezka.get(sciezka.size() - 1)]);
        droga += droga1;

        return droga;
    }

    public static double dlugoscSciezki(List<Integer> sciezka) {
        double droga = 0;
        for (int i = 0; i < sciezka.size() - 1; i++) {
            double droga1 = odleglosc(V[sciezka.get(i)], V[sciezka.get(i + 1)]);
            droga += droga1;
        }
        double droga1 = odleglosc(V[sciezka.get(0)], V[sciezka.get(sciezka.size() - 1)]);
        droga += droga1;

        return droga;
    }

    public static void pobierz_dane(String plik) throws NumberFormatException, IOException {
        BufferedReader fileReader = new BufferedReader(
                new FileReader("miasta/" + plik));
        for (int i = 0; i < 6; i++) {
            String dane = fileReader.readLine();
            if (i == 3) {
                String[] stringMiasta = dane.split(" ");
                liczbaMiast = Integer.parseInt(stringMiasta[1]);
            }
        }
        V = new double[liczbaMiast + 1][3];

        for (int i = 1; i <= liczbaMiast + 1; i++) {
            String wartosci = fileReader.readLine();
            if (wartosci.equals("EOF")) {
                break;
            }
            String[] wspolrzedne = wartosci.split(" ");
            V[i][0] = Double.parseDouble(wspolrzedne[0]);
            V[i][1] = Double.parseDouble(wspolrzedne[1]);
            V[i][2] = Double.parseDouble(wspolrzedne[2]);
        }

        E = new double[liczbaMiast + 1][liczbaMiast + 1][2];
        for (int i = 1; i < liczbaMiast + 1; i++) {
            for (int j = 1; j < liczbaMiast + 1; j++) {
                E[i][j][0] = odleglosc(V[i], V[j]);
            }
        }
        odwiedzone = new boolean[liczbaMiast][liczbaMiast + 1];
    }

    public static void alg_ant() {
        sciezkiMrowek.clear();
        double alfa = 1;
        double beta = 1;
        int Q = 1;
        double P = 0.5;
        for (int i = 1; i < liczbaMiast + 1; i++) {
            for (int j = 1; j < liczbaMiast + 1; j++) {
                E[i][j][1] = 1;
            }
        }

        ArrayList<Integer> mrowka_best = null;
        double best = Double.MAX_VALUE;

        for (int MAX_ITER = 1; MAX_ITER < 1000; MAX_ITER++) {

            sciezkiMrowek.clear();

            for (int i = 0; i < liczbaMiast; i++) {
                for (int j = 1; j < liczbaMiast + 1; j++) {
                    odwiedzone[i][j] = false;
                }
            }
            for (int i = 1; i < liczbaMiast + 1; i++) {
                ArrayList<Integer> mrowka = new ArrayList<Integer>();
                mrowka.add(i);
                odwiedzone[i - 1][i] = true;
                sciezkiMrowek.add(mrowka);
            }

            double nowy_feromon[][][] = new double[liczbaMiast + 1][liczbaMiast + 1][1];
            for (int i = 1; i < liczbaMiast + 1; i++) {
                for (int j = 1; j < liczbaMiast + 1; j++) {
                    nowy_feromon[i][j][0] = 0;
                }
            }

            for (int l = 0; l < liczbaMiast - 1; l++) {
                for (int k = 0; k < liczbaMiast; k++) {
                    int i = sciezkiMrowek.get(k).get(sciezkiMrowek.get(k).size() - 1);
                    int rozmiarTablicy = liczbaMiast - l - 1;
                    double[][] probability = new double[rozmiarTablicy][3]; 
                    int g = 0;
                    for (int j = 1; j < liczbaMiast + 1; j++) {
                        if (odwiedzone[k][j] == true) {
                            continue;
                        }
                        double licznik = Math.pow(E[i][j][1], alfa) * Math.pow((1 / E[i][j][0]), beta);
                        double mianownik = 0;
                        for (int m = 1; m < liczbaMiast + 1; m++) {
                            if (odwiedzone[k][m] == true) {
                                continue;
                            }
                            mianownik += Math.pow(E[i][m][1], alfa) * Math.pow((1 / E[i][m][0]), beta);
                        }
                        double p = licznik / mianownik;
                        if (g == 0) {
                            probability[g][0] = 0;
                            probability[g][1] = p;
                            probability[g][2] = j;
                        } else {
                            probability[g][0] = probability[g - 1][1];
                            probability[g][1] = p + probability[g - 1][1];
                            probability[g][2] = j;
                        }
                        g++;
                    }
                    Random los = new Random();
                    double losowa = los.nextDouble();
                    int index = 0;
                    for (int u = 0; u < rozmiarTablicy; u++) {
                        if (losowa >= probability[u][0] && losowa < probability[u][1]) {

                            index = (int) probability[u][2];
                        }
                    }
                    odwiedzone[k][index] = true;
                    sciezkiMrowek.get(k).add(index);
                }
            }
            for (int k = 0; k < liczbaMiast; k++) {

                for (int j = 1; j < liczbaMiast; j++) {
                    int a = sciezkiMrowek.get(k).get(j - 1);
                    int b = sciezkiMrowek.get(k).get(j);

                    nowy_feromon[a][b][0] += Q / dlugoscSciezki(sciezkiMrowek.get(k));
                }
            }
            for (int i = 1; i < liczbaMiast + 1; i++) {
                for (int j = 1; j < liczbaMiast + 1; j++) {
                }
            }
            for (int i = 1; i < liczbaMiast + 1; i++) {
                for (int j = 1; j < liczbaMiast + 1; j++) {
                    E[i][j][1] = (1 - P) * E[i][j][1] + nowy_feromon[i][j][0] + nowy_feromon[j][i][0];
                }
            }

            for (int i = 1; i < liczbaMiast + 1; i++) {
                for (int j = 1; j < liczbaMiast + 1; j++) {
                }
            }
            ArrayList<Integer> l_mrowka_best = null;
            double l_best = Double.MAX_VALUE;
            for (int j = 0; j < liczbaMiast; j++) {
                sciezkiMrowek.get(j).add(sciezkiMrowek.get(j).get(0));
                if (dlugoscSciezki(sciezkiMrowek.get(j)) < l_best) {
                    l_best = dlugoscSciezki(sciezkiMrowek.get(j));
                    l_mrowka_best = new ArrayList<Integer>(sciezkiMrowek.get(j));
                }
            }

            if (dlugoscSciezki(l_mrowka_best) < best) {
                best = dlugoscSciezki(l_mrowka_best);
                mrowka_best = new ArrayList<Integer>(l_mrowka_best);
            }
        }

        System.out.println("\nNajlepszy wynik:");
        drukuj(mrowka_best);
        System.out.println(best);
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        File dir = new File("miasta");
        File[] directoryListing = dir.listFiles();
        Double b = (double) 0;
        Double a = (double) 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                pobierz_dane(child.getName());
                for (int i = 0; i < 1; i++) {
                    alg_ant();
                }
            }
            b = b + a;
        }

    }

}