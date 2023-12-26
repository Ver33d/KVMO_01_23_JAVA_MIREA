// Пакет, в котором находится класс
package ru.mirea.practice5.services;

// Импорт необходимых библиотек и классов
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.mirea.practice5.models.BinaryFile;
import ru.mirea.practice5.repositories.BinaryFileRepository;

// Сервис для работы с бинарными файлами
@Service
public class FileService {

  // Автоматический инжект репозитория для работы с бинарными файлами
  @Autowired
  BinaryFileRepository repository;

  // Метод для загрузки бинарного файла
  public void uploadFile(String name, byte[] data) {
    // Проверка наличия файла с указанным именем в репозитории
    if (!repository.existsByFileName(name)) {
      // Создание объекта BinaryFile и сохранение в репозиторий
      BinaryFile file = new BinaryFile(name, data);
      repository.save(file);
    }
  }

  // Метод для скачивания бинарного файла по имени
  public byte[] downloadFile(String name) {
    // Получение объекта BinaryFile из репозитория по имени
    BinaryFile binaryFile = repository.findByFileName(name);
    // Возвращение данных файла
    return binaryFile.getFileData();
  }

  // Метод для определения типа медиа по расширению файла
  public String getMediaType(String name) {
    // Получение формата файла из имени
    String getFormat = name.split("\\.")[1];
    // Переменная для хранения типа медиа
    String type = null;
    
    // Определение типа медиа в зависимости от формата
    switch (getFormat) {
      case "pdf" :
        type = MediaType.APPLICATION_PDF_VALUE;
        break;
      case "txt" :
        type = MediaType.TEXT_PLAIN_VALUE;
        break;
      case "jpg", "jpeg" :
        type = MediaType.IMAGE_JPEG_VALUE;
        break;
      case "png" :
        type = MediaType.IMAGE_PNG_VALUE;
        break;
    }
    
    // Возвращение определенного типа медиа
    return type;
  }

}
