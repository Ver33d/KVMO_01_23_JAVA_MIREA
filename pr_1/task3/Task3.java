import java.lang.Thread;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task3{
    public static void main(String[] args) {
        // Создание экземпляра FileQueue с лимитом 5 файлов
        FileQueue queue = new FileQueue(5);
        String[] types = new String[] {"XML", "JSON", "XLS"}; // типы файлов
        FileHandler[] fileHandlers = new FileHandler[3];
        Thread[] handlerThreads = new Thread[3];
	// Создание и запуск потоков обработки файлов
        for (int i = 0; i < fileHandlers.length; i++){
            fileHandlers[i] = new FileHandler(queue, types[i], queue.lock, queue.notEmpty);
            handlerThreads[i] = new Thread(fileHandlers[i]);
            handlerThreads[i].start();
        }
	// Создание и запуск потока генерации файлов
        FileGenerator fg = new FileGenerator(queue, queue.lock, queue.notFull, types);
        Thread gt = new Thread(fg);
        gt.start();
    }
}

//  Класс File представляет собой файл с определенным типом и размером
class File {
    String type;
    Integer size;

    public File(String type, Integer size) { //  Конструктор класса File для создания файлов с заданным типом и размером.
        this.type = type; //Тип файла
        this.size = size; //Размер файла
    }
}

//Класс FileGenerator реализует поток для генерации файлов и помещения их в очередь.
class FileGenerator implements Runnable {
    private final FileQueue queue;
    private final Lock lock;
    private final Condition condition;
    private final String[] types;
    private final Random random = new Random();

    public FileGenerator(FileQueue queue, Lock lock, Condition condition, String[] types) {
        this.queue = queue;
        this.lock = lock;
        this.condition = condition;
        this.types = types;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                while (queue.size() == 5) {
                    condition.await();
                }

                String type = types[random.nextInt(3)];
                Integer size = random.nextInt(91)+10;
                int time = random.nextInt(901)+100;

                File file = new File(type,size);

                System.out.println("[" + Thread.currentThread().getName() + "] Generated File: " + file.size + " " + file.type);

                Thread.sleep(time);

                queue.add(file);
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

// класс обработчика файлов. Класс FileHandler реализует поток для обработки файлов из очереди.
class FileHandler implements Runnable {
    private final FileQueue queue;
    private final String type;
    private final Lock lock;
    private final Condition condition;

    public FileHandler(FileQueue queue, String type, Lock lock, Condition condition) {
        this.queue = queue;
        this.type = type;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                while (queue.size() == 0) {
                    condition.await();
                }
                File file = queue.peek();
                if (file.type.equals(type)) {
                    queue.remove();
                    long time = file.size * 7L;
                    Thread.sleep(time);
                    System.out.println("[" + Thread.currentThread().getName() + "] Processed File: " + file.size + " " + file.type + " [time spent: " + time + "ms]");
                }
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

//Класс FileQueue представляет собой очередь файлов с определенной емкостью
class FileQueue {
    public int capacity;
    public Queue<File> queue = new LinkedList<>();
    public ReentrantLock lock = new ReentrantLock();
    public Condition notFull = lock.newCondition();
    public Condition notEmpty = lock.newCondition();
    //Конструктор класса FileQueue для создания очереди с заданной емкостью
    public FileQueue(int capacity) {
        this.capacity = capacity;  //Емкость очереди
    }
    //Добавляет файл в очередь
    public void add(File file) throws InterruptedException { //file - Файл для добавления
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(file);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    //Удаляет файл из очереди
    public void remove() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == 0) {
                notEmpty.await();
            }
            queue.remove();
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
    //Возвращает размер очереди
    public int size() {
        return queue.size();  //Размер очереди
    }
    //Возвращает файл, находящийся в начале очереди, но не удаляет его
    public File peek() {
        return queue.peek();  //Первый файл в очереди
    }
}