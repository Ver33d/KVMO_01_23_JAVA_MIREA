package edu.mirea.rksp.pr3.task4;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

import java.util.Timer;
import java.util.TimerTask;

public class GeneratorFile implements ObservableOnSubscribe<FileData> {
     // Массив типов файлов
    private final String[] arrTypes;
     // Минимальный и максимальный размер файлов
    private final int MIN_SIZE;
    private final int MAX_SIZE;
    // Минимальный и максимальный период генерации файлов
    private final int MIN_PERIOD;
    private final int MAX_PERIOD;
     // Конструктор класса для инициализации полей
    public GeneratorFile(String[] arrTypes, int MIN_SIZE, int MAX_SIZE, int MIN_PERIOD, int MAX_PERIOD) {
        this.arrTypes = arrTypes;
        this.MIN_SIZE = MIN_SIZE;
        this.MAX_SIZE = MAX_SIZE;
        this.MIN_PERIOD = MIN_PERIOD;
        this.MAX_PERIOD = MAX_PERIOD;
    }
    // Метод интерфейса ObservableOnSubscribe, выполняющий подписку на Observable
    @Override
    public void subscribe(@NonNull ObservableEmitter<FileData> emitter) {
        generateSchedule(emitter);
    }
    // Метод для генерации файлов по расписанию
    private void generateSchedule(@NonNull ObservableEmitter<FileData> emitter) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run()
            {
                emitter.onNext(new FileData(generateType(), generateSize())); // Генерация нового файла и передача его в Observable
                generateSchedule(emitter); // Рекурсивный вызов метода для продолжения генерации файлов по расписанию
            }
        };
        // Запуск задачи таймера с периодом, полученным из генерации
        timer.schedule(task, generatePeriod());
    }
     // Метод для генерации случайного типа файла
    private String generateType() {
        int index = (int) Math.floor(Math.random()  * this.arrTypes.length); // Получение случайного индекса из массива типов файлов
         // Возврат соответствующего типа файла
        return this.arrTypes[index];
    }
    // Метод для генерации случайного размера файла
    private int generateSize() {
        return generateNumberInRange(MIN_SIZE, MAX_SIZE);
    }
    // Метод для генерации случайного периода
    private int generatePeriod() {
        return generateNumberInRange(MIN_PERIOD, MAX_PERIOD);
    }
    // Метод для генерации случайного числа в заданном диапазоне
    private int generateNumberInRange(int min, int max) {
        return min + (int) Math.round(Math.random() * (max - min));
    }
}
