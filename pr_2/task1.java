package pr1.task1; // Определение пакета, в котором находится класс task1.

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class task1 {
    //Определение статического метода createFile, который создает файл с указанным именем, путем и размером в байтах. Метод может выбросить IOException.
    public static File createFile(final String filename, final String pathname, final long sizeInBytes) throws IOException {
        File file = new File(pathname + File.separator + filename); //Создание объекта File с указанным путем и именем файла.
        file.createNewFile(); //Создание нового файла в файловой системе.
        // Создание объекта RandomAccessFile для работы с созданным файлом в режиме чтения и записи.
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(sizeInBytes); //Установка размера файла в байтах.
        raf.close(); //Закрытие RandomAccessFile.

        return file; // Возвращение созданного файла.
    }
    // Определение метода main, который является точкой входа в программу. Метод может выбросить IOException.
    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir"); // Получение текущего рабочего каталога.
        new File(currentDir+"/tmp").mkdir(); //Создание нового каталога "tmp" в текущем рабочем каталоге.
        Path tmpDir = Paths.get(currentDir, "tmp"); //Создание объекта Path для нового каталога "tmp".
        System.out.println("Working directory is - "+tmpDir); //Вывод сообщения о текущем рабочем каталоге.

        // Вызов метода createFile для создания файла "file.txt" в каталоге "tmp" с размером 0 байт.
        File myFile = createFile("file.txt", tmpDir.toString(),0);
        String str = "Hello, World!\nNew, second line\nAnd another line\nYet another line\n..."; //  Создание строки str с текстом.
        byte[] bs = str.getBytes(); // Преобразование строки в массив байт.
        Files.write(myFile.toPath(), bs); // : Запись массива байт в файл.
        // Вывод содержимого созданного файла.
        System.out.println("File "+myFile.getName()+" contents:\n" + new String(Files.readAllBytes(myFile.toPath())));
    }
}