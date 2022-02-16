import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.awt.FileDialog;
import java.awt.Frame;

public class wyzazanie {
    public static ArrayList<ArrayList<Integer>> odczyt_pliku(String nazwaPliku) {
        String data = "";
        try {
            File myObj = new File(nazwaPliku);
            Scanner myReader = new Scanner(myObj);
            ArrayList<ArrayList<String>> listawszystkichstr = new ArrayList<ArrayList<String>>();
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                List<String> tabnewline = new ArrayList<String>();
                tabnewline = Arrays.asList(data.split(" "));
                ArrayList<String> tabnew = new ArrayList<String>(tabnewline);
                tabnew.remove(0);
                listawszystkichstr.add(tabnew);
            }
            int m = Integer.parseInt(listawszystkichstr.get(0).get(0));
            listawszystkichstr.remove(0);
            ArrayList<ArrayList<Integer>> lista_wszystkich = new ArrayList<ArrayList<Integer>>();
            int k = 0;
            ArrayList<Integer> lista_rzad = new ArrayList<Integer>();
            for (int i = 0; i < listawszystkichstr.size(); i++) {
                for (int j = 0; j < listawszystkichstr.get(i).size(); j++) {
                    if (k < m) {
                        lista_rzad.add(Integer.parseInt(listawszystkichstr.get(i).get(j)));
                        k++;
                    }
                    if (k == m) {

                        ArrayList<Integer> copyList = new ArrayList<Integer>();
                        copyList.addAll(lista_rzad);
                        lista_wszystkich.add(cloneArrayList(lista_rzad));
                        lista_rzad.clear();
                        k = 0;
                    }
                }
            }
            myReader.close();
            return lista_wszystkich;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Integer> cloneArrayList(ArrayList lst) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < lst.size(); i++) {
            list.add((Integer) lst.get(i));
        }
        return list;
    }

    public static ArrayList<ArrayList<Integer>> poczatkowe_rozw(int m) {
        ArrayList<ArrayList<Integer>> lista = new ArrayList<ArrayList<Integer>>(m);
        ArrayList<Integer> listapracownicy = new ArrayList<Integer>();
        ArrayList<Integer> listazadania = new ArrayList<Integer>();
        Random rand = new Random();
        for (int i = 0; i < m; i++) {
            ArrayList<Integer> zapelnienie = new ArrayList<Integer>();
            for (int j = 0; j < m; j++) {
                zapelnienie.add(0);
            }
            lista.add(zapelnienie);
            listapracownicy.add(i);
            listazadania.add(i);
        }
        for (int i = 0; i < m; i++) {
            ArrayList<Integer> zapelnienie = new ArrayList<Integer>();
            int randomIndexprac = rand.nextInt(listapracownicy.size());
            int pracownik = listapracownicy.get(randomIndexprac);
            listapracownicy.remove(randomIndexprac);
            int randomIndexzad = rand.nextInt(listazadania.size());
            int zadanie = listazadania.get(randomIndexzad);
            listazadania.remove(randomIndexzad);
            for (int j = 0; j < m; j++) {
                if (j == zadanie) {
                    zapelnienie.add(1);
                } else {
                    zapelnienie.add(0);
                }
            }
            lista.set(pracownik, zapelnienie);

        }
        return lista;
    }

    public static ArrayList<ArrayList<Integer>> wyborsasiedztwa(ArrayList<ArrayList<Integer>> dane,
            ArrayList<ArrayList<Integer>> poczatkowe_rozwiazanie) {
        Random rand = new Random();
        ArrayList<ArrayList<Integer>> sasiad = new ArrayList<ArrayList<Integer>>();
        for (int k = 0; k < poczatkowe_rozwiazanie.size(); k++) {
            ArrayList<Integer> rzad = new ArrayList<Integer>();
            for (int l = 0; l < poczatkowe_rozwiazanie.size(); l++) {
                rzad.add(poczatkowe_rozwiazanie.get(k).get(l));
            }
            sasiad.add(rzad);
        }

        int pracownik1 = 0, pracownik2 = 0, zadanie1 = 0, zadanie2 = 0;
        while (pracownik1 == pracownik2) {
            pracownik1 = rand.nextInt(poczatkowe_rozwiazanie.size());
            pracownik2 = rand.nextInt(poczatkowe_rozwiazanie.size());
        }
        for (int i = 0; i < sasiad.get(pracownik1).size(); i++) {
            if (sasiad.get(pracownik1).get(i) == 1) {
                zadanie1 = i;
            }
            if (sasiad.get(pracownik2).get(i) == 1) {
                zadanie2 = i;
            }
        }
        int suma1 = 0;
        suma1 += dane.get(pracownik1).get(zadanie1) + dane.get(pracownik2).get(zadanie2);
        int suma2 = dane.get(pracownik1).get(zadanie2) + dane.get(pracownik2).get(zadanie1);
        sasiad.get(pracownik1).set(zadanie1, 0);
        sasiad.get(pracownik1).set(zadanie2, 1);
        sasiad.get(pracownik2).set(zadanie2, 0);
        sasiad.get(pracownik2).set(zadanie1, 1);
        int roznica = suma2 - suma1;
        ArrayList<Integer> listroznica = new ArrayList<Integer>();
        listroznica.add(roznica);
        sasiad.add(listroznica);
        return sasiad;
    }

    public static int ocen(ArrayList<ArrayList<Integer>> dane, ArrayList<ArrayList<Integer>> poczatkowe_rozwiazanie) {
        int suma = 0;
        for (int i = 0; i < poczatkowe_rozwiazanie.size(); i++) {
            for (int j = 0; j < poczatkowe_rozwiazanie.size(); j++) {
                if (poczatkowe_rozwiazanie.get(i).get(j) == 1) {
                    suma += dane.get(i).get(j);
                }
            }
        }
        return suma;
    }

    public static void main(String[] args) {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        System.out.println(file + " chosen.");
        ArrayList<ArrayList<Integer>> dane = odczyt_pliku(file);

        for (int h = 0; h < 10; h++) {
            double T = 100;
            double MAX_ITER = 1000;
            double N = 100;
            double alfa = 0.8;
            ArrayList<ArrayList<Integer>> poczatkowe_rozwiazanie = poczatkowe_rozw(dane.size());
            int ocena_pocz = ocen(dane, poczatkowe_rozwiazanie);
            Random rand = new Random();

            for (int i = 0; i < MAX_ITER; i++) {
                int iter = 0;
                do {
                    ArrayList<ArrayList<Integer>> sasiad = wyborsasiedztwa(dane, poczatkowe_rozwiazanie);
                    ArrayList<Integer> roznica = sasiad.remove(sasiad.size() - 1);
                    int ocena_sasiada = ocena_pocz + roznica.get(0);
                    if (ocena_pocz > ocena_sasiada) {
                        poczatkowe_rozwiazanie = new ArrayList<ArrayList<Integer>>();
                        for (int k = 0; k < sasiad.size(); k++) {
                            ArrayList<Integer> rzad = new ArrayList<Integer>();
                            for (int l = 0; l < sasiad.size(); l++) {
                                rzad.add(sasiad.get(k).get(l));
                            }
                            poczatkowe_rozwiazanie.add(rzad);
                        }

                        ocena_pocz = ocena_sasiada;
                    } else if (rand.nextDouble() < Math.exp((ocena_pocz - ocena_sasiada) / T)) {
                        poczatkowe_rozwiazanie = new ArrayList<ArrayList<Integer>>();
                        for (int k = 0; k < sasiad.size(); k++) {
                            ArrayList<Integer> rzad = new ArrayList<Integer>();
                            for (int l = 0; l < sasiad.size(); l++) {
                                rzad.add(sasiad.get(k).get(l));
                            }
                            poczatkowe_rozwiazanie.add(rzad);
                        }
                        ocena_pocz = ocena_sasiada;

                    }
                    iter++;
                } while (iter != N);
                T = alfa * T;

            }
            System.out.println(ocena_pocz);

        }
        System.exit(0);
    }
}
// raporty dla b to wybór innego sąsiedztwa
// assign100 305
// assign500 991