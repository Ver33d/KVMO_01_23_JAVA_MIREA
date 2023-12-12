# Задание 4
Реализовать следующую систему.

Файл. Имеет следующие характеристики:
1. Тип файла (например XML, JSON, XLS)
2. Размер файла — целочисленное значение от 10 до 100
   
Генератор файлов — генерирует файлы с задержкой от 100 до 1000 мс.

Очередь — получает файлы из генератора. Вместимость очереди — 5 файлов.

Обработчик файлов — получает файл из очереди. Каждый обработчик
имеет параметр — тип файла, который он может обработать. Время обработки
файла: «Размер файла*7мс».

Система должна быть реализована при помощи инструментов RxJava.


Класс FileData

Класс FileData представляет информацию о файле и реализует методы для управления его данными.

Методы:

    public FileData(String type, int size): Конструктор класса, создающий объект FileData с заданным типом и размером файла, а также уникальным идентификатором.
    public String getType(): Геттер для получения типа файла.
    public void setType(String type): Сеттер для установки типа файла.
    public int getSize(): Геттер для получения размера файла.
    public void setSize(int size): Сеттер для установки размера файла.
    public UUID getUuid(): Геттер для получения уникального идентификатора файла.
    @Override public String toString(): Переопределение метода toString() для удобного вывода информации об объекте в строковом виде.


Класс FileData

Класс FileData представляет информацию о файле и реализует методы для управления данными.


Методы:

    public FileData(String type, int size): Конструктор класса, создающий объект FileData с заданным типом и размером файла, а также уникальным идентификатором.
    public String getType(): Геттер для получения типа файла.
    public void setType(String type): Сеттер для установки типа файла.
    public int getSize(): Геттер для получения размера файла.
    public void setSize(int size): Сеттер для установки размера файла.
    public UUID getUuid(): Геттер для получения уникального идентификатора файла.
    @Override public String toString(): Переопределение метода toString() для удобного вывода информации об объекте в строковом виде.



Класс FileQueue
Класс FileQueue реализует интерфейс Observer<FileData> и ObservableOnSubscribe<FileData>. Этот класс представляет собой очередь файлов и управляет их передачей через RxJava Observable.

Методы:

    public FileQueue(int LENGTH_QUEUE): Конструктор класса для инициализации максимальной длины очереди и списка файлов.
    @Override public void subscribe(@NonNull ObservableEmitter<FileData> emitter) throws Throwable: Метод интерфейса ObservableOnSubscribe. Выполняет подписку на Observable и создает таймер для периодической проверки и передачи файлов из очереди в Observable.
    @Override public void onSubscribe(@NonNull Disposable d): Метод интерфейса Observer, вызываемый при подписке на Observable.
    @Override public void onNext(@NonNull FileData fileData): Метод интерфейса Observer, вызываемый при получении нового элемента от Observable. Добавляет файл в очередь, если ее текущая длина меньше максимальной.
    @Override public void onError(@NonNull Throwable e): Метод интерфейса Observer, вызываемый при возникновении ошибки в Observable.
    @Override public void onComplete(): Метод интерфейса Observer, вызываемый при завершении работы Observable.


Класс GeneratorFile

Методы:

    public GeneratorFile(String[] arrTypes, int MIN_SIZE, int MAX_SIZE, int MIN_PERIOD, int MAX_PERIOD): Конструктор класса для инициализации полей.
    public void subscribe(@NonNull ObservableEmitter<FileData> emitter): Реализация интерфейса ObservableOnSubscribe. Выполняет подписку на Observable и запускает генерацию файлов по расписанию.
    private void generateSchedule(@NonNull ObservableEmitter<FileData> emitter): Метод для генерации файлов по расписанию.
    private String generateType(): Генерирует случайный тип файла.
    private int generateSize(): Генерирует случайный размер файла.
    private int generatePeriod(): Генерирует случайный период между генерацией файлов.
    private int generateNumberInRange(int min, int max): Генерирует случайное число в заданном диапазоне.

Класс FileDataHandler

Методы:

    public FileDataHandler(int PERIOD_MULTIPLIER, String TYPE_FILE): Конструктор класса для инициализации полей.
    @Override public void onSubscribe(@NonNull Disposable d): Метод интерфейса Observer, вызываемый при подписке на Observable.
    @Override public void onNext(@NonNull FileData fileData): Метод интерфейса Observer, вызываемый при получении нового элемента от Observable. Обрабатывает файлы в зависимости от типа и отправляет их на обработку с заданным периодом.
    @Override public void onError(@NonNull Throwable e): Метод интерфейса Observer, вызываемый при возникновении ошибки в Observable.
    @Override public void onComplete(): Метод интерфейса Observer, вызываемый при завершении работы Observable.