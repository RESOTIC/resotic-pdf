package fr.resotic.pdf.pdf.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import fr.resotic.pdf.pdf.util.Aes128Coder;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class IdDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return deserialize(jsonParser.readValueAs(String.class));
    }

    public static Integer deserialize(String id) {
        Integer result = null;
        if (id != null) {
            if (id.equals("0")) {
                return 0;
            } else {
                result = Integer.parseInt(Aes128Coder.decrypt(IdSerializer.KEY, id));
            }
        }
        return result;
    }

    public static final JacksonAnnotationIntrospector IGNORE_JSON_DESERIALIZE_ANNOTATIONS = new JacksonAnnotationIntrospector() {
        @Override
        protected <A extends Annotation> A _findAnnotation(final Annotated annotated, final Class<A> annoClass) {
            if (annoClass.equals(JsonDeserialize.class) &&
                    annotated.getAnnotation(JsonDeserialize.class) != null &&
                    (IdDeserializer.class).equals(annotated.getAnnotation(JsonDeserialize.class).using())) {
                return null;
            }
            return super._findAnnotation(annotated, annoClass);
        }
    };
}
