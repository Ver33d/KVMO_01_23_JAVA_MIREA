# Задание 1

Разработать децентрализованную распределенную систему по согласованной теме с преподавателем с использованием Spring/Spring Boot.
Требуемый функционал системы: загрузка и выгрузка файлов различных форматов.
Для демонстрации системы необходимо развернуть как минимум 4 экземпляра приложения при помощи Docker и продемонстрировать работоспособность при отключении каждого из 4х экземпляров, а также при отказе любых двух экземпляров.
В случае невозможности реализации системы с использованием предложенного стека технологий необходимо обосновать это и предложить решение на собственном стеке.


класса FileController
#Зависимости

Для правильного функционирования FileController импортированы следующие библиотеки и классы:

  java.io.IOException
  
  java.util.Objects
  
  org.springframework.beans.factory.annotation.Autowired
  
  org.springframework.http.HttpHeaders
  
  org.springframework.http.MediaType
  
  org.springframework.http.ResponseEntity
  
  org.springframework.util.StringUtils
  
  org.springframework.web.bind.annotation.GetMapping
  
  org.springframework.web.bind.annotation.PathVariable
  
  org.springframework.web.bind.annotation.PostMapping
  
  org.springframework.web.bind.annotation.RequestParam
  
  org.springframework.web.bind.annotation.RestController
  
  org.springframework.web.multipart.MultipartFile
  
  org.springframework.web.servlet.support.ServletUriComponentsBuilder
  
  ru.mirea.practice5.services.FileService


  Конечные точки
  Загрузка файла
  Конечная точка: /upload
  Метод: POST

  Параметры:
  file - Файл для загрузки (multipart/form-data)
  Описание: Обрабатывает загрузку файла в базу данных. Возвращает URI для скачивания загруженного файла.

  Скачивание файла
  Конечная точка: /download/{fileName:.+}
  Метод: GET
  Параметры:
  fileName - Имя файла для скачивания
  Описание: Обрабатывает скачивание файла из базы данных. Возвращает данные файла с соответствующими заголовками.

Класс BinaryFile представляет модель для хранения бинарных файлов в базе данных. Он используется в сочетании с JPA (Java Persistence API) аннотациями для управления отображением объектов в базе данных.

Поля Класса
  id: Идентификатор файла (первичный ключ).
  fileName: Имя файла.
  fileData: Данные файла в виде массива байт.

Конструктор
  BinaryFile(String name, byte[] data): Конструктор для создания объекта BinaryFile с заданным именем и данными.


BinaryFileRepository.java

findByFileName
Метод: 
  BinaryFile findByFileName(String fileName)
Описание: Возвращает объект BinaryFile по его имени файла.
existsByFileName
Метод: 
  Boolean existsByFileName(String fileName)
Описание: Проверяет наличие файла с указанным именем в базе данных. Возвращает true, если файл существует, и false в противном случае.



Класс FileService представляет собой сервис, предназначенный для обработки операций загрузки и скачивания бинарных файлов в приложении Spring Boot. Он взаимодействует с BinaryFileRepository для выполнения операций с базой данных.

Зависимости
  org.springframework.beans.factory.annotation.Autowired
  org.springframework.http.MediaType
  org.springframework.stereotype.Service
  ru.mirea.practice5.models.BinaryFile
  ru.mirea.practice5.repositories.BinaryFileRepository

Методы сервиса
uploadFile

Метод: 
  public void uploadFile(String name, byte[] data)
Описание: Загружает бинарный файл в базу данных, проверяя его наличие по указанному имени.
downloadFile

Метод: 
  public byte[] downloadFile(String name)
Описание: Скачивает бинарный файл из базы данных по указанному имени.
getMediaType

Метод: 
  public String getMediaType(String name)
Описание: Определяет тип медиа по расширению файла.
