package edu.mirea.rksp.pr3.task2;

import io.reactivex.rxjava3.core.Observable;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main2 {
    private static boolean isEnd = false;

    public static void main(String[] args) {
        // Создаем два RxJava Observable, генерирующих последовательности чисел
        // Observable1 генерирует случайные числа от 1 до 10 каждые 500 миллисекунд
        Observable<Integer> observable1 = Observable
                .intervalRange(1, 10, 0, 500, TimeUnit.MILLISECONDS)
                .map(i -> getRandomNumber1());
        // Observable2 генерирует случайные числа от 15 до 24 каждые 500 миллисекунд
        Observable<Integer> observable2 = Observable
                .intervalRange(1, 10, 15, 500, TimeUnit.MILLISECONDS)
                .map(i -> getRandomNumber2());
        // Объединяем два Observable в один
        Observable<Serializable> observableMain = Observable.merge(observable1, observable2);

        // Создаем счетчик CountDownLatch с начальным значением 1
        CountDownLatch latch = new CountDownLatch(1);
        // Подписываемся на Observable и выводим результаты в консоль
        observableMain.subscribe(System.out::print, System.out::print, latch::countDown);

        try {
             // Ожидаем завершения работы Observable
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     // Метод для генерации случайного числа в заданном диапазоне
    private static int getRandomNumber(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
    // Метод для генерации случайного числа в диапазоне от 0 до 0
    private static int getRandomNumber1() {
        return getRandomNumber(0, 0);
    }
     // Метод для генерации случайного числа в диапазоне от 9 до 9
    private static int getRandomNumber2() {
        return getRandomNumber(9, 9);
    }
}
