package ru.ai.sin.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.ai.sin.entity.model.ResultEnum;

@Converter(autoApply = true)
public class ResultEnumConverter implements AttributeConverter<ResultEnum, String> {
    @Override
    public String convertToDatabaseColumn(ResultEnum attribute) {
        if (attribute == null) return null;
        return attribute.getResult();
    }

    @Override
    public ResultEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return ResultEnum.fromCourse(dbData);
    }
}
