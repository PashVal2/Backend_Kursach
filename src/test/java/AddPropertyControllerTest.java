import org.example.Main;
import org.example.model.Property;
import org.example.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class AddPropertyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PropertyService propertyService;
    private String name;
    @BeforeEach
    void setUP() {
        name = "testName";
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
    @WithMockUser(username = "testName", roles = "ADMIN")
    void testGetAddProperty() throws Exception {
        mockMvc.perform(get("/addProperty"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("isPost", false))
                .andExpect(model().attribute("name", name));
    }
}
