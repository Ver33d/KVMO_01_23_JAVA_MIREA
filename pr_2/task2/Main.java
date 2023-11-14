package pr2.task2;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import static edu.mirea.rksp.pr2.task1.Main.createFile;

public class Main { // Определение публичного класса Main
    // Определение приватного статического метода copyFileUsingStream, который использует потоки ввода/вывода для копирования файла. Метод может выбросить IOException.
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            // 65536 .. 8192 4096
            byte[] buffer = new byte[65536];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
// Реализация метода copyFileUsingStream. В этом блоке кода открываются потоки ввода (is) и вывода (os), создается буфер для копирования данных, и затем данные копируются из исходного файла в цикле. 
// В блоке finally закрываются открытые потоки.
    private static void copyFileUsingChannel(File source, File dest) throws IOException {
	// Определение приватного статического метода copyFileUsingChannel, который использует FileChannel для копирования файла. Метод может выбросить IOException
	// Реализация метода copyFileUsingChannel. В этом блоке кода создаются FileChannel для исходного и целевого файлов, и данные копируются с использованием метода transferFrom. В блоке finally закрываются открытые каналы.
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally{
            sourceChannel.close();
            destChannel.close();
        }
    }
     // Определение метода copyFileUsingApacheCommonsIO, который использует метод copyFile из библиотеки Apache Commons IO для копирования файла. Метод может выбросить IOException.
     private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }
    // Определение метода copyFileUsingJava7Files, который использует метод copy из класса Files в Java 7 для копирования файла. Метод может выбросить IOException
	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
    // Определение метода main. Точка входа в программу. Метод может выбросить InterruptedException и IOException.
    public static void main(String[] args) throws InterruptedException, IOException {
        String currentDir = System.getProperty("user.dir"); // Получение текущего рабочего каталога.
        new File(currentDir+"/tmp").mkdir(); // Создание нового каталога "tmp" в текущем рабочем каталоге.
        Path tmpDir = Paths.get(currentDir, "tmp"); // Создание объекта Path для нового каталога "tmp".
        System.out.println("Working directory is - "+tmpDir); // Вывод информации о текущем рабочем каталоге.

        // Получение объекта File для каталога "tmp" и удаление всех файлов из этого каталога с использованием метода cleanDirectory из Apache Commons IO.
        File directory = new File(tmpDir.toString());
        FileUtils.cleanDirectory(directory);

        
        final int FILE_SIZE = 100;
        File myFile = createFile("FILE", tmpDir.toString(),FILE_SIZE * 1024 * 1024);

        // Создание файла "FILE" заданного размера в мегабайтах с использованием метода createFile из класса Main в пакете edu.mirea.rksp.pr2.task1.
        File dest = new File(tmpDir+"/FILE_copy1");
        long start = System.nanoTime();// Определение объекта File для целевого файла "FILE_copy1".
        copyFileUsingStream(myFile, dest);
        System.out.println("[IOStreams] Time - "+(System.nanoTime()-start));

        // Копирование файла с использованием потоков ввода/вывода и измерение времени выполнения.
        dest = new File(tmpDir+"/FILE_copy2");
        start = System.nanoTime();
        copyFileUsingChannel(myFile, dest);
        System.out.println("[FileChannel] Time - "+(System.nanoTime()-start));

        // Копирование файла с использованием FileChannel и измерение времени выполнения.
        dest = new File(tmpDir+"/FILE_copy3");
        start = System.nanoTime();
        copyFileUsingApacheCommonsIO(myFile, dest);
        System.out.println("[Apache Commons IO] Time - "+(System.nanoTime()-start));

        // Копирование файла с использованием Files из Java 7 и измерение времени выполнения.
        dest = new File(tmpDir+"/FILE_copy4");
        start = System.nanoTime();
        copyFileUsingJava7Files(myFile, dest);
        System.out.println("[Files class] Time - "+(System.nanoTime()-start));
    }
}