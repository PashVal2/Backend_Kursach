import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Main;
import org.example.model.*;
import org.example.service.DateService;
import org.example.service.NewsService;
import org.example.service.PropertyService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class JSControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PropertyService propertyService;
    @MockBean
    private NewsService newsService;
    @MockBean
    private DateService dateService;
    private String name;
    @BeforeEach
    void setUP() {
        name = "testName";
    }
    @Test
    @WithMockUser(username = "testName")
    void testGetTransitDates() throws Exception {
        Long propertyId = 1L;
        User user = new User(1L, "testName", "12345678");
        Property property = new Property(propertyId, "Test Property", 10.0, 20.0);
        Dates date = new Dates(1L, 2024, 12, 1, user, property);

        Mockito.when(userService.findByName(name)).thenReturn(Optional.of(user));
        Mockito.when(dateService.findByPropertyId(propertyId)).thenReturn(List.of(date));

        mockMvc.perform(get("/api/book-dates/{propertyId}", propertyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].day").value(1))
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].month").value(12))
                .andExpect(jsonPath("$[0].userIsOwner").value(true));;
    }
    @Test
    void testGetCoord() throws Exception {
        Property property = new Property(1L, "Test Property", 10, 10);
        Mockito.when(propertyService.findAll()).thenReturn(List.of(property));
        mockMvc.perform(get("/api/coord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latitude").value(10))
                .andExpect(jsonPath("$[0].longitude").value(10))
                .andExpect(jsonPath("$[0].name").value("Test Property"))
                .andExpect(jsonPath("$[0].id").value(1));
    }
    @Test
    void testPostNews() throws Exception {
        News news = new News("Title", "Tag", "https://example.com");
        Mockito.when(newsService.getAllNews()).thenReturn(List.of(news));

        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].tag").value("Tag"))
                .andExpect(jsonPath("$[0].url").value("https://example.com"));
    }
    @Test
    @WithMockUser(username = "testName")
    void testPostBookDates() throws Exception {
        // Создаем список дат
        List<BookingDates> bookingDatesList = new ArrayList<>();
        bookingDatesList.add(new BookingDates(2024, 12, 1, 1L)); // дата 1
        bookingDatesList.add(new BookingDates(2024, 12, 2, 2L)); // дата 2

        User user = new User(1L, "testName", "12345678");
        Mockito.when(userService.findByName("testName")).thenReturn(Optional.of(user));
        dateService.addDates(bookingDatesList, user);
        String bookingDatesJson = new ObjectMapper().writeValueAsString(bookingDatesList);

        mockMvc.perform(post("/api/book-dates")
                        .contentType("application/json")
                        .content(bookingDatesJson))  // Передаем правильный объект
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Даты добавленны"));

        Mockito.verify(dateService).addDates(bookingDatesList, user);
    }
}
