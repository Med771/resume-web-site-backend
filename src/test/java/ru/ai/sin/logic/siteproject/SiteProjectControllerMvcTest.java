package ru.ai.sin.logic.siteproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.ai.sin.config.MethodSecurityTestConfig;
import ru.ai.sin.filter.JwtCookieAuthenticationFilter;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SiteProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MethodSecurityTestConfig.class)
class SiteProjectControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteProjectService siteProjectService;

    @MockBean
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Test
    void list_unauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    void list_okForGuest() throws Exception {
        UUID id = UUID.randomUUID();
        when(siteProjectService.listAuthenticatedVisible(true, null)).thenReturn(List.of(
                new SiteProjectDTO(id, "T", null, null, null, List.of(), List.of(), 0, false, null, null, null)
        ));
        mockMvc.perform(get("/projects").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void list_okForStudent() throws Exception {
        when(siteProjectService.listAuthenticatedVisible(false, null)).thenReturn(List.of());
        mockMvc.perform(get("/projects").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void list_okForAdmin() throws Exception {
        when(siteProjectService.listAuthenticatedVisible(true, null)).thenReturn(List.of());
        mockMvc.perform(get("/projects").with(csrf()))
                .andExpect(status().isOk());
    }
}
