package org.example.repos;

import org.example.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository // CRUD-операции над News
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findByTitle(String title);
    Optional<News> findById(Long id);
}
