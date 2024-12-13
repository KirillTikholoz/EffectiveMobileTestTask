package org.example.repositories;

import org.example.entitis.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthor(String author, Pageable pageable);
    Page<Task> findByExecutor(String executor, Pageable pageable);
}
