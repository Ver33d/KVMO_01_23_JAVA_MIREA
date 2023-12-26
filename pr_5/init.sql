-- Создание таблицы 'binary_file' в схеме 'public'
create table if not exists public.binary_file
(
    -- Поле 'id' с автоматическим увеличением и первичным ключом
    id serial primary key,
    
    -- Поле 'file_name' для хранения имен файлов
    file_name varchar,
    
    -- Поле 'file_data' для хранения бинарных данных файлов
    file_data bytea
);
