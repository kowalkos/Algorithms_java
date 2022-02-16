import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
public class alg_knapsack {

    public static HashMap<ArrayList<Integer>,Integer> ListaTabu=new HashMap<ArrayList<Integer>,Integer>();
    
    /////////////////////////OCENIANIE//////////////////////////////////
    public static double ocen(ArrayList<ArrayList<Double>>dane,ArrayList<Integer> rozw,int indeks)
    {
        double suma=0;
        for(int i=0;i<rozw.size();i++)
        {
            suma+=dane.get(rozw.get(i)).get(indeks);
        }

        return suma;
    }

    public static boolean ocen_slusznosc(double B,ArrayList<ArrayList<Double>>dane,ArrayList<Integer>rozw)
    {
        double suma=0;
        for(int i=0;i<rozw.size();i++)
        {
            suma+=dane.get(rozw.get(i)).get(1);
        }
        return suma<=B;
    }
//////////////////////////////////////////////////////////







/////////////////////ODCZYT PLIKU//////////////////////////
    public static ArrayList<ArrayList<Double>> odczyt_pliku(String filename)
    {
        String data="";
        try{
        File myObj = new File(filename);
        Scanner myReader = new Scanner(myObj);
        ArrayList<ArrayList<Double>> dane = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<String>> listawszystkichstr = new ArrayList<ArrayList<String>>();
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            List<String> tabnewline = new ArrayList<String>();
            tabnewline = Arrays.asList(data.split(" "));
            ArrayList<String> tabnew = new ArrayList<String>(tabnewline);
            listawszystkichstr.add(tabnew);
        }
        for(int i=0;i<listawszystkichstr.size();i++)
        {
            ArrayList<Double> rzad=new ArrayList<Double>();
            for(int j=0;j<listawszystkichstr.get(i).size();j++)
            {
                rzad.add(Double.parseDouble(listawszystkichstr.get(i).get(j)));
            }
            dane.add(rzad);
        }
        dane.remove(dane.size()-1);//comment this if file is for small_instances
        return dane;
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

////////////////////////////////POCZĄTKOWE ROZWIAZANIA////////////////////////////


    public static ArrayList<Integer> poczatkowev_2(ArrayList<ArrayList<Double>>dane,double B)
    {
        ArrayList<Integer> tab=new ArrayList<Integer>();
        ArrayList<ArrayList<Double>> tab_to_sort=new ArrayList<ArrayList<Double>> ();
        for(double i=0;i<dane.size();i++)
        {
            ArrayList<Double> row=new ArrayList<Double>();
            row.add(dane.get((int)i).get(0)/dane.get((int)i).get(1));
            row.add(i);
            tab_to_sort.add(row);
        }
        int n=dane.size();
        for(int i=0;i<n;i++)
        {
            for(int j=1;j<n-i;j++)
            {
                if(tab_to_sort.get(j-1).get(0)<tab_to_sort.get(j).get(0))
                {
                    Collections.swap(tab_to_sort, j, j-1);
                }
            }
        }
        double suma=0;
        for(int i=0;i<tab_to_sort.size();i++)
        {
            if(suma+dane.get(tab_to_sort.get(i).get(1).intValue()).get(1)<=B)
            {
                tab.add(tab_to_sort.get(i).get(1).intValue());
                suma+=dane.get(tab_to_sort.get(i).get(1).intValue()).get(1);
            }
        }
       // System.out.println(tab_to_sort.toString());
       // System.out.println(tab.toString());
        return tab;
    }
    public static ArrayList<Integer> poczatkowe(ArrayList<ArrayList<Double>>dane,double B)
    {
        ArrayList<Integer> tab=new ArrayList<Integer>();
        ArrayList<Integer> lista_indeksow=new ArrayList<Integer>();
        double suma=0;
        for(int i=0;i<dane.size();i++)
        {
            lista_indeksow.add(i);
        }
        Random r= new Random();
        for(int i=0;i<dane.size();i++)
        {
            int indeksRand=r.nextInt(lista_indeksow.size());
            int item=lista_indeksow.get(indeksRand);
            lista_indeksow.remove(indeksRand);
            if(suma+dane.get(item).get(1)<B)
            {
                tab.add(item);
                suma+=dane.get(item).get(1);
            }
        }
        //System.out.println(tab.toString());
        return tab;
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////WYBÓR SOMSIADOW/////////////////////////////////

    public static ArrayList<Integer> wybor_somsiad(
        ArrayList<ArrayList<Double>>dane,
        ArrayList<Integer>pocz,
        double n,double B)
    {
        Random r=new Random();
        ArrayList<Integer> indeksy=new ArrayList<Integer>();
        for(int i=0;i<dane.size();i++)
        {
            if(!pocz.contains(i))
            indeksy.add(i);
        }
        int kadencja=50;
        if(indeksy.size()<=50)
            kadencja=indeksy.size();
        int p1=0,p2=0;
        ArrayList<Integer>somsiad= new ArrayList<Integer>();
        for (Integer el : pocz) {
            somsiad.add(el);
        }
        //System.out.println(indeksy.toString());
        ArrayList<Integer> ruch;
        ArrayList<Integer> ruch_odw;
        int i=0;
        boolean zamieniono=true;
        do{
            somsiad= new ArrayList<Integer>();
            for (Integer el : pocz) {
                somsiad.add(el);
            }
            p1=pocz.get(r.nextInt(somsiad.size()));
            int p2indeks=r.nextInt(indeksy.size());
            p2=indeksy.get(p2indeks);
            somsiad.remove(somsiad.indexOf(p1));
            somsiad.add(p2);
            ruch=new ArrayList<Integer>();
            ruch.add(p1);
            ruch.add(p2);
            ruch_odw=new ArrayList<Integer>();
            ruch_odw.add(p2);
            ruch_odw.add(p1);
            i++;
            if(i==10)
            {
                zamieniono=false;
                break;
            }
        }
        while(ocen(dane, somsiad, 1)>B||
                ListaTabu.containsKey(ruch)||
                ListaTabu.containsKey(ruch_odw)||
                ocen(dane,pocz,0)>ocen(dane, somsiad, 0));
        if(zamieniono){
        Set<ArrayList<Integer>> set = ListaTabu.keySet();
        for (ArrayList<Integer> key : set) {
            ListaTabu.replace(key, ListaTabu.get(key)-1);
        }
        ListaTabu.values().removeIf(value-> value==0);
        ListaTabu.put(ruch, kadencja);
    }
    else{
        somsiad= new ArrayList<Integer>();
        for (Integer el : pocz) {
            somsiad.add(el);
        }
    }
        //System.out.println(p1+" "+ p2);
       // System.out.println(somsiad.toString());
        //System.out.println(ListaTabu.toString());
       // System.out.println(ocen(dane, somsiad,1));
        return somsiad;
    }



    public static ArrayList<Integer> wybor_somsiadv2(
        ArrayList<ArrayList<Double>>dane,
        ArrayList<Integer>pocz,
        double n,double B)
    {
        Random r=new Random();
        ArrayList<Integer> indeksy=new ArrayList<Integer>();
        for(int i=0;i<dane.size();i++)
        {
            if(!pocz.contains(i))
            indeksy.add(i);
        }
        int kadencja=10;
        if(indeksy.size()<=10)
            kadencja=indeksy.size();
        int p1=0,p2=0;
        ArrayList<Integer>somsiad= new ArrayList<Integer>();
        for (Integer el : pocz) {
            somsiad.add(el);
        }
        //System.out.println(indeksy.toString());
        ArrayList<Integer> ruch;
        ArrayList<Integer> ruch_odw;
        int i=0;
        boolean zamieniono=true;
        do{
            somsiad= new ArrayList<Integer>();
            for (Integer el : pocz) {
                somsiad.add(el);
            }
            p1=pocz.get(r.nextInt(somsiad.size()));
            int p2indeks=r.nextInt(indeksy.size());
            p2=indeksy.get(p2indeks);
            somsiad.remove(somsiad.indexOf(p1));
            somsiad.add(p2);
            ruch=new ArrayList<Integer>();
            ruch.add(p1);
            ruch.add(p2);
            ruch_odw=new ArrayList<Integer>();
            ruch_odw.add(p2);
            ruch_odw.add(p1);
            i++;
            if(i==10)
            {
                zamieniono=false;
                break;
            }
        }
        while(ocen(dane, somsiad, 1)>B||
                ListaTabu.containsKey(ruch)||
                ListaTabu.containsKey(ruch_odw)||
                ocen(dane,pocz,0)>ocen(dane, somsiad, 0));
        if(zamieniono){
        for(int k=0;k<dane.size();k++)
        {
            if(!somsiad.contains(k)&&ocen(dane, somsiad, 1)+dane.get(k).get(1)<B)
            {
                somsiad.add(k);
            }
        }
        Set<ArrayList<Integer>> set = ListaTabu.keySet();
        for (ArrayList<Integer> key : set) {
            ListaTabu.replace(key, ListaTabu.get(key)-1);
        }
        ListaTabu.values().removeIf(value-> value==0);
        ListaTabu.put(ruch, kadencja);
    }
    else{
        somsiad= new ArrayList<Integer>();
        for (Integer el : pocz) {
            somsiad.add(el);
        }
    }
        //System.out.println(p1+" "+ p2);
       // System.out.println(somsiad.toString());
        //System.out.println(ListaTabu.toString());
       // System.out.println(ocen(dane, somsiad,1));
        return somsiad;
    }
     /////////////////////////////////////////////////////////////////////////////



    //////////////////////////////ALGORYTMY///////////////////////////////////


    public static void algorytm_v1(ArrayList<ArrayList<Double>>dane,int MAX_ITER,double n,double B)
    {
        ListaTabu.clear();
        ArrayList<Integer> vc=poczatkowev_2(dane, B);
        ArrayList<Integer> best=new ArrayList<Integer>();
        for (Integer el : vc) {
            best.add(el);
        }
        for(int i=0;i<MAX_ITER;i++)
        {
            ArrayList<Integer> vn=wybor_somsiadv2(dane,vc,n,B);
            vc.clear();;
            for (Integer el : vn) {
                vc.add(el);
            }
            if(ocen(dane,vn,0)>ocen(dane, best,0))
            {
                best.clear();
                for (Integer el : vn) {
                    best.add(el);
                }
               // System.out.println("alg");
            }
           // System.out.println(i);
        }
        System.out.println(ocen(dane,best,0));
       // System.out.println(best.toString());
    }
      /////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        File dir = new File("large_instances");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
            // Do something with
                System.out.println("\n"+"large_instances/"+child.getName());
                ArrayList<ArrayList<Double>>dane=odczyt_pliku("large_instances/"+child.getName());
                double n=dane.get(0).get(0);
                double B=dane.get(0).get(1);
                dane.remove(0);
              //  for (ArrayList arrayList : dane) {
                //    System.out.println(arrayList.toString());
//
              //  }
                //poczatkowe(dane, n, B);
                // for(int i=0;i<100;i++)
                // {
                // System.out.println(ocen(dane,poczatkowe(dane, n, B),1));
                // }
                for(int i=0;i<10;i++)
                algorytm_v1(dane, 10000, n, B);
                //poczatkowev_2(dane, B);
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

}
