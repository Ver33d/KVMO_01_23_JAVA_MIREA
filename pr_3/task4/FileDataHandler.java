package edu.mirea.rksp.pr3.task4;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class FileDataHandler implements Observer<FileData> {
    private final int PERIOD_MULTIPLIER; // Множитель периода задержки в потоке (передается через конструктор)
    private final String TYPE_FILE;   // Тип файла, который обрабатывает данный обработчик (передается через конструктор)

    public FileDataHandler(int PERIOD_MULTIPLIER, String TYPE_FILE) {  // Конструктор класса для инициализации множителя периода и типа файла
        this.PERIOD_MULTIPLIER = PERIOD_MULTIPLIER;
        this.TYPE_FILE = TYPE_FILE;
    }
    // Метод вызывается при подписке на Observable
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println("onSubscribe " + this.getClass());
    }
     // Метод вызывается при получении нового элемента от Observable
    @Override
    public void onNext(@NonNull FileData fileData) {
        if (!fileData.getType().equals(this.TYPE_FILE)) {
            return;
        }
         // Создание нового потока для обработки файла
        Thread thread = new Thread(() -> {
            try {
                // Рассчитывается время задержки в потоке на основе размера файла и множителя периода
                long sleepTime = (long) fileData.getSize() * this.PERIOD_MULTIPLIER;
                System.out.println("sleep: " + sleepTime);
                // Задержка потока на рассчитанное время
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             // Вывод информации о файле после задержки
            System.out.println(fileData);
        });
        // Запуск потока
        thread.start();
    }
    // Метод вызывается при возникновении ошибки в Observable
    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }
     // Метод вызывается при завершении работы Observable
    @Override
    public void onComplete() {
        System.out.println("onComplete " + this.getClass());
    }
}
