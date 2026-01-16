package ru.ai.sin.dto.telegram;

import ru.ai.sin.entity.model.ResultEnum;

import java.util.List;

public record StatusUpdateReq(
        List<Pair> newStatuses
) {
    public record Pair(
            Long id,
            ResultEnum result
    ) { }
}
