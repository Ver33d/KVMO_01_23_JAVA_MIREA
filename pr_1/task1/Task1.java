import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
// Основной класс, выполняющий последовательные и многопоточные вычисления суммы элементов массива.
public class Task1 {
    public static void main(String[] args) throws Exception {
        int arrayLength = 10000; // Длина массива
        int threadCount = 10;   // Количество потоков для многопоточных вычислений
        int[] array = new int[arrayLength]; // Массив для хранения случайных чисел
        int sum = 0;           // Сумма элементов в однопоточном вычислении
        int sumThread = 0;     // Сумма элементов в многопоточных вычислениях
        int sumFork;           // Сумма элементов с использованием Fork-Join Framework
        Random rd = new Random(); // Генератор случайных чисел
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // Счетчик для ожидания завершения потоков
        SumThread[] threads = new SumThread[threadCount]; // Потоки для многопоточных вычислений

        // Генерация случайных чисел и заполнение массива
        for (int i = 0; i < array.length; i++) {
            array[i] = rd.nextInt(10);
        }

        // Последовательный расчёт
        long timeSequence = System.currentTimeMillis(); // Засечение времени начала
        long memorySeq = Runtime.getRuntime().freeMemory(); // Засечение свободной памяти
        for (int value : array) {
            sum += value;
            // Имитация задержки для демонстрации многопоточности
            Thread.sleep(1);
        }
        timeSequence = System.currentTimeMillis() - timeSequence; // Рассчет времени выполнения
        memorySeq = memorySeq - Runtime.getRuntime().freeMemory(); // Рассчет использования памяти

        // Многопоточный расчёт
        long timeThread = System.currentTimeMillis(); // Засечение времени начала
        long memoryThr = Runtime.getRuntime().totalMemory(); // Засечение общей памяти

        // Создание и запуск потоков для многопоточных вычислений
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new SumThread(countDownLatch, Arrays.copyOfRange(array, i * arrayLength / threadCount,
                (i + 1) * arrayLength / threadCount));
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < threadCount; i++) {
            sumThread += threads[i].getSum();
        }
        timeThread = System.currentTimeMillis() - timeThread; // Рассчет времени выполнения
        memoryThr = memoryThr - Runtime.getRuntime().freeMemory(); // Рассчет использования памяти

        // Создание Fork-Join пула для многопоточных вычислений
        ForkJoinPool fjp = new ForkJoinPool();
        ArraySum task = new ArraySum(array, 0, array.length);

        // Использование Fork-Join Framework для параллельных вычислений
        long timeFork = System.currentTimeMillis(); // Засечение времени начала
        long memoryFork = Runtime.getRuntime().freeMemory(); // Засечение свободной памяти
        sumFork = fjp.invoke(task);
        timeFork = System.currentTimeMillis() - timeFork; // Рассчет времени выполнения
        memoryFork = memoryFork - Runtime.getRuntime().freeMemory(); // Рассчет использования памяти

        // Вывод результатов вычислений
        System.out.println("Sum: " + sum + ". Time: " + timeSequence + ". Memory: " + memorySeq + " [Sequence]");
        System.out.println("Sum: " + sumThread + ". Time: " + timeThread + ". Memory: " + memoryThr + " [Thread]");
        System.out.println("Sum: " + sumFork + ". Time: " + timeFork + ". Memory: " + memoryFork + " [Fork]");
    }
}
// Класс SumThread для многопоточных вычислений в отдельных потоках
class SumThread extends Thread {
    private final int[] array; // Массив для вычислений
    private int sum = 0;      // Сумма элементов
    private final CountDownLatch countDownLatch; // Счетчик для ожидания завершения


    SumThread(CountDownLatch countDownLatch, int[] array){
        super();
        this.array = array;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run(){
        for (int value : this.array) {
            this.sum += value;
	    // Имитация задержки для демонстрации многопоточности
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }

    public int getSum(){
        return this.sum;
    }
}
// Класс ArraySum для многопоточных вычислений с использованием Fork-Join
class ArraySum extends RecursiveTask<Integer> {
    int[] array;
    int start, end;

    public ArraySum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 1) {
            return array[start];
        } else {
            int mid = (start + end) / 2;

            ArraySum left = new ArraySum(array, start, mid);
            ArraySum right = new ArraySum(array, mid, end);

            left.fork();
            right.fork();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return left.join() + right.join();
        }
    }
}