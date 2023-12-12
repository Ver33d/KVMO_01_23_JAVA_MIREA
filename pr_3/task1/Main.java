package edu.mirea.rksp.pr3.task1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class Main {
    private static int MIN_TEMPERATURE_VALUE = 15;
    private static int MAX_TEMPERATURE_VALUE = 30;
    private static int MIN_CO2_VALUE = 30;
    private static int MAX_CO2_VALUE = 100;
    private static int NORM_TEMPERATURE = 25;
    private static int NORM_CO2 = 70;

    public static void main(String[] args) {
         // Создаем объекты сенсоров для температуры и уровня CO2
        Sensor sensorTemperature = new Sensor(MIN_TEMPERATURE_VALUE, MAX_TEMPERATURE_VALUE);
        Sensor sensorCO2 = new Sensor(MIN_CO2_VALUE, MAX_CO2_VALUE);
         // Создаем Observable для каждого сенсора
        Observable<Integer> observableCO2 = Observable.create(sensorCO2);
        Observable<Integer> observableTemperature = Observable.create(sensorTemperature);
        // Используем оператор join для объединения данных об уровне CO2 и температуры
        // с учетом временной задержки в 100 миллисекунд
        observableCO2.join(
                observableTemperature,
                i -> Observable.timer(100, TimeUnit.MILLISECONDS),
                i -> Observable.timer(100, TimeUnit.MILLISECONDS),
                 // Функция, которая объединяет данные в объект типа DataSensor
                (co2, temperature) -> new DataSensor(temperature, co2)
        ).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe");  // Обработка подписки на Observable
            }

            @Override
            public void onNext(@NonNull DataSensor dataSensor) {
                // Обработка полученных данных
                int temperature = dataSensor.getTemperature();
                int co2 = dataSensor.getCo2();

                String warningMessage = "";
                 // Проверка условий и формирование предупреждения
                if (temperature > NORM_TEMPERATURE && co2 > NORM_CO2) {
                    warningMessage = "ALARM!!!";
                } else if (temperature > NORM_TEMPERATURE || co2 > NORM_CO2) {
                    warningMessage = "Warning!";
                }
                 // Вывод данных и предупреждения
                System.out.println("Temperature: " + temperature + ", CO2: " + co2 + " " + warningMessage);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Обработка ошибки
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                 // Обработка завершения работы Observable
                System.out.println("onComplete");
            }
        });
    }
}
