import org.example.Main;
import org.example.model.Property;
import org.example.repos.PropertyRepository;
import org.example.repos.UserRepository;
import org.example.service.NewsService;
import org.example.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class MyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PropertyRepository propertyRepository;
    @MockBean
    private NewsService newsService;
    @MockBean
    private PropertyService propertyService;
    @MockBean
    private UserRepository userRepository;
    private String name;
    @BeforeEach
    void setUP() {
        name = "testName";
    }
    @Test
    @WithMockUser(username = "testName", roles = "ADMIN")
    public void testGetProperty() throws Exception {
        Property property = new Property(1L, "Property Name", 100.0, 50.0);
        List<Property> properties = Arrays.asList(property);

        Mockito.when(propertyRepository.findAll()).thenReturn(properties);

        mockMvc.perform(get("/property"))
                .andExpect(status().isOk())
                .andExpect(view().name("property"))
                .andExpect(model().attribute("properties", properties))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("ADMIN", true))
                .andExpect(model().attribute("name", name));
    }
    @Test
    @WithMockUser(username = "testName")
    public void testGetSpecificProperty() throws Exception {
        Property property = new Property(1L, "Property Name", 100.0, 50.0);
        Mockito.when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        mockMvc.perform(get("/property/Property Name_1"))
                .andExpect(status().isOk())
                .andExpect(view().name("property-details"))
                .andExpect(model().attribute("property", property))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("ADMIN", false))
                .andExpect(model().attribute("name", name));
    }
    @Test
    public void testGetSpecificPropertyForUnauthUser() throws Exception {
        Property property = new Property(1L, "Property Name", 100.0, 50.0);
        Mockito.when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        mockMvc.perform(get("/property/Property Name_1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("message", "b"))
                .andExpect(model().attribute("showLogout", false))
                .andExpect(model().attribute("ADMIN", false))
                .andExpect(model().attributeDoesNotExist("name"))
                .andExpect(view().name("index"));
    }
    @Test
    @WithMockUser(username = "testName")
    void testIndexAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("message", "b"))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("ADMIN", false))
                .andExpect(model().attribute("name", name))
                .andExpect(view().name("index"));
    }
}

