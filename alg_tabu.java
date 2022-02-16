import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.awt.FileDialog;
import java.awt.Frame;

public class alg_tabu {
    public static HashMap<ArrayList<Integer>,Integer> ListaTabu=new HashMap<ArrayList<Integer>,Integer>();
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

    public static ArrayList<Integer> cloneArrayList(ArrayList<Integer> lst) {
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
        // System.out.println(m);
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

    public static ArrayList<ArrayList<Integer>> wyborsasiedztwa(
            ArrayList<ArrayList<Integer>> dane,
            ArrayList<ArrayList<Integer>> poczatkowe_rozwiazanie,
            int kadencja,
            int dlugoscListyTabu) {
        Random rand = new Random();
        
        for(int j =0;j<20;j++)
        {
            ArrayList<ArrayList<Integer>> sasiad = new ArrayList<ArrayList<Integer>>();
        for (int k = 0; k < poczatkowe_rozwiazanie.size(); k++) {
            ArrayList<Integer> rzad = new ArrayList<Integer>();
            for (int l = 0; l < poczatkowe_rozwiazanie.size(); l++) {
                rzad.add(poczatkowe_rozwiazanie.get(k).get(l));
            }
            sasiad.add(rzad);
        }
        int pracownik1 = 0, pracownik2 = 0, zadanie1 = 0, zadanie2 = 0;
        int pracownik_worse=0;
        int pracownik_worse_value=0;
            for(int row=0;row<dane.size();row++)
            {
                for(int col=0;col<dane.get(row).size();col++)
                {
                if(poczatkowe_rozwiazanie.get(row).get(col)==1)
                {
                    if(pracownik_worse_value<dane.get(row).get(col))
                    {
                        pracownik_worse=row;
                        pracownik_worse_value=dane.get(row).get(col);
                    }
                }
                }
            }
        ArrayList<Integer> ruch=new ArrayList<Integer>(Arrays.asList(pracownik1,pracownik2,zadanie1,zadanie2));
        ArrayList<Integer>ruch_odw=new ArrayList<Integer>(Arrays.asList(pracownik1,pracownik2,zadanie2,zadanie1));
        do
         {
             
            pracownik1 = pracownik_worse;
            pracownik2 = rand.nextInt(poczatkowe_rozwiazanie.size());
        
            for (int i = 0; i < sasiad.get(pracownik1).size(); i++) {
                if (sasiad.get(pracownik1).get(i) == 1) {
                    zadanie1 = i;
                }
                if (sasiad.get(pracownik2).get(i) == 1) {
                    zadanie2 = i;
                }
            }
            ruch=new ArrayList<Integer>(Arrays.asList(pracownik1,pracownik2,zadanie1,zadanie2));
            ruch_odw=new ArrayList<Integer>(Arrays.asList(pracownik1,pracownik2,zadanie2,zadanie1));
        }while (pracownik1 == pracownik2 || ListaTabu.containsKey(ruch)||ListaTabu.containsKey(ruch_odw));
        sasiad.get(pracownik1).set(zadanie1, 0);
        sasiad.get(pracownik1).set(zadanie2, 1);
        sasiad.get(pracownik2).set(zadanie2, 0);
        sasiad.get(pracownik2).set(zadanie1, 1);
        if(ocen(dane, poczatkowe_rozwiazanie)>ocen(dane, sasiad))
        {
        Set<ArrayList<Integer>> set = ListaTabu.keySet();
        for (ArrayList<Integer> key : set) {
            ListaTabu.replace(key, ListaTabu.get(key)-1);
        }
        ListaTabu.values().removeIf(value-> value==0);
        if(ListaTabu.size()==dlugoscListyTabu)
        {
            ///WARUNEK NA USUNIECIE
        }
        ListaTabu.put(ruch, kadencja);
        return sasiad;
       // System.out.println(ListaTabu);
    }
        else if(j==19)
        {
            return poczatkowe_rozwiazanie;

        }
    }
    return null;
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
    public static void algorytm_tabu(
        ArrayList<ArrayList<Integer>>dane,
        double MAX_ITER,
        int kadencja,
        int dlugoscListyTabu,
        int m
    )
    {
        ListaTabu.clear();
        ArrayList<ArrayList<Integer>> Vc=poczatkowe_rozw(m);
        ArrayList<ArrayList<Integer>> Best=new ArrayList<ArrayList<Integer>>(Vc);
        for(int i =0;i<MAX_ITER;i++)
        {

            ArrayList<ArrayList<Integer>>Vn=wyborsasiedztwa(dane, Vc, kadencja,dlugoscListyTabu);
            Vc=new ArrayList<ArrayList<Integer>>(Vn);
            if(ocen(dane, Vn)<ocen(dane, Best))
            {
                Best=new ArrayList<ArrayList<Integer>>(Vn);
            }
            

        }
        System.out.println(ocen(dane, Best));
    }

    public static void main(String[] args) {
       // FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
       // dialog.setMode(FileDialog.LOAD);
       // dialog.setVisible(true);
       // String file = dialog.getFile();
       // System.out.println(file + " chosen.");
        ArrayList<ArrayList<Integer>> dane = odczyt_pliku("assign100.txt");
        ArrayList<Integer> params= new ArrayList<>();
        params.add(5000);
        params.add(5000);
        params.add(2000);
        params.add(2000);
        params.add(200);
        params.add(200);
        params.add(1000);
        params.add(200);
        params.add(200);
        params.add(1000);
        ArrayList<Integer> paramMax_iter=new ArrayList<Integer>();
        paramMax_iter.add(1000);
        paramMax_iter.add(2000);
        paramMax_iter.add(5000);
        for(int j=0;j<paramMax_iter.size();j++)
        {
            double MAX_ITER = paramMax_iter.get(j);
        for (int i=0;i<params.size();i+=2) {
            int kadencja=params.get(i), dlugoscListyTabu=params.get(i+1);
        for (int h = 0; h < 10; h++) {
            double T = 100;
            
            ArrayList<ArrayList<Integer>> poczatkowe_rozwiazanie = poczatkowe_rozw(dane.size());
            int ocena_pocz = ocen(dane, poczatkowe_rozwiazanie);
            Random rand = new Random();
            algorytm_tabu(dane, MAX_ITER, kadencja, dlugoscListyTabu, dane.size());
        }
        System.out.println("Kadencja: "+kadencja+", Dlugość listy "+dlugoscListyTabu+", MAX_ITER: "+MAX_ITER);
    }
}
        System.exit(0);

    }
}

// raporty dla b to wybór innego sąsiedztwa
// assign100 305
// assign500 991