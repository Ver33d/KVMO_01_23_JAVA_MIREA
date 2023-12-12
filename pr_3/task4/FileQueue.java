package edu.mirea.rksp.pr3.task4;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FileQueue implements Observer<FileData>, ObservableOnSubscribe<FileData> {
    private final int LENGTH_QUEUE; // Максимальная длина очереди
    private final ArrayList<FileData> fileDataArrayList; // Список для хранения данных о файлах в очереди
    // Конструктор класса для инициализации максимальной длины очереди
    public FileQueue(int LENGTH_QUEUE) {
        this.LENGTH_QUEUE = LENGTH_QUEUE;
        this.fileDataArrayList = new ArrayList<>();
    }
     // Метод интерфейса ObservableOnSubscribe, выполняющий подписку на Observable
    @Override
    public void subscribe(@NonNull ObservableEmitter<FileData> emitter) throws Throwable {
        // Создание таймера для периодической проверки и передачи файлов из очереди в Observable
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() // Передача первого файла из очереди в Observable, если очередь не пуста
            {
                if (!fileDataArrayList.isEmpty()) {
                    emitter.onNext(fileDataArrayList.remove(0));
                }
            }
        };
        // Запуск таймера с интервалом 10 миллисекунд
        timer.schedule(task, 0, 10);
    }
    // Метод интерфейса Observer, вызываемый при подписке на Observable
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println("onSubscribe " + this.getClass());
    }
    // Метод интерфейса Observer, вызываемый при получении нового элемента от Observable
    @Override
    public void onNext(@NonNull FileData fileData) {
        if (fileDataArrayList.size() < LENGTH_QUEUE) {
            fileDataArrayList.add(fileData);
        }
    }
    // Метод интерфейса Observer, вызываемый при возникновении ошибки в Observable
    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }
     // Метод интерфейса Observer, вызываемый при завершении работы Observable
    @Override
    public void onComplete() {
        System.out.println("onComplete " + this.getClass());
    }
}
