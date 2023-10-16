import java.util.Scanner;
import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task2 {
    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(2); // лимит на 2 потока

        // ввод числа
        Scanner scan = new Scanner(System.in);
        int input1 = Integer.parseInt(scan.nextLine());

        // создание Future
        Future<?> ftr = es.submit(new MyRunnable(input1));

        while (true) {
            String input2 = scan.nextLine(); // ввод нового запроса
            if (!ftr.isDone()) {
                es.submit(new MyRunnable(Integer.parseInt(input2)));
            }
            break;
        }

        es.shutdown();
    }
}

class MyRunnable implements Runnable {
    Integer number;
    public MyRunnable(int number) {
        this.number = number; // передача числа в поток
    }

    @Override
    public void run() {
        try {
            String threadName = Thread.currentThread().getName(); // название потока

            // вывод информации по инициализации потока
            System.out.println("["+threadName+"] Waiting for response... [Math.pow("+number+",2)]");

            long random = (long)(Math.random() * 4000) + 1000; // случайная задержка
            double result = Math.pow(number, 2);

            Thread.sleep(random); // ожидание по задержке


            // вывод ответа для потока
            System.out.println(
                    "["+threadName+"]" +
                            " Square of the number " + number + " is: " + result +
                            " [response time: " + random + "ms]"
            );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}