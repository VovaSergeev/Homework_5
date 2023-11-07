/**
 * Класс, наследующийся от Thread для вычисления определителя матрицы в
 * отдельном потоке.
 */
class MatrixThread extends Thread {
    private long[][] matrix; // поле для хранения матрицы
    private long result; // поле для хранения результата

    /**
     * Конструктор класса MatrixThread.
     *
     * @param matrix
     *               матрица, для которой будет вычисляться определитель
     */
    public MatrixThread(long[][] matrix) {
        this.matrix = matrix; // инициализация поля матрицы
    }

    /**
     * Метод для получения результата вычисления определителя матрицы.
     *
     * @return определитель матрицы
     */
    public long getResult() {
        return result;
    }

    /**
     * Метод, который будет выполняться в отдельном потоке.
     * Вычисляет определитель матрицы, используя метод detOneThread из класса Main.
     */
    @Override
    public void run() {
        result = Main.detOneThread(matrix); // используем метод из класса Main для вычисления определителя матрицы
    }
}
