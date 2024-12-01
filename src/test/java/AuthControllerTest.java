import org.example.Main;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private String name;
    @BeforeEach
    void setUP() {
        name = "testName";
    }
    @Test
    void testGetRegistrationPage_Anonymous() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("showLogout", false));
    }
    @Test
    @WithMockUser(username = "testName")
    void testGetRegistrationPage_Authenticated() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("name", name));
    }
    @Test
    void testPostRegisterUser_Success() throws Exception {
        String username = "newUser";
        String password = "password";
        String confirmPassword = "password";
        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", confirmPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        Mockito.verify(userService).register(username, password);
    }
    @Test
    void testPostRegisterUser_Miss() throws Exception {
        String username = "newUser";
        String password = "password";
        String confirmPassword = "password1";
        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", confirmPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Пароли не совападают"));
    }
    @Test
    void TestGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("showLogout", false));
    }
    @Test
    @WithMockUser(username = "testName")
    void GetLoginPage_Auth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("showLogout", true))
                .andExpect(model().attribute("name", name));
    }
}



