package tacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在运行时自动生成类中的SLF4J记录器
 * 相当于：
 * private static final org.slf4j.Logger log =
 *     org.slf4j.LoggerFactory.getLogger(DesignTacoController.class)
 */
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private TacoRepository designRepo;


    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository designRepo) {
        this.ingredientRepository = ingredientRepository;
        this.designRepo = designRepo;
    }



    @GetMapping
    public String showDesignForm(Model model){

        List<Ingredient> ingredients = new ArrayList<>();

        ingredientRepository.findAll().forEach(i->ingredients.add(i));

        Ingredient.Type[] types = Ingredient.Type.values();
        for(Ingredient.Type type : types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients,type));
        }

        model.addAttribute("design",new Taco());

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients.stream()
                .filter(x->x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if(errors.hasErrors()) {
            log.info("forward design");

            return "design";
        }
        // Save the taco design...
        // We'll do this in chapter 3
        log.info("Processing design: " + design);
        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }
}
