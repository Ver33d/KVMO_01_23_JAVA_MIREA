package edu.mirea.rksp.pr3.task4;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Main {
    private static final int PERIOD_MULTIPLIER = 7;  // Множитель периода для обработчиков файлов

    public static void main(String[] args) {
        // Инициализация генератора файлов
        GeneratorFile generatorFile = new GeneratorFile(
                new String[]{"XML", "JSON", "XLS"},
                10, 100,
                100, 1000
        );
        // Инициализация очереди файлов
        FileQueue fileQueue = new FileQueue(5);

        FileDataHandler handlerXML = new FileDataHandler(PERIOD_MULTIPLIER, "XML");
        FileDataHandler handlerJSON = new FileDataHandler(PERIOD_MULTIPLIER, "JSON");
        FileDataHandler handlerXLS = new FileDataHandler(PERIOD_MULTIPLIER, "XLS");

        Observable.create(generatorFile).subscribe(fileQueue);

        ConnectableObservable<FileData> observableFileQueue = Observable.create(fileQueue).publish();

        observableFileQueue.subscribe(handlerXML);
        observableFileQueue.subscribe(handlerJSON);
        observableFileQueue.subscribe(handlerXLS);

        observableFileQueue.connect();
    }
}
