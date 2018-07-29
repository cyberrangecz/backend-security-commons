package cz.muni.ics.kypo.commons.rest.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author Pavel Šeda
 *
 */
public class JsonRawValueDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(JsonParser jp, DeserializationContext context) throws IOException {
    return jp.readValueAsTree().toString();
  }
}
