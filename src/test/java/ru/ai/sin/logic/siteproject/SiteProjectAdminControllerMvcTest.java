package ru.ai.sin.logic.siteproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.ai.sin.config.MethodSecurityTestConfig;
import ru.ai.sin.filter.JwtCookieAuthenticationFilter;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectStudentsReq;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SiteProjectAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MethodSecurityTestConfig.class)
class SiteProjectAdminControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteProjectService siteProjectService;

    @MockBean
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Test
    void list_forbiddenWithoutAuth() throws Exception {
        mockMvc.perform(get("/admin/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void list_ok() throws Exception {
        UUID id = UUID.randomUUID();
        when(siteProjectService.listAdminOrdered(null)).thenReturn(List.of(
                new SiteProjectDTO(id, "T", null, null, null, List.of(), List.of(), 0, true, null, null, null)
        ));
        mockMvc.perform(get("/admin/projects").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    void list_forbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin/projects").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listStudents_ok() throws Exception {
        UUID projectId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        when(siteProjectService.listStudentIds(projectId)).thenReturn(List.of(studentId));

        mockMvc.perform(get("/admin/projects/" + projectId + "/students").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(studentId.toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void bindStudents_ok() throws Exception {
        UUID projectId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(post("/admin/projects/" + projectId + "/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentIds\":[\"" + studentId + "\"]}"))
                .andExpect(status().isNoContent());

        verify(siteProjectService).bindStudents(projectId, new SiteProjectStudentsReq(List.of(studentId)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unbindStudents_ok() throws Exception {
        UUID projectId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(delete("/admin/projects/" + projectId + "/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentIds\":[\"" + studentId + "\"]}"))
                .andExpect(status().isNoContent());

        verify(siteProjectService).unbindStudents(projectId, new SiteProjectStudentsReq(List.of(studentId)));
    }
}
