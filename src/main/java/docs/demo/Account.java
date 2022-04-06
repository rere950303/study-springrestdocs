package docs.demo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Account {
    private Long id;
    private String email;
    private String name;
}
