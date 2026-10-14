import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static ComplexMatrix a = null;
    private static ComplexMatrix b = null;

    public static void main(String[] args) {
        System.out.println("Калькулятор матриц комплексных чисел");
        System.out.println("Числа вводятся без пробелов внутри: 3+4i, -2.5i, 7, i, 1-i");

        while (true) {
            System.out.println();
            System.out.println("1 - Ввести матрицу A");
            System.out.println("2 - Ввести матрицу B");
            System.out.println("3 - Показать матрицы");
            System.out.println("4 - A + B");
            System.out.println("5 - A * B");
            System.out.println("6 - A / B");
            System.out.println("7 - Транспонировать A");
            System.out.println("8 - Определитель A");
            System.out.println("0 - Выход");
            System.out.print("Выберите пункт: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        a = readMatrix("A");
                        break;
                    case "2":
                        b = readMatrix("B");
                        break;
                    case "3":
                        System.out.println("A =\n" + (a == null ? "не задана\n" : a));
                        System.out.println("B =\n" + (b == null ? "не задана\n" : b));
                        break;
                    case "4":
                        checkMatrices();
                        System.out.println("A + B =\n" + a.add(b));
                        break;
                    case "5":
                        checkMatrices();
                        System.out.println("A * B =\n" + a.multiply(b));
                        break;
                    case "6":
                        checkMatrices();
                        System.out.println("A / B =\n" + a.divide(b));
                        break;
                    case "7":
                        checkA();
                        System.out.println("A^T =\n" + a.transpose());
                        break;
                    case "8":
                        checkA();
                        System.out.println("det(A) = " + a.determinant());
                        break;
                    case "0":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Нет такого пункта");
                }
            } catch (MatrixException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String name) {
        int rows = readInt("Число строк матрицы " + name + " (1-10): ");
        int cols = readInt("Число столбцов матрицы " + name + " (1-10): ");
        ComplexMatrix m = new ComplexMatrix(rows, cols);
        System.out.println("Вводите строки матрицы, числа через пробел:");

        for (int i = 0; i < rows; i++) {
            while (true) {
                System.out.print("Строка " + (i + 1) + ": ");
                String[] parts = sc.nextLine().trim().split("\\s+");
                if (parts.length != cols) {
                    System.out.println("Нужно ввести чисел: " + cols + ". Попробуйте ещё раз.");
                    continue;
                }
                try {
                    for (int j = 0; j < cols; j++) {
                        m.set(i, j, Complex.parse(parts[j]));
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage() + ". Попробуйте ещё раз.");
                }
            }
        }
        System.out.println(name + " =\n" + m);
        return m;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= 1 && n <= 10) {
                    return n;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Введите целое число от 1 до 10");
        }
    }

    private static void checkA() throws MatrixException {
        if (a == null) {
            throw new MatrixException("сначала введите матрицу A");
        }
    }

    private static void checkMatrices() throws MatrixException {
        if (a == null || b == null) {
            throw new MatrixException("сначала введите матрицы A и B");
        }
    }
}
