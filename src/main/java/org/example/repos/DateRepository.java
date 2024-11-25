package org.example.repos;

import org.example.model.Dates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<Dates, Long> {
    List<Dates> findByYearAndMonthAndDayAndPropertyId(int year, int month, int day, Long propertyId);
    List<Dates> findByPropertyId(Long PropertyId);
}
