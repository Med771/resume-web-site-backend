package ru.ai.sin.dto.skill;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SetSkillReqTest {

    private final Validator validator;

    SetSkillReqTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validName() {
        SetSkillNameReq req = new SetSkillNameReq("Java");
        assertThat(validator.validate(req)).isEmpty();
    }

    @Test
    void blankNameShouldFail() {
        SetSkillNameReq req = new SetSkillNameReq("");
        assertThat(validator.validate(req)).isNotEmpty();
    }

    @Test
    void tooLongNameShouldFail() {
        SetSkillNameReq req = new SetSkillNameReq("X".repeat(256));
        assertThat(validator.validate(req)).isNotEmpty();
    }
}
