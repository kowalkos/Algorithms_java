import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class zad2 {
    static double odleglosc(Double[] m1, Double[] m2) {
        return (Math.sqrt(Math.pow(m2[0] - m1[0], 2) + Math.pow(m2[1] - m1[1], 2)));
    }

    static double ocen(ArrayList<Integer> primarytable, ArrayList<Double[]> data) {
        double value = 0;
        for (int i = 0; i < data.size() - 1; i++) {
            Double[] m1 = { data.get(primarytable.get(i))[1], data.get(primarytable.get(i))[2] };
            Double[] m2 = { data.get(primarytable.get(i + 1))[1], data.get(primarytable.get(i + 1))[2] };
            value += odleglosc(m1, m2);
        }
        return value;
    }

    static ArrayList<Integer> newsolution(ArrayList<Integer> list, ArrayList<Double[]> data) {
        ArrayList<ArrayList<Integer>> lista_list = new ArrayList<ArrayList<Integer>>();
        for (int j = 0; j < 52; j++) {
            ArrayList<Integer> lista = new ArrayList<Integer>();
            for (int i = 0; i < list.size(); i++) {
                lista.add(list.get(i));
            }
            int ind1;
            int ind2;
            do {
                ind1 = (int) ((Math.random() * (list.size() - 0)) + 0);
                ind2 = (int) ((Math.random() * (list.size() - 0)) + 0);
            } while (ind1 == ind2);
            Collections.swap(lista, ind1, ind2);
            lista_list.add(lista);
        }
        ArrayList<Integer> min = new ArrayList<Integer>();
        for (int i = 0; i < lista_list.size(); i++) {
            if (i == 0) {
                min = lista_list.get(i);
            } else if (ocen(lista_list.get(i), data) < ocen(min, data)) {

                min.clear();
                for (int j = 0; j < lista_list.get(i).size(); j++) {
                    min.add(lista_list.get(i).get(j));
                }
            }
        }
        return min;
    }

    static ArrayList<Integer> newsolution_First(ArrayList<Integer> list, ArrayList<Double[]> data) {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            lista.add(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (j == i) {
                    continue;
                }
                Collections.swap(lista, i, j);
                if (ocen(list, data) > ocen(lista, data)) {
                    return lista;
                } else {
                    lista.clear();
                    for (int k = 0; k < list.size(); k++) {
                        lista.add(list.get(k));
                    }
                }
            }
        }
        return lista;
    }

    static ArrayList<Integer> newsolution_Best(ArrayList<Integer> list, ArrayList<Double[]> data) {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        ArrayList<Integer> min = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            lista.add(list.get(i));
            min.add(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (j == i) {
                    continue;
                }
                Collections.swap(lista, i, j);
                if (ocen(min, data) > ocen(lista, data)) {
                    min.clear();
                    for (int k = 0; k < lista.size(); k++) {
                        min.add(lista.get(k));
                    }
                } else {
                    lista.clear();
                    for (int k = 0; k < list.size(); k++) {
                        lista.add(list.get(k));
                    }
                }
            }
        }
        return min;
    }

    public static void permutacje(ArrayList<Double[]> data, ArrayList<Integer> primarytable) {
        int t = 0;
        ArrayList<Integer> best = new ArrayList<Integer>();
        for (int i = 0; i < primarytable.size(); i++) {
            best.add(primarytable.get(i));
        }
        do {
            boolean local = false;
            Collections.shuffle(primarytable);
            do {
                // ArrayList<Integer> neighbour_solution=newsolution(primarytable,data);
                // ArrayList<Integer> neighbour_solution = newsolution_First(primarytable,
                // data);
                ArrayList<Integer> neighbour_solution = newsolution_Best(primarytable, data);
                if (ocen(neighbour_solution, data) < ocen(primarytable, data)) {
                    primarytable.clear();
                    for (int i = 0; i < neighbour_solution.size(); i++) {
                        primarytable.add(neighbour_solution.get(i));
                    }
                    // System.out.println("W IFIE");
                    // System.out.println(primarytable.toString());
                    // System.out.println(ocen(primarytable, data));
                } else {
                    local = true;

                }
            } while (!local);
            t++;
            if (ocen(primarytable, data) < ocen(best, data)) {
                best.clear();
                for (int i = 0; i < primarytable.size(); i++) {
                    best.add(primarytable.get(i));
                }
            }

        } while (t < 100);
        System.out.println(best.toString());
        System.out.println(ocen(best, data));
    }

    public static void main(String[] args) {
        ArrayList<Double[]> storing = new ArrayList<Double[]>();

        File file = new File("berlin52tsp.tsp");

        try {
            Scanner sc = new Scanner(file);
            String nextValue = null;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if ("NODE_COORD_SECTION".equals(line)) {
                    while (sc.hasNextLine()) {
                        nextValue = sc.nextLine();
                        String tab[] = nextValue.trim().split(" ");
                        Double tabd[] = new Double[tab.length];
                        if (nextValue.equals("EOF"))
                            break;
                        for (int i = 0; i < tab.length; i++) {
                            tabd[i] = Double.parseDouble(tab[i]);
                        }
                        storing.add(tabd);
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
        }
        // storing.forEach(a -> { for (int i = 0; i < a.length; i++) {
        // System.out.print(a[i] + " "); } System.out.println(); });

        ArrayList<Integer> lista = new ArrayList<Integer>();
        // for (int i = 0; i < storing.size(); i++) {
        // lista.add(i);
        // }
        // System.out.println(lista.toString());
        // System.out.println(ocen(lista, storing));
        // permutacje(storing, lista);
        // int[] tab = { 27, 43, 11, 7, 1, 18, 33, 47, 39, 30, 37, 32, 22, 50, 31, 46,
        // 24, 14, 5, 16, 40, 34, 51, 9, 41, 4,
        // 8, 2, 12, 0, 36, 48, 38, 23, 15, 6, 10, 3, 20, 19, 29, 42, 35, 26, 49, 17,
        // 28, 45, 13, 25, 21, 44 };
        // lista.clear();
        // for (int i = 0; i < tab.length; i++) {
        // lista.add(tab[i]);
        // }
        // System.out.println(lista.toString());
        // System.out.println(ocen(lista, storing));
        // permutacje(storing, lista);
        int[] tabbest = { 0, 21, 48, 31, 35, 34, 33, 38, 39, 37, 36, 47, 23, 4, 14, 5, 3, 24, 45, 43, 15, 49, 19, 22,
                30, 17, 2, 18, 44, 40, 7, 9, 8, 42, 32, 50, 11, 27, 26, 25, 46, 12, 13, 51, 10, 28, 29, 20, 16, 41, 6,
                1 };
        lista.clear();
        for (int i = 0; i < tabbest.length; i++) {
            lista.add(tabbest[i]);
        }
        System.out.println(lista.toString());
        System.out.println(ocen(lista, storing));
        permutacje(storing, lista);
    }
}
