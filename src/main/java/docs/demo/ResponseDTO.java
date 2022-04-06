package docs.demo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseDTO {
    Map<String, Object> data = new HashMap<>();
}
