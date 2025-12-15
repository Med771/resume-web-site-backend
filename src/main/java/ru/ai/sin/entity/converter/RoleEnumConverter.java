package ru.ai.sin.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.ai.sin.entity.model.RoleEnum;

@Converter(autoApply = true)
public class RoleEnumConverter implements AttributeConverter<RoleEnum, String> {

    @Override
    public String convertToDatabaseColumn(RoleEnum attribute) {
        if (attribute == null) return null;
        return attribute.getRole();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return RoleEnum.fromRole(dbData);
    }
}
