//package phonebook;
package com.kabir.milton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> number=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();
        ArrayList<String> fnd=new ArrayList<>();
        BufferedReader objReader = null;
        try {
            String strCurrentLine;

            objReader = new BufferedReader(new FileReader("C:\\Users\\drifter\\Downloads\\directory.txt"));

            while ((strCurrentLine = objReader.readLine()) != null) {

                String[] ar=strCurrentLine.split(" ",2);
//                System.out.println(strCurrentLine);
                number.add(ar[0]);
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
        System.out.println("Start searching...");
        long beginTime = System.currentTimeMillis();
        int cnt=0;
        for(int i=0;i<fnd.size();i++) {
            if (name.contains(fnd.get(i))) {
                cnt++;
            }
        }


        long endTime = System.currentTimeMillis();

        long diff = endTime - beginTime;
//        System.out.println(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        diff-=(minutes*1000*60);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        diff-=(seconds*1000);
//        System.out.println(minutes+" "+seconds+" "+diff);


        System.out.println("Found "+cnt+" / "+fnd.size()+" entries. Time taken: "+minutes+" min. "+seconds+" sec. "+diff+" ms.");



    }
}
