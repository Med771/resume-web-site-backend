package ru.ai.sin.dto.telegram;

import ru.ai.sin.entity.model.ResultEnum;

import java.util.List;

public record OfferFilterReq(
        boolean isStud,

        List<ResultEnum> results
) {
}
