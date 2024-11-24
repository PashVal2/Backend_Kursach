package org.example.repos;

import org.example.model.BookingDates;
import org.example.model.Dates;
import org.example.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DateService {
    private final DateRepository dateRepository;
    public DateService(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }
    public void addDates(List<BookingDates> bookingDatesList,
         User user) {
        for (BookingDates bookingDate: bookingDatesList) {
            Dates date = new Dates();
            date.setDay(bookingDate.getDay());
            date.setMonth(bookingDate.getMonth());
            date.setYear(bookingDate.getYear());
            date.setUser(user);
            dateRepository.save(date);
        }
    }
}
