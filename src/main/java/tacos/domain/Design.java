package tacos.domain;

import lombok.Data;

import java.util.List;

@Data
public class Design {
    private List<String> ingredients;
    private String name;
}
