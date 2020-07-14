package tacos.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
@Slf4j
@Repository
public class JdbcTacoRepository implements TacoRepository {

    private SimpleJdbcInsert tacoInsert;
    private JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.tacoInsert = new SimpleJdbcInsert(jdbc)
            .withTableName("taco")
            .usingGeneratedKeyColumns("id");
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());

        Map<String,Object> map = new ObjectMapper().convertValue(taco, Map.class);
        map.put("createdAt",taco.getCreatedAt());
        log.info(map.toString());
        return tacoInsert.executeAndReturnKey(map).longValue();
    }

    private void saveIngredientToTaco(
            Ingredient ingredient, long tacoId) {
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient) " +
                        "values (?, ?)",
                tacoId, ingredient.getId());
    }

}
