package docs.demo;

import org.springframework.restdocs.snippet.Attributes.Attribute;

public interface RestDocsSupport {

    public static Attribute field(String key, String value) {
        return new Attribute(key, value);
    }
}