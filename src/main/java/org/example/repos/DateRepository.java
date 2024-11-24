package org.example.repos;

import org.example.model.Dates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<Dates, Long> {
    List<Dates> findByYearAndMonth(String year, int month);
    List<Dates> findByPropertyId(Long PropertyId);
}
