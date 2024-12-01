import org.example.Main;
import org.example.model.Dates;
import org.example.model.Property;
import org.example.model.User;
import org.example.repos.DateRepository;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private DateRepository dateRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PropertyRepository propertyRepository;
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

        Mockito.when(userRepository.findByName(name)).thenReturn(Optional.of(user));
        Mockito.when(dateRepository.findByPropertyId(propertyId)).thenReturn(List.of(date));

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
        Mockito.when(propertyRepository.findAll()).thenReturn(List.of(property));
        mockMvc.perform(get("/api/coord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latitude").value(10))
                .andExpect(jsonPath("$[0].longitude").value(10))
                .andExpect(jsonPath("$[0].name").value("Test Property"))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
