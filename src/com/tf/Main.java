package com.tf;
import java.util.Random;
import static java.lang.Math.*;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random();
        int a=0,b=0,c=-1,d=40,coun=0,end=10000, totalAdapt=0;
        double Pk=0.7,Pm=0.4; //wspołczynniki krzyzowania i mutacji
        int genoTypes=6;
        int nGeens =6; //liczba geniow zawsze parzysta
        int mesur =10; //czestosc pomiarów
        int genoMesur=0;
        int genoMesurL=100;
        int genoBestMesur=-1000000000;
        int bestGeneration=0;
        double [] percentDiv = new double[nGeens];
        int [] adaptation = new int[nGeens];
        int[][] gPool=new int[nGeens][genoTypes];
        int[][] tempgPool=new int[nGeens][genoTypes];
        int[][] gBestPool=new int [nGeens][genoTypes];

        //generator genów (Start)
        for (int i=0;i<nGeens;i++){
            System.out.print("Tworzenie Genu "+(i+1)+": ");
            for(int j=0;j<genoTypes-1;j++){
                gPool[i][j]=rand.nextInt(2);
                System.out.print(gPool[i][j]);
            }
            System.out.println();
        }
        while ( (coun<=end) && (!(genoMesur==genoMesurL))) {
            //"Obliczanie genotypu"
            for (int i = 0; i < nGeens; i++) {
                gPool[i][genoTypes-1]=0;
                for (int j = 0; j < (genoTypes - 1); j++) {
                    gPool[i][genoTypes - 1] += (int) (gPool[i][j] * pow(2, j));
                }
            }
            //obliczanie przystosowania (wynik funkcji)
            totalAdapt=0;
            for (int i = 0; i < nGeens; i++) {
                adaptation[i]=0;
                adaptation[i] = a * (int) pow(gPool[i][genoTypes - 1], 3) + b * (int) pow(gPool[i][genoTypes - 1], 2) + c * gPool[i][genoTypes - 1] + d;
                totalAdapt += adaptation[i];
                System.out.println("Poziom adaptacji Genu "+(i+1)+" wynosi: "+adaptation[i]);
            }
            //Suma przystosowania i obliczanie wartości przystosowania(%udział)
            percentDiv[0] = ((double) adaptation[0] / (double) totalAdapt) * 100;
            for (int i = 1; i < nGeens; i++) {
                percentDiv[i] = ((double) adaptation[i] / (double) totalAdapt) * 100 + percentDiv[i - 1];
            }
            //losowanie genów i kopiowanie ich
            for (int i = 0; i < nGeens; i++) {
                double generated = rand.nextDouble() * 100;
                int loop = 1;
                if (generated < percentDiv[0]) {
                    if (adaptation[0] > adaptation[i]){
                        for (int j = 0; j < genoTypes - 1; j++)
                            tempgPool[i][j] = gPool[0][j];
                    System.out.println("Gen " + (i + 1) + " -> Gen 1");
                }
                    else{
                        System.out.println("Brak zamiany Gen 1 jest słabszy lub równy od Genowi "+(i+1));
                    for (int j = 0; j < genoTypes - 1; j++)
                        tempgPool[i][j] = gPool[i][j];}
            }
                else {
                    while (!(((percentDiv[loop - 1] <= generated)) && ((generated < percentDiv[loop])))) {
                        ++loop;
                    }
                    if (adaptation[loop] > adaptation[i]) {
                        for (int j = 0; j < genoTypes - 1; j++)
                            tempgPool[i][j] = gPool[loop][j];
                            System.out.println("Gen " + (i + 1) + " -> Gen " + (loop + 1));
                    }
                    else{
                        System.out.println("Brak zamiany, Gen "+loop+" jest słabszyod lub równy Genowi "+(i+1));
                        for (int j = 0; j < genoTypes - 1; j++)
                            tempgPool[i][j] = gPool[i][j];}
                }
                }
            for (int i=0;i<nGeens;i++){
                for(int j=0;j<genoTypes-1;j++)
                    gPool[i][j]=tempgPool[i][j];
            }
            //krzyzowanie i lokusy
            for (int i=0;i<nGeens;i++){
                System.out.print("Gen "+(i+1)+": ");
                for(int j=0;j<genoTypes-1;j++)
                    System.out.print(gPool[i][j]);
                System.out.println();
            }
            for (int i = 0; i < nGeens; i += 2) {
                double generated = rand.nextDouble();
                if (generated < Pk) {
                    int lokus = rand.nextInt(4);
                    for (int j = 0; j <= lokus; j++) {
                        gPool[i][j] = tempgPool[i + 1][j];
                        gPool[i + 1][j] = tempgPool[i][j];
                    }
                    System.out.println("Pk= " + generated+ ". Krzyżowanie! Gen " + (i + 1) + " oraz Gen " + (i + 2) + " Lokus na pozycji: " + (lokus+1));
                } else {
                    System.out.println("Pk= " + generated + ". Brak krzyżowania! Gen " + (i + 1) + " oraz Gen " + (i + 2));
                }
            }
            //mutacje i lokusy
            for (int i=0;i<nGeens;i++){
                System.out.print("Gen "+(i+1)+": ");
                for(int j=0;j<genoTypes-1;j++)
                    System.out.print(gPool[i][j]);
                System.out.println();
            }
            for (int i = 0; i < nGeens; i++) {
                double generated = rand.nextDouble();
                if (generated < Pm) {
                    int lokus = rand.nextInt(4);
                    if (gPool[i][(lokus)] == 0)
                        gPool[i][(lokus)] = 1;
                    else
                        gPool[i][(lokus)] = 0;
                    System.out.println("Pm= " + generated+ " mutacja Gen " + (i + 1) + " Lokus na pozycji " + (lokus+1));
                } else
                    System.out.println("Pm= " + generated + " brak mutacji Gen " + (i + 1));
            }
            for (int i=0;i<nGeens;i++){
                System.out.print("Gen "+(i+1)+": ");
                for(int j=0;j<genoTypes-1;j++)
                    System.out.print(gPool[i][j]);
                System.out.println();
            }
            if ((coun%mesur==0)/*||(!(genoMesur==genoMesurL))*/) {
                genoMesurL=genoMesur;
                genoMesur = totalAdapt;
                if (genoMesur>genoBestMesur){
                    bestGeneration=coun;
                    genoBestMesur=genoMesur;
                    for (int i=0;i<nGeens;i++){
                        for(int j=0;j<genoTypes;j++)
                            gBestPool[i][j]=tempgPool[i][j];
                    }
                }
            }
            System.out.println("Koniec generacji "+coun+"!\n");
            System.out.println("");
            ++coun;
        }
        System.out.println("Liczba generacji: "+coun);
        System.out.println("Adaptacja ostatniej generacji: "+totalAdapt);
        System.out.println("Adaptacja najlepszej generacji: "+genoBestMesur);
        System.out.println("Geny Najlepszej generacji: ");
        for (int i=0;i<nGeens;i++){
            System.out.print("Gen "+(i+1)+": ");
            for(int j=0;j<genoTypes-1;j++)
                System.out.print(gBestPool[i][j]);
           // System.out.print(" Genotyp genu: "+gBestPool[i][genoTypes-1]);
            System.out.println();
        }

        System.out.println("Najlepsza generacja: "+bestGeneration);
    }
}
