package edu.mirea.rksp.pr3.task4;

import java.util.UUID;

public class FileData {
    private String type;  // Тип файла
    private int size; // Размер файла
    private UUID uuid; // Уникальный идентификатор файла
    // Конструктор класса для инициализации объекта FileData
    public FileData(String type, int size) {
        this.type = type;
        this.size = size;
        this.uuid = UUID.randomUUID(); // Генерация уникального идентификатора с использованием класса UUID
    }
    // Геттер для получения типа файла
    public String getType() {
        return type;
    }
    // Сеттер для установки типа файла
    public void setType(String type) {
        this.type = type;
    }
    // Геттер для получения размера файла
    public int getSize() {
        return size;
    }
    // Сеттер для установки размера файла
    public void setSize(int size) {
        this.size = size;
    }
    // Геттер для получения уникального идентификатора файла
    public UUID getUuid() {
        return uuid;
    }
    // Переопределение метода toString() для вывода информации об объекте в строковом виде
    @Override
    public String toString() {
        return "FileData{" +
                "type='" + type + '\'' +
                ", size=" + size +
                ", uuid=" + uuid +
                '}';
    }
}
