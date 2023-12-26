package ru.mirea.practice5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.practice5.models.BinaryFile;

public interface BinaryFileRepository extends JpaRepository<BinaryFile, Integer> {
  BinaryFile findByFileName(String fileName);

  Boolean existsByFileName(String fileName);
}
