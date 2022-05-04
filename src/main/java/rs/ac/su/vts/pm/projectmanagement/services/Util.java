package rs.ac.su.vts.pm.projectmanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import rs.ac.su.vts.pm.projectmanagement.exception.ValidationException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class Util
{

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy.");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static String formatDate(Date date)
    {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDate(long timestamp)
    {
        return DATE_TIME_FORMAT.format(new Date(timestamp));
    }

    public static <T> T readValue(String value, Class<T> clazz)
    {
        try {
            return new ObjectMapper().readValue(value, clazz);
        }
        catch (JsonProcessingException e) {
            log.error("e", e);
            throw new ValidationException("invalid json");
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
    {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
