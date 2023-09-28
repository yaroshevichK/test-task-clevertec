package task.clevertec.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate> {
  @Override
  public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                               final JsonSerializationContext context) {
    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
  }

//  @Override
//  public LocalDate deserialize(final JsonElement json, final Type typeOfT,
//      final JsonDeserializationContext context) throws JsonParseException {
//    return LocalDate.parse(json.getAsString(), formatter);
//  }
}