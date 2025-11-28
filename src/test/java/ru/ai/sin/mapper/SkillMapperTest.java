package ru.ai.sin.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.SkillEnt;

import static org.assertj.core.api.Assertions.assertThat;

class SkillMapperTest {

    private SkillMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SkillMapper.class);
    }

    @Test
    void toEntityShouldMapCorrectly() {
        AddSkillReq req = new AddSkillReq("Java");

        SkillEnt entity = mapper.toEntity(req);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Java");

        // Проверяем, что ignored поля заполнены
        assertThat(entity.getIsActive()).isNotNull();
        assertThat(entity.getTimestamps()).isNotNull();

        // Проверяем, что ignored поля не заполнены
        assertThat(entity.getId()).isNull();

        assertThat(entity.getTimestamps().getCreatedAt()).isNull();
        assertThat(entity.getTimestamps().getUpdatedAt()).isNull();
    }

    @Test
    void toEntityWithNullShouldReturnNull() {
        SkillEnt entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void toDTOShouldMapCorrectly() {
        SkillEnt entity = new SkillEnt();
        entity.setId(1L);
        entity.setName("Python");
        entity.setIsActive(true);

        SkillDTO dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Python");
    }

    @Test
    void toDTOWithNullEntityShouldReturnNull() {
        SkillDTO dto = mapper.toDTO(null);
        assertThat(dto).isNull();
    }
}