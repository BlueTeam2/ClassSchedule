package com.softserve.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


@Getter
public class FieldAlreadyExistsException extends RuntimeException {
    private final Class<?> clazz;
    private final String[] searchParamsMap;
    private final String shortMessage;

    public FieldAlreadyExistsException(Class<?> clazz, String... searchParamsMap) {
        super(FieldAlreadyExistsException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, (Object[]) searchParamsMap)));
        this.clazz = clazz;
        this.searchParamsMap = searchParamsMap;
        shortMessage = (StringUtils.capitalize(clazz.getSimpleName()) +
                " with provided " + toMap(String.class, String.class, (Object[]) searchParamsMap)
                .keySet() + " already exists").replaceAll("[\\[\\]]", "");
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " with parameter " + searchParams + " already exists";
    }

    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1) {
            throw new IllegalArgumentException("Invalid entries");
        }
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
