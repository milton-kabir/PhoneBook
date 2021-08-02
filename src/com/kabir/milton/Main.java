//package phonebook;
package com.kabir.milton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static HashMap<String, String> myMap=new HashMap<>();
    static  ArrayList<String> name=new ArrayList<>();
    static  ArrayList<String> fnd=new ArrayList<>();
    static long matches = 0, start, end,min,sec,msec;
    public static void main(String[] args) {
        start = System.currentTimeMillis();
        gatheringData();
        linearSearching();
        end = System.currentTimeMillis();
        String perf=performanceStats();
        System.out.println(perf);
        System.out.println();

        matches=0;
        System.out.println("Start searching (bubble sort + jump search)...");
        start = System.currentTimeMillis();
        bubbleSort();

        end = System.currentTimeMillis();
        perf=performanceStats();
        long bmin=min,bsec=sec,bmsec=msec;
        jumpSearch();
        end = System.currentTimeMillis();
        perf=performanceStats();
        System.out.println(perf);
        System.out.println("Sorting time: "+bmin+" min. "+bsec+" sec. "+bmsec+" ms.");
        System.out.println("Searching time: "+(min-bmin)+" min. "+(sec-bsec)+" sec. "+(msec-bmsec)+" ms.");



    }

    static void bubbleSort(){
        String temp;
        for(int i=0;i<name.size()-1;i++){
            for(int j=i+1;j< name.size();j++){
                if (name.get(j).compareTo(name.get(i)) > 0)
                {
                    temp = name.get(j);
                    name.set(j, name.get(i));
                    name.set(i, temp);
                }
            }
        }
    }

    static void jumpSearch() {
        for (var v : myMap.entrySet()) {
            if (fnd.contains(v.getKey())) {
                matches++;
            }
        }
    }

    static void linearSearching() {
        System.out.println("Start searching (linear search)...");
        for (var v : myMap.entrySet()) {
            if (fnd.contains(v.getKey())) {
                matches++;
            }
        }
    }
    static String performanceStats() {
        int searchTime = (int) (end - start);
        min = searchTime / 60000;
        sec = searchTime / 1000 % 60;
        msec = searchTime % 1000;
        return "Found "+matches+"/"+fnd.size()+" entries. Time taken: "+min+" min. "+sec+" sec. "+msec+" ms.";
    }

    static void gatheringData() {
        BufferedReader objReader = null;
        try {
            String strCurrentLine;

            objReader = new BufferedReader(new FileReader("C:\\Users\\drifter\\Downloads\\directory.txt"));

            while ((strCurrentLine = objReader.readLine()) != null) {

                String[] ar=strCurrentLine.split(" ",2);
//                System.out.println(strCurrentLine);
                myMap.put(ar[1],ar[0]);
                name.add(ar[1]);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                if (objReader != null)
                    objReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Searching Starts
        try {
            String strCurrentLine;

            objReader = new BufferedReader(new FileReader("C:\\Users\\drifter\\Downloads\\find.txt"));

            while ((strCurrentLine = objReader.readLine()) != null) {
//                System.out.println(strCurrentLine);
                fnd.add(strCurrentLine);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                if (objReader != null)
                    objReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
