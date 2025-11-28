package ru.ai.sin.dto.skill;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddSkillReqTest {

    private final Validator validator;

    AddSkillReqTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validName() {
        AddSkillReq req = new AddSkillReq("Java");
        assertThat(validator.validate(req)).isEmpty();
    }

    @Test
    void blankNameShouldFail() {
        AddSkillReq req = new AddSkillReq("");
        assertThat(validator.validate(req)).isNotEmpty();
    }

    @Test
    void tooLongNameShouldFail() {
        AddSkillReq req = new AddSkillReq("X".repeat(256));
        assertThat(validator.validate(req)).isNotEmpty();
    }
}
