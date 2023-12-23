// Пакет, в котором находится класс
package edu.mirea.rksp.pr4;

// Импорт аннотаций для работы с Jackson JSON
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Класс представляет объект сообщения
public class Message {

    // Поле, содержащее текстовое сообщение
    public final String message;

    // Аннотированный конструктор, используемый при десериализации из JSON
    @JsonCreator
    public Message(@JsonProperty("message") String message) {
        // Присвоение значений полям объекта при создании экземпляра класса
        this.message = message;
    }
}
