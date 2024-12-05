import org.example.Main;
import org.example.service.NewsService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class IndexControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;

    @MockBean
    private UserService userService;
    private String name;
    @BeforeEach
    void setUP() {
        name = "testName";
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

