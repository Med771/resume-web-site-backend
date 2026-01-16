package ru.ai.sin.dto.telegram;

import org.antlr.v4.runtime.misc.Pair;
import ru.ai.sin.entity.model.ResultEnum;

import java.util.List;

public record StatusUpdateReq(
        List<Pair<Long, ResultEnum>> newStatuses
) {
}
