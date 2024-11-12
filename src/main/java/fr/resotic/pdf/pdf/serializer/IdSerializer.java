package fr.resotic.pdf.pdf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import fr.resotic.pdf.pdf.util.Aes128Coder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Date;

public class IdSerializer extends JsonSerializer<Integer> {

    public final static String KEY = "myKey";

    @Override
    public void serialize(Integer id, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString(serializeId(id));
    }

    public static String serializeId(Integer id) {
        String result = null;
        if (id != null) {
            if (0 == id) {
                result = "0";
            } else {
                result = Aes128Coder.encrypt(KEY, id.toString());
            }
        }
        return result;
    }

    public static final JacksonAnnotationIntrospector IGNORE_JSON_SERIALIZE_ANNOTATIONS = new JacksonAnnotationIntrospector() {
        @Override
        protected <A extends Annotation> A _findAnnotation(final Annotated annotated, final Class<A> annoClass) {
            if (annoClass.equals(JsonSerialize.class) &&
                    annotated.getAnnotation(JsonSerialize.class) != null &&
                    (IdSerializer.class).equals(annotated.getAnnotation(JsonSerialize.class).using()) &&
                    !annotated.getRawType().equals(Date.class)) {
                return null;
            }
            return super._findAnnotation(annotated, annoClass);
        }
    };
}
