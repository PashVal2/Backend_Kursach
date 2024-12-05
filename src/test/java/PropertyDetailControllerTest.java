import org.example.Main;
import org.example.model.Property;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class PropertyDetailControllerTest {
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
    @WithMockUser(username = "testName")
    public void testGetSpecificProperty() throws Exception {
        Property property = new Property(1L, "Property Name", 100.0, 50.0);
        Mockito.when(propertyService.findById(1L)).thenReturn(Optional.of(property));

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
        Mockito.when(propertyService.findById(1L)).thenReturn(Optional.of(property));

        mockMvc.perform(get("/property/Property Name_1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    @Test
    @WithMockUser(username = "testName", roles = "USER")
    public void testEditPropertySuccess() throws Exception {
        Long propertyId = 1L;
        Double cost = 500.0;
        String description = "Updated Description";

        Property property = new Property(propertyId, "Name", 50.0, 50.0);

        Mockito.when(propertyService.findById(propertyId)).thenReturn(Optional.of(property));
        Mockito.doNothing().when(propertyService).save(Mockito.any(Property.class));

        mockMvc.perform(post("/editProperty")
                        .param("id", String.valueOf(propertyId))
                        .param("name", "Name")
                        .param("cost", String.valueOf(cost))
                        .param("description", description))
                .andExpect(status().isForbidden());

        Mockito.verify(propertyService, Mockito.times(0)).findById(propertyId);
        Mockito.verify(propertyService, Mockito.times(0)).save(Mockito.any(Property.class));
    }
    @Test
    @WithMockUser(username = "testName", roles = "ADMIN")
    public void testEditPropertySuccessForUSer() throws Exception {
        Long propertyId = 1L;
        Double cost = 500.0;
        String description = "Updated Description";

        Property property = new Property(propertyId, "Name", 50.0, 50.0);

        Mockito.when(propertyService.findById(propertyId)).thenReturn(Optional.of(property));
        Mockito.doNothing().when(propertyService).save(Mockito.any(Property.class));

        mockMvc.perform(post("/editProperty")
                        .param("id", String.valueOf(propertyId))
                        .param("name", "Name")
                        .param("cost", String.valueOf(cost))
                        .param("description", description))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/property"));

        Mockito.verify(propertyService, Mockito.times(1)).findById(propertyId);
        Mockito.verify(propertyService, Mockito.times(1)).save(Mockito.any(Property.class));
    }
}
