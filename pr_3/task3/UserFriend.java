package edu.mirea.rksp.pr3.task3;

import java.util.Objects;

public class UserFriend {
    private int userId; // Идентификатор пользователя
    private int friendId; // Идентификатор друга пользователя
    // Конструктор класса для инициализации объекта UserFriend
    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    // Геттер для получения идентификатора пользователя
    public int getUserId() {
        return userId;
    }
    // Сеттер для установки идентификатора пользователя
    public void setUserId(int userId) {
        this.userId = userId;
    }
    // Геттер для получения идентификатора друга пользователя
    public int getFriendId() {
        return friendId;
    }
    // Сеттер для установки идентификатора друга пользователя
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
     // Переопределение метода toString() для вывода информации об объекте в строковом виде
    @Override
    public String toString() {
        return "UserFriend{" +
                "userId=" + userId +
                ", friendId=" + friendId +
                '}';
    }
    // Переопределение метода equals() для сравнения объектов по содержимому
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFriend that = (UserFriend) o;
        return userId == that.userId && friendId == that.friendId;
    }
    // Переопределение метода hashCode() для вычисления хеш-кода объекта
    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
