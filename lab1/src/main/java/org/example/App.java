package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.System.exit;

public class App
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1 - рандом матрица\n2 - своя матрица\n3 - матрица из файла");
            while (!scanner.hasNextInt()) {
                System.out.println("Пожалуйста, введите число 1, 2 или 3");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 3);


        Matrix matrix = new Matrix();
        switch (choice) {
            case 1:
                matrix.randomMatrixFromInput();
                break;
            case 2:
                matrix.readMatrixFromInput(scanner);
                break;
            case 3:
                System.out.println("Введите название файла:");
                String fileName = scanner.next();
                try {
                    Scanner fileScanner = new Scanner(new File(fileName));
                    matrix.readMatrixFromInput(fileScanner);
                    fileScanner.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден.");
                    exit(0);
                }
                break;
        }

        int permutations = matrix.gaussianElimination();
        double determinant = matrix.calculateDeterminant(permutations);

        if (Double.toString(determinant).contains("E")) {
            System.out.println(Double.toString(determinant).replace("E", "*10^")+" ");
        }else {
            System.out.println(determinant + " ");
        }
        if(abs(determinant) != 0){
            System.out.println("\nDiagonal matrix: ");
            matrix.backwardSubstitution();
            matrix.printMatrix();
            matrix.info();
        }else{
            if(matrix.checkSolution()){
                System.out.println("СЛАУ имеет бесконечное количество решений. Найден вектор (0 ... 0 | 0). ");
            }else {
                System.out.println("СЛАУ не имеет решений (несовместна). Найден вектор (0 ... 0 | x), где x!=0");
            }
            matrix.printMatrix();
        }
        int er;
        Scanner scanner1 = new Scanner(System.in);
        do {
            System.out.println("1 - exit\n2 - повтор");
            while (!scanner1.hasNextInt()) {
                System.out.println("Пожалуйста, введите число 1, 2");
                scanner1.next();
            }
            er = scanner1.nextInt();
        } while (er < 1 || er > 3);
        switch (er) {
            case 1:
                exit(0);
            case 2:
                main(new String[]{});
        }
    }
}

