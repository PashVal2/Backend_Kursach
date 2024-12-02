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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @Test
    @WithMockUser(username = "testName", roles = "ADMIN")
    void testGetAddProperty() throws Exception {
        mockMvc.perform(get("/addProperty"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("isPost", false))
                .andExpect(model().attribute("name", name));
    }
    @Test
    @WithMockUser(username = "testName", roles = "USER")  // Пользователь без прав для /addProperty
    void testGetAddPropertyForUser() throws Exception {
        mockMvc.perform(get("/addProperty"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/accessDenied"));
    }
    @Test
    void testGetAddPropertyForUnauth() throws Exception {
        mockMvc.perform(get("/addProperty"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    @Test
    @WithMockUser(username = "testName", roles = "USER")
    void testGetAllNews() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(view().name("news"))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("name", "testName"))
                .andExpect(model().attribute("ADMIN", false));
    }
    @Test
    void testGetAccessDenied() throws Exception {
        mockMvc.perform(get("/accessDenied"))
                .andExpect(status().isOk())
                .andExpect(view().name("accessDenied"));
    }
    @Test
    @WithMockUser(username = "testName", roles = "ADMIN")
    void testAddPropertyPost_Success() throws Exception {
        // Подготовим мок для сервиса
        Property newProperty = new Property(1L, "Test Property", 10.0, 20.0);

        // Выполняем POST-запрос с параметрами формы и проверяем результат
        mockMvc.perform(post("/addProperty")
                        .param("name", "Test Property")
                        .param("cost", "0.0")
                        .param("description", "Test Description")
                        .param("latitude", "10.0")
                        .param("longitude", "20.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("addProperty"))
                .andExpect(model().attribute("isPost", true));

        verify(propertyService).addProperty(
                newProperty.getName(),
                newProperty.getDescription(),
                newProperty.getCost(),
                newProperty.getLatitude(),
                newProperty.getLongitude()
        );
    }
}

