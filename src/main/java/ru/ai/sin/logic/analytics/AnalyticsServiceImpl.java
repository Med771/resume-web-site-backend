package ru.ai.sin.logic.analytics;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.logic.analytics.dto.AnalyticsEventInReq;
import ru.ai.sin.logic.analytics.dto.AnalyticsPathCountRow;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryDTO;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryReq;
import ru.ai.sin.logic.registration.ClientIpResolver;
import ru.ai.sin.models.enums.AnalyticsEventType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsEventRepo analyticsEventRepo;
    private final AnalyticsMinuteIpRateLimiter analyticsMinuteIpRateLimiter;

    @Override
    @Transactional
    public void recordEvent(AnalyticsEventInReq req, HttpServletRequest httpRequest) {
        String ip = ClientIpResolver.resolve(httpRequest);
        analyticsMinuteIpRateLimiter.check(ip);

        AnalyticsEventType type;
        try {
            type = AnalyticsEventType.fromCode(req.eventType());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }

        String path = req.path().trim();
        if (path.isEmpty()) {
            throw new BadRequestException("path must not be blank");
        }
        if (path.length() > 1024) {
            path = path.substring(0, 1024);
        }

        AnalyticsEventEnt e = new AnalyticsEventEnt();
        e.setEventType(type.getCode());
        e.setPath(path);
        e.setSessionId(req.sessionId());
        e.setUserAgent(trimTo(req.userAgent(), 512));
        e.setIpHash(sha256Hex(ip));
        analyticsEventRepo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsSummaryDTO summarize(AnalyticsSummaryReq req) {
        if (!req.to().isAfter(req.from())) {
            throw new BadRequestException("Parameter 'to' must be after 'from'");
        }
        List<Object[]> rows = analyticsEventRepo.countByPathBetween(req.from(), req.to());
        List<AnalyticsPathCountRow> mapped = rows.stream()
                .map(r -> new AnalyticsPathCountRow((String) r[0], ((Number) r[1]).longValue()))
                .toList();
        return new AnalyticsSummaryDTO(mapped);
    }

    private static String trimTo(String s, int max) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max);
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
