package fr.ulille.iut.pizzaland;

import org.glassfish.jersey.server.ResourceConfig;

import fr.ulille.iut.pizzaland.beans.Commande;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;
import fr.ulille.iut.pizzaland.dao.CommandeDao;
import fr.ulille.iut.pizzaland.dao.IngredientDao;
import fr.ulille.iut.pizzaland.dao.PizzaDao;
import fr.ulille.iut.pizzaland.dto.PizzaCreateDto;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("api/v1/")
public class ApiV1 extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger(ApiV1.class.getName());

    public ApiV1() {
        packages("fr.ulille.iut.pizzaland");
        String environment = System.getenv("PIZZAENV");

        if ( environment != null && environment.equals("withdb") ) {
            LOGGER.info("Loading with database");
            try {
                FileReader reader = new FileReader( getClass().getClassLoader().getResource("ingredients.json").getFile() );
                List<Ingredient> ingredients = JsonbBuilder.create().fromJson(reader, new ArrayList<Ingredient>(){}.getClass().getGenericSuperclass());
                List<Pizza> pizzas = JsonbBuilder.create().fromJson(reader, new ArrayList<Pizza>(){}.getClass().getGenericSuperclass());
                List<Commande> commandes = JsonbBuilder.create().fromJson(reader, new ArrayList<Commande>(){}.getClass().getGenericSuperclass());

                
                IngredientDao ingredientDao = BDDFactory.buildDao(IngredientDao.class);
                ingredientDao.dropTable();
                ingredientDao.createTable();
                
                for ( Ingredient ingredient: ingredients) {
                    ingredientDao.insert(ingredient); 
            }
                
                PizzaDao pizzaDao = BDDFactory.buildDao(PizzaDao.class);
                pizzaDao.dropTable();
                pizzaDao.createPizzaTable();
                
                for (Pizza pizza : pizzas) {
                    pizzaDao.insert(pizza);
                    
                }
                
                CommandeDao commandeDao = BDDFactory.buildDao(CommandeDao.class);
                commandeDao.dropTable();
                commandeDao.createTable();
                
                for (Commande commande : commandes) {
                	commandeDao.insert(commande);
                    
                }
                
            } catch ( Exception ex ) {
                throw new IllegalStateException(ex);
            }
        } 
    }
}
