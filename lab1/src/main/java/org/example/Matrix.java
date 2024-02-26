package org.example;


import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

public class Matrix {
    private int size;
    private double[][] matrix_a;
    private double[] matrix_b;
    private double[] matrix_old_b;
    private double[][] matrix_old_a;
    private double[] solutions;

    public Matrix() {
    }

    public void readMatrixFromInput(Scanner scanner) {
        size = scanner.nextInt();
        matrix_a = new double[size][size];
        matrix_b = new double[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < this.size + 1; j++) {
                String buffer = scanner.next();
                buffer = buffer.replaceAll(",", ".");
                double value = Double.parseDouble(buffer);
                if (j == this.size) {
                    matrix_b[i] = value;
                } else {
                    matrix_a[i][j] = value;
                }
            }
        }
        matrix_old_b = matrix_b;
        matrix_old_a = matrix_a;
        solutions = new double[size];
    }

    public void randomMatrixFromInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            size = scanner.nextInt();
        }catch (Exception e){
            System.out.println("пока");
            exit(0);
        }
        if(size>20 || size<1){
            System.out.println("Размерность неверна");
            exit(888);
        }
        matrix_a = new double[size][size];
        matrix_b = new double[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                Random random = new Random();
                double value = random.nextDouble();
                if (j == size) {
                    matrix_b[i] = value;
                } else {
                    matrix_a[i][j] = value;
                }
            }
        }
        matrix_old_b = matrix_b;
        matrix_old_a = matrix_a;
        solutions = new double[size];
        System.out.println("Рандомная матрица: ");
        printMatrix();
    }

    public void printMatrix() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (Double.toString(matrix_a[i][j]).contains("E")) {
                    System.out.print(Double.toString(matrix_a[i][j]).replace("E", "*10^")+" ");
                }else {
                    System.out.print(this.matrix_a[i][j] + " ");
                }
            }
            System.out.print("| ");
            System.out.println(this.matrix_b[i]);
        }
    }

    public int gaussianElimination() {
        int count = 0;
        for (int pivot = 0; pivot < size - 1; pivot++) {
            // Проверяем, равен ли текущий ведущий элемент нулю или почти нулю
            if (Math.abs(matrix_a[pivot][pivot]) < 1e-12) {
                // Ищем ненулевой элемент в столбце ниже текущего ведущего элемента.
                for (int row = pivot + 1; row < size; row++) {
                    if (Math.abs(matrix_a[row][pivot]) > 1e-12) {
                        count++;
                        // Обмен строками.
                        double[] temp = matrix_a[pivot];
                        matrix_a[pivot] = matrix_a[row];
                        matrix_a[row] = temp;

                        double tempB = matrix_b[pivot];
                        matrix_b[pivot] = matrix_b[row];
                        matrix_b[row] = tempB;
                        break; // Прерываем цикл после обмена.
                    }
                }
            }
            // После обмена проверяем, не стал ли элемент на диагонали ненулевым.
            if (Math.abs(matrix_a[pivot][pivot]) < 1e-12) {
                continue;
            }
            // Применяем преобразование к оставшимся строкам.
            for (int row = pivot + 1; row < size; row++) {
                double factor = matrix_a[row][pivot] / matrix_a[pivot][pivot];
                for (int column = pivot; column < size; column++) {
                    matrix_a[row][column] -= factor * matrix_a[pivot][column];
                }
                matrix_b[row] -= factor * matrix_b[pivot];
            }
        }
        return count;
    }


    public void backwardSubstitution() {
        for (int i = size - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < size; j++) {
                sum += matrix_a[i][j] * solutions[j];
            }
            solutions[i] = (matrix_b[i] - sum) / matrix_a[i][i];
        }
    }

    public double calculateDeterminant(int count) {
        double determinant = 1;
        int size = matrix_a.length;
        for (int i = 0; i < size; i++) {
            determinant *= matrix_a[i][i];
        }
        if(count%2==0){
            return determinant;
        }
        return -determinant;
    }

    public void info() {
        System.out.println("\nVector of unknowns: ");
        for (int i = 0; i < this.size; i++) {
            System.out.println(solutions[i]);
        }
        System.out.println("\nVector of residuals: ");

        for (int i = 0; i < this.size; i++) {
            double sum = 0;
            for (int j = 0; j < this.size; j++) {
                sum+= matrix_old_a[i][j]*solutions[j];
            }
            if (Double.toString(sum- matrix_old_b[i]).contains("E")) {
                System.out.println(Double.toString(sum- matrix_old_b[i]).replace("E", "*10^")+" ");
            }else {
                System.out.println(sum- matrix_old_b[i] + " ");
            }
        }
    }

    public boolean checkSolution() {
        for (int i = 0; i < size; i++) {
            boolean allZeros = true;
            for (int j = 0; j < size; j++) {
                if (matrix_a[i][j] != 0) {
                    allZeros = false;
                    break;
                }
            }
            if (allZeros && matrix_b[i] != 0) {
                return false;
            }
        }
        return true;
    }

}
