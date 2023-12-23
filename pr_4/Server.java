// Пакет, в котором находится класс
package edu.mirea.rksp.pr4;

// Импорт необходимых библиотек
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.*;

// Класс для реализации RSocket-сервера
public final class Server {

    // Экземпляр логгера для регистрации сообщений
    private static Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    // Статический блок для установки уровня логирования на INFO
    static {
        log.setLevel(Level.INFO);
    }

    // URL для подключения к базе данных PostgreSQL
    private static String url = "jdbc:postgresql://localhost:5433/postgresdb?user=pguser&password=pgpass";

    // Главный метод сервера
    public static void main(String[] args) {
        // Настройка и запуск RSocket-сервера
        RSocketFactory.receive()
                .acceptor((setup, sendingSocket) -> Mono.just(new DefaultSimpleService()))
                .transport(WebsocketServerTransport.create(8801))
                .start()
                .block()
                .onClose()
                .block();
    }

    // Внутренний класс для обработки RSocket-запросов
    public static final class DefaultSimpleService extends AbstractRSocket {

        // Метод для обработки Fire and Forget запроса
        @Override
        public Mono<Void> fireAndForget(Payload payload) {
            System.out.println(payload);
            log.info("получен Fire and Forget запрос в сервере");
            log.info(payload.getDataUtf8());
            Connection conn;

            {
                try {
                    // Подключение к базе данных и выполнение SQL-запроса
                    conn = DriverManager.getConnection(url);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM rbac_userroles WHERE user_id = 1");
                    while (rs.next()) {
                        System.out.print("Колонка 1 вернула ");
                        System.out.println(rs.getString(1));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return Mono.empty();
        }

        // Метод для обработки Request-Response запроса
        @Override
        public Mono<Payload> requestResponse(Payload payload) {
            System.out.println(payload);
            log.info("получен Request-Response запрос в сервере");
            log.info(payload.getDataUtf8());
            Connection conn;

            {
                try {
                    // Подключение к базе данных и выполнение SQL-запроса
                    conn = DriverManager.getConnection(url);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM rbac_userroles WHERE user_id = 1");
                    while (rs.next()) {
                        System.out.print("Колонка 1 вернула ");
                        System.out.println(rs.getString(1));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Формирование и отправка ответа
            return Mono.just(payload.getDataUtf8())
                    .map(payloadString -> MessageMapper.jsonToMessage(payloadString))
                    .map(message -> message.message + " | Request-Response от сервера #1")
                    .map(responseText -> new Message(responseText))
                    .map(responseMessage -> MessageMapper.messageToJson(responseMessage))
                    .map(responseJson -> DefaultPayload.create(responseJson));
        }

        // Метод для обработки Request-Stream запроса
        @Override
        public Flux<Payload> requestStream(Payload payload) {
            System.out.println(payload);
            log.info("получен Request-Stream запрос в сервере");
            log.info(payload.getDataUtf8());
            Connection conn;

            {
                try {
                    // Подключение к базе данных и выполнение SQL-запроса
                    conn = DriverManager.getConnection(url);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM rbac_userroles WHERE user_id = 1");
                    while (rs.next()) {
                        System.out.print("Колонка 1 вернула ");
                        System.out.println(rs.getString(1));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Формирование и отправка потока ответов
            return Mono.just(payload.getDataUtf8())
                    .map(payloadString -> MessageMapper.jsonToMessage(payloadString))
                    .flatMapMany(msg -> Flux.range(0, 5)
                            .map(count -> msg.message + " | Request-Stream от сервера #" + count)
                            .map(responseText -> new Message(responseText))
                            .map(responseMessage -> MessageMapper.messageToJson(responseMessage)))
                    .map(message -> DefaultPayload.create(message));
        }

        // Метод для обработки Request-Channel запроса
        @Override
        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
            System.out.println(payloads);
            log.info("получен Request-Channel запрос в сервере");
            Connection conn;

            {
                try {
                    // Подключение к базе данных и выполнение SQL-запроса
                    conn = DriverManager.getConnection(url);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM rbac_userroles WHERE user_id = 1");
                    while (rs.next()) {
                        System.out.print("Колонка 1 вернула ");
                        System.out.println(rs.getString(1));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Формирование и отправка потока ответов
            return Flux.from(payloads)
                    .map(payload -> payload.getDataUtf8())
                    .map(payloadString -> {
                        log.info(payloadString);
                        return MessageMapper.jsonToMessage(payloadString);
                    })
                    .flatMap(msg -> Flux.range(0, 2)
                            .map(count -> msg.message + " | Request-Channel от сервера #" + count)
                            .map(responseText -> new Message(responseText))
                            .map(responseMessage -> MessageMapper.messageToJson(responseMessage)))
                    .map(message -> DefaultPayload.create(message));

        }
    }
}
