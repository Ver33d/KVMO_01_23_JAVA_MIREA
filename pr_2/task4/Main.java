package pr2.task4;

import static edu.mirea.rksp.pr2.task3.Main.sum;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main { // Класс содержит метод main(String[] args), который является точкой входа в программу.

    public static void main(String[] args) {
        var path = "tmp"; // Создается каталог "tmp" в текущем рабочем каталоге.
        String currentDir = System.getProperty("user.dir");
        new File(currentDir+File.separator+path).mkdir();
        Path tmpDir = Paths.get(currentDir, path);
        System.out.println("Working directory is - "+tmpDir);

        try { // Настройка WatchService для мониторинга изменений в каталоге
            WatchService watcher = FileSystems.getDefault().newWatchService();
            var reservePath = ".reserve";
            Reserve(tmpDir.getFileName().toString(), reservePath);
            tmpDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            System.out.println("Watch Service registered for dir: " + tmpDir.getFileName());

            while (true) { // В бесконечном цикле происходит ожидание событий от WatchService
		// Обработка событий, таких как создание, удаление или модификация файлов
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    System.out.println(kind.name() + ": " + fileName);

                    if (kind == OVERFLOW) {
                        continue;
                    } else if (kind == ENTRY_CREATE) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CreateBak(Paths.get(path, fileName.toString()),
                                Paths.get(reservePath, fileName.toString()));
                    } else if (kind == ENTRY_DELETE) {
                        try {
                            sum(Paths.get(reservePath, fileName.toString() + ".bak").toFile());
                        } catch (IOException e) {
                            System.err.println(fileName.toString() + ": " + e);
                        }
                        DeleteBakFile(Paths.get(reservePath, fileName.toString() + ".bak"));
                    } else if (kind == ENTRY_MODIFY) {
                        Diff(Paths.get(path, fileName.toString()),
                                Paths.get(reservePath, fileName.toString() + ".bak"));
                        CreateBak(Paths.get(path, fileName.toString()),
                                Paths.get(reservePath, fileName.toString()));
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    // Метод Diff(Path path, Path reservePath)
    // Сравнивает содержимое двух файлов и выводит добавленные и удаленные строки
    // Создается поток строк из содержимого файлов
    // Создаются списки newList и oldList из строк каждого файла
    // Выводится содержимое старого списка
    // Создаются списки для удаленных и добавленных строк
    // Выводятся новый список, удаленные и добавленные строки
    private static void Diff(Path path, Path reservePath) {
        try {
            Stream<String> stream = Files.lines(path);
            List<String> newList = stream.collect(Collectors.toList());
            stream = Files.lines(reservePath);
            List<String> oldList = stream.collect(Collectors.toList());

            System.out.println("=============");
            System.out.printf("Old list: ");
            System.out.println(oldList);

            ArrayList<String> removedList = new ArrayList<String>();
            for (String itemToRemove : oldList) {
                if(!newList.contains(itemToRemove)){
                    removedList.add(itemToRemove);
                }
            }

            ArrayList<String> addedList = new ArrayList<String>();
            for (String itemToAdd : newList) {
                if(!oldList.contains(itemToAdd)){
                    addedList.add(itemToAdd);
                }
            }

            System.out.printf("New list: ");
            System.out.println(newList);

            System.out.println("=============");
            System.out.printf("Removed: ");
            System.out.println(removedList);

            System.out.printf("Added: ");
            System.out.println(addedList);

            stream.close();
        } catch (Throwable cause) {
            System.out.println("Exception");
        }
    }
    // Метод Reserve(String path, String reservePath):
    // Вызывает методы для подготовки резервной копии перед началом мониторинга
    // Создается резервный каталог
    // Устанавливается хук завершения программы для удаления резервного каталога при завершении
    private static void Reserve(String path, String reservePath) {
        CreateDirectory(reservePath);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                DeleteDirectory(reservePath);
            }
        });
        CopyFolder(path, reservePath);
    }
    // Метод CopyFolder(String path, String reservePath):
    // Рекурсивно копирует файлы из указанного каталога в резервный каталог.
    // Получает список файлов в указанном каталоге.
    // Для каждого файла: если это каталог, вызывает себя рекурсивно; если это файл, создает резервную копию.
    private static void CopyFolder(String path, String reservePath) {
        var files = new File(path).listFiles();
        for (var file : files) {
            if (file.isDirectory()) {
                CopyFolder(file.getAbsolutePath(), reservePath);
            } else {
                var reserveFile = new File(reservePath + File.separator + file.getName() + ".bak");
                try {
                    reserveFile.createNewFile();
                    var fis = new FileInputStream(file);
                    var fos = new FileOutputStream(reserveFile);
                    var buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Метод CreateBak(Path path, Path reservePath):
    // Создает резервную копию файла.
    // Использует Files.copy для копирования файла в резервный каталог с заменой существующего файла.
    private static void CreateBak(Path path, Path reservePath) {
        try {
            java.nio.file.Files.copy(path, reservePath.resolveSibling(
                    reservePath.getFileName() + ".bak"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Метод DeleteBakFile(Path path):
    // Удаляет резервную копию файла.
    // Использует Files.delete для удаления файла.
    private static void DeleteBakFile(Path path) {
        try {
            java.nio.file.Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Метод CreateDirectory(String name):
    // Создает каталог, если он не существует.
    // Проверяет, существует ли каталог, и, если нет, создает его.
    private static void CreateDirectory(String name) {
        File file = new File(name);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Created reserve folder for monitoring.");
            } else {
                System.out.println("Failed to create reserve folder for monitoring.");
            }
        }
    }
    
    private static void DeleteDirectory(String name) {
        CleanDirectory(name); // Вызывает метод CleanDirectory для удаления содержимого каталога перед удалением самого каталога.
        File file = new File(name); // Создает объект File для представления каталога с указанным именем.
        if (file.exists()) { // Проверяет, существует ли указанный каталог.
            if (file.delete()) {
                System.out.println("Stop monitoring...");
            } else {
                System.out.println("Failed to delete reserve folder for monitoring.");
            }
        }
    }

    private static void CleanDirectory(String name) {
        File file = new File(name); // Создает объект File для представления каталога с указанным именем.
        if (file.exists()) { // Проверяет, существует ли указанный каталог
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) { // Проверяет, является ли текущий элемент файлом.
                    f.delete(); //  Если это файл, удаляет его
                }
            }
        }
    }
}