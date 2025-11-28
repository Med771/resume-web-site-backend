package ru.ai.sin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SetSkillNameReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.repository.SkillRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillServImplTest {

    @Mock
    private SkillRepo skillRepo;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillServImpl skillService;

    @Test
    void getByIdSuccess() {
        SkillEnt ent = new SkillEnt(1L, "Java", true, null);
        SkillDTO dto = new SkillDTO(1L, "Java");

        when(skillRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(ent));
        when(skillMapper.toDTO(ent)).thenReturn(dto);

        SkillDTO result = skillService.getById(1L);

        assertEquals(dto, result);
        verify(skillRepo).findByIdAndIsActiveTrue(1L);
    }

    @Test
    void getByIdNotFoundThrows() {
        when(skillRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> skillService.getById(1L));
    }

    @Test
    void createNewSkill() {
        AddSkillReq req = new AddSkillReq("Java");
        SkillEnt ent = new SkillEnt(1L, "Java", true, null);
        SkillDTO dto = new SkillDTO(1L, "Java");

        when(skillRepo.findByName("Java")).thenReturn(Optional.empty());
        when(skillMapper.toEntity(req)).thenReturn(ent);
        when(skillRepo.save(ent)).thenReturn(ent);
        when(skillMapper.toDTO(ent)).thenReturn(dto);

        SkillDTO result = skillService.create(req);

        assertEquals(dto, result);
    }

    @Test
    void createSkillAlreadyExistsReactivates() {
        AddSkillReq req = new AddSkillReq("Java");
        SkillEnt existing = new SkillEnt(1L, "Java", false, null);
        SkillDTO dto = new SkillDTO(1L, "Java");

        when(skillRepo.findByName("Java")).thenReturn(Optional.of(existing));
        when(skillRepo.save(existing)).thenReturn(existing);
        when(skillMapper.toDTO(existing)).thenReturn(dto);

        SkillDTO result = skillService.create(req);

        assertTrue(existing.getIsActive());
        assertEquals(dto, result);
    }

    @Test
    void setNameByIdSuccess() {
        SetSkillNameReq req = new SetSkillNameReq("Kotlin");
        SkillEnt ent = new SkillEnt(1L, "Java", true, null);
        SkillDTO dto = new SkillDTO(1L, "Kotlin");

        when(skillRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(ent));
        when(skillRepo.save(ent)).thenReturn(ent);
        when(skillMapper.toDTO(ent)).thenReturn(dto);

        SkillDTO result = skillService.setNameById(1L, req);

        assertEquals("Kotlin", ent.getName());
        assertEquals(dto, result);
    }

    @Test
    void deleteByIdSuccess() {
        SkillEnt ent = new SkillEnt(1L, "Java", true, null);
        SkillDTO dto = new SkillDTO(1L, "Java");

        when(skillRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(ent));
        when(skillRepo.save(ent)).thenReturn(ent);
        when(skillMapper.toDTO(ent)).thenReturn(dto);

        SkillDTO result = skillService.deleteById(1L);

        assertFalse(ent.getIsActive());
        assertEquals(dto, result);
    }
}
