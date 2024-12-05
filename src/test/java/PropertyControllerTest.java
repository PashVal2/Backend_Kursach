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

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class PropertyControllerTest {
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
    public void testGetProperty() throws Exception {
        Property property = new Property(1L, "Property Name", 100.0, 50.0);
        List<Property> properties = Arrays.asList(property);
        Mockito.when(propertyService.findAll()).thenReturn(properties);

        mockMvc.perform(get("/property"))
                .andExpect(status().isOk())
                .andExpect(view().name("property"))
                .andExpect(model().attribute("properties", properties))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("ADMIN", true))
                .andExpect(model().attribute("name", name));
    }
    @Test
    @WithMockUser(username = "testName", roles = "ADMIN")
    public void testDeleteProperty() throws Exception {
        Long propertyId = 1L;
        Mockito.doNothing().when(propertyService).deleteById(propertyId);

        mockMvc.perform(delete("/deleteProperty")
                        .param("property_id", String.valueOf(propertyId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/property"));

        Mockito.verify(propertyService, Mockito.times(1)).deleteById(propertyId);
    }
    @Test
    @WithMockUser(username = "testName", roles = "USER")
    public void testDeletePropertyForUser() throws Exception {
        Long propertyId = 1L;
        mockMvc.perform(delete("/deleteProperty")
                        .param("property_id", String.valueOf(propertyId)))
                .andExpect(status().isForbidden());

        Mockito.verify(propertyService, Mockito.times(0)).deleteById(Mockito.anyLong());
    }
}
