# Задание 3
Реализовать класс UserFriend. Поля — int userId, friendId. Заполнить массив
объектов UserFriend случайными данными.

Реализовать функцию: Observable<UserFriend> getFriends(int userId),
возвращающую поток объектов UserFriend, по заданному userId. (Для
формирования потока из массива возможно использование функции
Observable.fromArray(T[] arr)).

Дан массив из случайных userId. Сформировать поток из этого массива.
Преобразовать данный поток в поток объектов UserFriend. Обязательно
получение UserFriend через функцию getFriends.


1. Класс Main (Main.java)

Класс Main представляет пример использования RxJava для обработки данных о связях между пользователями.

Методы:

    public static void main(String[] args): Основной метод программы, в котором:

Инициализируется список связей между пользователями с помощью initUserFriends().
Получается Observable с идентификаторами пользователей с использованием getObservableUserId().
Каждый идентификатор преобразуется в список друзей с использованием метода getFriends(userId).
Результат выводится в консоль.

    private static Observable<Integer> getObservableUserId(): Метод генерирует Observable с случайными идентификаторами пользователей.
    private static Observable<UserFriend> getFriends(int userId): Метод возвращает Observable с друзьями пользователя по заданному идентификатору.
    private static void initUserFriends(): Метод инициализирует список связей между пользователями, генерируя случайные пары идентификаторов для пользователей, исключая случаи, когда пользователь является своим собственным другом.
    private static int getRandomUserId(): Метод генерирует случайный идентификатор пользователя в заданном диапазоне.
    private static int getRandomNumber(int min, int max): Метод возвращает случайное число в заданном диапазоне.

2. Класс UserFriend

Класс UserFriend представляет связь между двумя пользователями и реализует интерфейс для управления данными об этой связи.

Методы:

    public UserFriend(int userId, int friendId): Конструктор класса, создающий объект UserFriend с заданными идентификаторами пользователя и друга.
    public int getUserId(): Геттер для получения идентификатора пользователя.
    public void setUserId(int userId): Сеттер для установки идентификатора пользователя.
    public int getFriendId(): Геттер для получения идентификатора друга пользователя.
    public void setFriendId(int friendId): Сеттер для установки идентификатора друга пользователя.
    @Override public String toString(): Переопределение метода toString(), возвращающего строковое представление объекта.
    @Override public boolean equals(Object o): Переопределение метода equals(), сравнивающего объекты по содержимому.
    @Override public int hashCode(): Переопределение метода hashCode(), вычисляющего хеш-код объекта.