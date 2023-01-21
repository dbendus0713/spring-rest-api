package dy.study.springrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartArray();
    //field error
    errors.getFieldErrors().stream().forEach(v -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("field", v.getField());
        jsonGenerator.writeStringField("objectName", v.getObjectName());
        jsonGenerator.writeStringField("code", v.getCode());
        jsonGenerator.writeStringField("defaultMessage", v.getDefaultMessage());
        Object rejectedValue = v.getRejectedValue();
        if (rejectedValue != null) {
          jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
        }
        jsonGenerator.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    //global error
    errors.getGlobalErrors().forEach(v -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("objectName", v.getObjectName());
        jsonGenerator.writeStringField("code", v.getCode());
        jsonGenerator.writeStringField("defaultMessage", v.getDefaultMessage());
        jsonGenerator.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    jsonGenerator.writeEndArray();
  }
}
