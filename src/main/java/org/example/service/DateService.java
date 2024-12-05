package org.example.service;

import org.example.model.BookingDates;
import org.example.model.Dates;
import org.example.model.Property;
import org.example.model.User;
import org.example.repos.DateRepository;
import org.example.repos.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DateService {
    private final DateRepository dateRepository;
    private final PropertyRepository propertyRepository;
    public DateService(DateRepository dateRepository, PropertyRepository propertyRepository) {
        this.dateRepository = dateRepository;
        this.propertyRepository = propertyRepository;
    }
    public void addDates(List<BookingDates> bookingDatesList,
         User user) {
        for (BookingDates bookingDate: bookingDatesList) {
            Dates date = new Dates();
            Property property = new Property();
            property = propertyRepository.findById(bookingDate.getPropertyId()).get();
            int day = bookingDate.getDay();
            int month = bookingDate.getMonth();
            int year = bookingDate.getYear();
            if (!dateRepository.findByYearAndMonthAndDayAndPropertyId(year, month, day, property.getId()).isEmpty()) {
                continue;
            }
            date.setDay(day);
            date.setMonth(month);
            date.setYear(year);
            date.setProperty(property);
            date.setUser(user);
            dateRepository.save(date);
        }
    }

    public List<Dates> findByPropertyId(Long propertyId) {
        return dateRepository.findByPropertyId(propertyId);
    }
}
