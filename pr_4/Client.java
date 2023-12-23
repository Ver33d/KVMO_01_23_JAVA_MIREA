// Подключение необходимых библиотек и классов
package edu.mirea.rksp.pr4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Определение класса Client
public class Client {

    // Экземпляр логгера для регистрации сообщений
    private static Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    // Статический блок для установки уровня логирования на INFO
    static {
        log.setLevel(Level.INFO);
    }

    // Главный метод
    public static void main(String[] args) {
        // Создание экземпляра класса Client
        final Client client = new Client();
        // Подключение к серверу и получение экземпляра RSocket
        final RSocket rSocket = client.connect();

        // Создание и настройка графического интерфейса
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Создание и настройка панели для кнопок
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setBackground(Color.lightGray);
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        // Создание кнопок для различных типов взаимодействия с RSocket
        final JButton fireAndForgetBtn = new JButton("Fire and Forget");
        final JButton requestResponseBtn = new JButton("Request-Response");
        final JButton requestStreamBtn = new JButton("Request-Stream");
        final JButton requestChannelBtn = new JButton("Request-Channel");

        // Обработчик событий для обработки кликов по кнопкам
        final ActionListener AListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "Fire and Forget":
                        client.fireAndForget(rSocket);
                        break;
                    case "Request-Response":
                        client.requestResponse(rSocket);
                        break;
                    case "Request-Stream":
                        client.requestStream(rSocket);
                        break;
                    case "Request-Channel":
                        client.requestChannel(rSocket);
                        break;
                }
            }
        };

        // Добавление обработчика событий к кнопкам
        fireAndForgetBtn.addActionListener(AListener);
        requestResponseBtn.addActionListener(AListener);
        requestStreamBtn.addActionListener(AListener);
        requestChannelBtn.addActionListener(AListener);

        // Добавление кнопок на панель
        panel.add(fireAndForgetBtn);
        panel.add(requestResponseBtn);
        panel.add(requestStreamBtn);
        panel.add(requestChannelBtn);

        // Добавление панели на фрейм и отображение фрейма
        frame.add(panel);
        frame.setVisible(true);
    }

    // Метод для подключения к серверу и получения RSocket
    private RSocket connect() {
        return RSocketFactory.connect()
                .transport(WebsocketClientTransport.create(8801))
                .start()
                .block();
    }

    // Метод для отправки Fire and Forget запроса
    private void fireAndForget(RSocket rSocket) {
        log.info("отправка fire and forget от клиента");
        Flux.just(new Message("fire and forget JAVA client!"))
                .map(msg -> MessageMapper.messageToJson(msg))
                .map(json -> DefaultPayload.create(json))
                .flatMap(message -> rSocket.fireAndForget(message))
                .blockLast();
    }

    // Метод для отправки Request-Response запроса
    private void requestResponse(RSocket rSocket) {
        log.info("отправка запроса-request-response от клиента");
        Flux.just(new Message("requestResponse от JAVA клиента!"))
                .map(msg -> MessageMapper.messageToJson(msg))
                .map(json -> DefaultPayload.create(json))
                .flatMap(message -> rSocket.requestResponse(message))
                .map(payload -> payload.getDataUtf8())
                .doOnNext(payloadString -> {
                    log.info("получен ответ от JAVA клиента");
                    log.info(payloadString);
                })
                .blockLast();
    }

    // Метод для отправки Request-Stream запроса
    private void requestStream(RSocket rSocket) {
        log.info("отправка запроса-request-stream от клиента");
        Flux.just(new Message("requestStream от JAVA клиента!"))
                .map(msg -> MessageMapper.messageToJson(msg))
                .map(json -> DefaultPayload.create(json))
                .flatMap(message -> rSocket.requestStream(message))
                .map(payload -> payload.getDataUtf8())
                .doOnNext(payloadString -> log.info(payloadString))
                .blockLast();
    }

    // Метод для отправки Request-Channel запроса
    private void requestChannel(RSocket rSocket) {
        log.info("отправка запроса-request-channel от клиента");
        final Flux<Payload> requestPayload = Flux.range(0, 5)
                .map(count -> new Message("requestChannel от JAVA клиента! #" + count))
                .map(msg -> {
                    log.info("отправка сообщения: {}", msg.message);
                    return MessageMapper.messageToJson(msg);
                })
                .map(json -> DefaultPayload.create(json));

        rSocket
                .requestChannel(requestPayload)
                .map(payload -> payload.getDataUtf8())
                .doOnNext(payloadString -> log.info(payloadString))
                .blockLast();
    }
}
