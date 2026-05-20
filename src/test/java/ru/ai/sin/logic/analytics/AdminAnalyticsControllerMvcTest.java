package ru.ai.sin.logic.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.ai.sin.logic.analytics.dto.AnalyticsPathCountRow;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryDTO;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryReq;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminAnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MethodSecurityTestConfig.class)
class AdminAnalyticsControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnalyticsService analyticsService;

    @MockBean
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Test
    void summary_unauthorized() throws Exception {
        AnalyticsSummaryReq body = new AnalyticsSummaryReq(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        mockMvc.perform(post("/admin/analytics/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void summary_ok() throws Exception {
        when(analyticsService.summarize(any())).thenReturn(
                new AnalyticsSummaryDTO(List.of(new AnalyticsPathCountRow("/a", 3L))));
        AnalyticsSummaryReq body = new AnalyticsSummaryReq(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        mockMvc.perform(post("/admin/analytics/summary")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void summary_forbidden() throws Exception {
        AnalyticsSummaryReq body = new AnalyticsSummaryReq(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        mockMvc.perform(post("/admin/analytics/summary")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isForbidden());
    }
}
