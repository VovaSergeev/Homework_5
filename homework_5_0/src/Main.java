import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Стартовый класс.
 */
public class Main {

    /**
     * Точка старта приложения
     *
     * @param args
     *             стартовые аргументы
     */
    public static void main(String[] args) {
        for (MatrixExample matrixExample : MatrixExample.values()) {
            // вызываем метод для вычисления определителя матрицы с использованием
            // многопоточности
            printResult("detMultiThread", matrixExample, () -> detMultiThread(matrixExample.getMatrix()));
        }
    }

    /**
     * Рекурсивный расчет определителя матрицы методом разложения по строке в один
     * поток.
     *
     * @param a
     *          матрица
     * @return определитель матрицы
     */
    static long detOneThread(long[][] a) {
        if (a.length == 1) {
            return a[0][0];
        }
        long result = 0L;
        for (int i = 0; i < a.length; i++) {
            int sign = (i % 2 == 0 ? 1 : -1);
            result = result + sign * a[i][0] * detOneThread(minor(a, i));
        }

        return result;
    }

    /**
     * Расчет определителя матрицы методом разложения по строке с использованием
     * многопоточности.
     *
     * @param a
     *          матрица
     * @return определитель матрицы
     */
    private static long detMultiThread(long[][] a) {
        if (a.length == 1) {
            return a[0][0]; // если матрица одномерная, вернуть ее единственный элемент
        }
        long result = 0L; // переменная для хранения результата
        List<Thread> threads = new ArrayList<>(); // список потоков
        List<Long> results = new ArrayList<>(); // список результатов
        for (int i = 0; i < a.length; i++) {
            Thread thread = new MatrixThread(minor(a, i)); // наследование от Thread
            threads.add(thread); // добавить поток в список
            thread.start(); // запустить поток
        }
        for (int i = 0; i < a.length; i++) {
            try {
                threads.get(i).join(); // ожидание завершения потока
                results.add(((MatrixThread) threads.get(i)).getResult()); // добавление результата в список
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < a.length; i++) {
            int sign = (i % 2 == 0 ? 1 : -1);
            result = result + sign * a[i][0] * results.get(i); // вычисление определителя
        }

        return result;
    }

    /**
     * Вычисляет минорную матрицу от заданной. Удаляется первый столбец и заданная
     * строка.
     *
     * @param original
     *                  матрица, от которой требуется вычислить минорную
     * @param exceptRow
     *                  удаляемая строка
     * @return минорная матрица
     */
    public static long[][] minor(final long[][] original, int exceptRow) {
        long[][] minor = new long[original.length - 1][original.length - 1];
        int minorLength = minor.length;
        for (int i = 0; i < exceptRow; i++) {
            System.arraycopy(original[i], 1, minor[i], 0, minorLength);
        }
        for (int i = exceptRow + 1; i < original.length; i++) {
            System.arraycopy(original[i], 1, minor[i - 1], 0, minorLength);
        }
        return minor;
    }

    /**
     * Выводит в консоль результат работы.
     *
     * @param method
     *                 название метода расчета
     * @param matrix
     *                 матрица из предложенных для примера
     * @param executor
     *                 алгоритм расчета определителя матрицы
     */
    private static void printResult(String method, MatrixExample matrix, Supplier<Long> executor) {
        long start = System.currentTimeMillis();
        Long det = executor.get();
        long executionTimeMillis = System.currentTimeMillis() - start;
        double executionTimeSeconds = executionTimeMillis / 1000.0;
        System.out.println("Method -> " + method);
        System.out.println("Matrix name -> " + matrix.name());
        System.out.println("Matrix dimension -> " + matrix.getMatrix().length);
        System.out.println("Matrix determinant  = " + det + (det != matrix.getDeterminant() ? " ERROR!" : ""));
        System.out.println("Execution time -> " + executionTimeSeconds + " seconds");
        System.out.println();
    }
}
