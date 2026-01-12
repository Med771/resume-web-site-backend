package ru.ai.sin.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.sin.helper.model.ChatCreateRequestDto;
import ru.ai.sin.helper.model.ChatCreateResponseDto;
import ru.ai.sin.property.FastProperties;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class FastHelper {

    private final FastProperties properties;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatCreateResponseDto createChat(String chatName)
            throws Exception {
        ChatCreateRequestDto requestDto = ChatCreateRequestDto.builder().chatName(chatName).build();

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.registerModule(new ParameterNamesModule());

        String json = objectMapper.writeValueAsString(requestDto);

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(properties.getUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "HTTP " + response.statusCode() + ": " + response.body()
            );
        }

        return objectMapper.readValue(
                response.body(),
                ChatCreateResponseDto.class
        );
    }
}
