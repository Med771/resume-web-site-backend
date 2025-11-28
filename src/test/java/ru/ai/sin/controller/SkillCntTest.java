package ru.ai.sin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.service.impl.SkillService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SkillCntTest {

    @Mock
    private SkillService service;

    @InjectMocks
    private SkillCnt controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Test
    void getByIdReturnsOk() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(service.getById(1L)).thenReturn(new SkillDTO(1L, "Java"));

        mockMvc.perform(get("/skill/getById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void createReturnsCreated() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        AddSkillReq req = new AddSkillReq("Java");
        SkillDTO dto = new SkillDTO(1L, "Java");

        when(service.create(any(AddSkillReq.class))).thenReturn(dto);

        mockMvc.perform(post("/skill/create")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllReturnsList() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(service.getAll(0, 10)).thenReturn(List.of(new SkillDTO(1L, "Java")));

        mockMvc.perform(get("/skill/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }
}
