package edu.mirea.rksp.pr3.task3;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    // Максимальное количество друзей у пользователя
    private static final int MAX_COUNT_USER_FRIENDS = 100;
    // Минимальный и максимальный идентификаторы пользователя
    private static final int MIN_USER_ID = 0;
    private static final int MAX_USER_ID = 9;
    // Список для хранения связей между пользователями
    private static ArrayList<UserFriend> userFriends = new ArrayList<>();

    public static void main(String[] args) {
        // Инициализация списка друзей
        initUserFriends();
         // Получение Observable с идентификаторами пользователей
        getObservableUserId()
                // Преобразование каждого идентификатора в список друзей
                .map(userId -> {
                    System.out.println("UserId: " + userId);
                    return getFriends(userId);
                })
                // Подписка на Observable и вывод списка друзей в консоль
                .subscribe(userFriendsObservable -> {
                    userFriendsObservable.forEach(System.out::println);
                });
    }
    // Получение Observable с случайными идентификаторами пользователей
    private static Observable<Integer> getObservableUserId() {
        Set<Integer> userIdsSet = new HashSet<>();
        // Генерация 10 случайных идентификаторов пользователя
        for (int i = 0; i < 10; i++) {
            userIdsSet.add(getRandomUserId());
        }
        // Преобразование множества в Observable
        return Observable.fromArray(userIdsSet.toArray(Integer[]::new));
    }
    // Получение Observable с друзьями пользователя по заданному идентификатору
    private static Observable<UserFriend> getFriends(int userId) {
        // Фильтрация списка друзей по идентификатору пользователя
        UserFriend[] arrFilteredUserFriend = userFriends
                .stream()
                .filter(userFriend -> userFriend.getUserId() == userId)
                .toArray(UserFriend[]::new);
        // Преобразование массива в Observable
        return Observable.fromArray(arrFilteredUserFriend);
    }
    // Инициализация списка друзей
    private static void initUserFriends() {
        Set<UserFriend> userFriendSet = new HashSet<>();
        // Генерация случайных связей между пользователями
        for (int i = 0; i < MAX_COUNT_USER_FRIENDS; i++) {
            int newUserId = getRandomUserId();
            int newFriendId = getRandomUserId();
            // Проверка, чтобы пользователь не был своим собственным другом
            if (newFriendId != newUserId) {
                userFriendSet.add(new UserFriend(newUserId, newFriendId));
            }
        }
        // Преобразование множества в список
        userFriends = new ArrayList<>(userFriendSet);
    }
    // Генерация случайного идентификатора пользователя
    private static int getRandomUserId() {
        return getRandomNumber(MIN_USER_ID, MAX_USER_ID);
    }
    // Генерация случайного числа в заданном диапазоне
    private static int getRandomNumber(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}
