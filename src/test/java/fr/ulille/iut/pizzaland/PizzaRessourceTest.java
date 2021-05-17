package fr.ulille.iut.pizzaland;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.glassfish.jersey.test.JerseyTest;

import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;
import fr.ulille.iut.pizzaland.dao.PizzaDao;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;
import fr.ulille.iut.pizzaland.dto.PizzaCreateDto;
import fr.ulille.iut.pizzaland.dto.PizzaDto;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PizzaRessourceTest extends JerseyTest{
	private static final Logger LOGGER = Logger.getLogger(PizzaRessourceTest.class.getName());
    private PizzaDao dao;
    
    
    protected Application configure() {
    	BDDFactory.setJdbiForTests();
        return new ApiV1();
        
    }
    @Before
    public void setEnvUp() {
    	dao = BDDFactory.buildDao(PizzaDao.class);
    	dao.createPizzaTable();
    }

    @After
    public void tearEnvDown() throws Exception {
    	dao.dropTable();
    }
    
    @Test
    public void testGetEmptyList() {
        // La méthode target() permet de préparer une requête sur une URI.
        // La classe Response permet de traiter la réponse HTTP reçue.
        Response response = target("/pizza").request().get();

        // On vérifie le code de la réponse (200 = OK)
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // On vérifie la valeur retournée (liste vide)
        // L'entité (readEntity() correspond au corps de la réponse HTTP.
        // La classe jakarta.ws.rs.core.GenericType<T> permet de définir le type
        // de la réponse lue quand on a un type paramétré (typiquement une liste).
        List<PizzaDto> pizzas;
        pizzas = response.readEntity(new GenericType<List<PizzaDto>>() {
        });

        assertEquals(0, pizzas.size());

    }
    
    @Test
    public void testGetExistingPizza() {
    	
    	Pizza pizza = new Pizza();
    	
    	pizza.setName("MARGHERITA");
    	dao.insert(pizza);
    	
    	Response response = target("/pizza").path(pizza.getId().toString()).request(MediaType.APPLICATION_JSON).get();

    	assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
    	
    	
    	Pizza result = Pizza.fromDto(response.readEntity(PizzaDto.class));
    	assertEquals(pizza, result);



    }
    
    @Test
    public void testGetNotExistingPizza() {
    	Response response = target("/pizza").path(UUID.randomUUID().toString()).request().get();
    	assertEquals(Response.Status.NOT_FOUND.getStatusCode(),response.getStatus());
    }
    
    @Test
    public void testCreatePizza() {
    	PizzaCreateDto pizzaCreateDto = new PizzaCreateDto();
    	pizzaCreateDto.setName("MARGHERITA");
    	
    	Response response = target("/pizza").request().post(Entity.json(pizzaCreateDto));
    	
    	assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    	
    	PizzaDto returnedEntity = response.readEntity(PizzaDto.class);
    	
    	assertEquals(target("/pizza/"+returnedEntity.getId()).getUri(), response.getLocation());
    	assertEquals(returnedEntity.getName(), pizzaCreateDto.getName());
    }
    
    @Test
    public void testCreateSamePizza() {
    	Pizza pizza = new Pizza();
    	pizza.setName("MARGHERITA");
    	dao.insert(pizza);
    	
    	PizzaCreateDto pizzaCreateDto = Pizza.toCreateDto(pizza);
    	Response response = target("/ingredients").request().post(Entity.json(pizzaCreateDto));
    	
    	assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }
    @Test
    public void testCreatePizzaWithoutName() {
    	PizzaCreateDto pizzaCreateDto = new PizzaCreateDto();
    	
    	Response response = target("/pizza").request().post(Entity.json(pizzaCreateDto));
    	
    	assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteExistingPizza() {
      Pizza pizza = new Pizza();
      pizza.setName("MARGHERITA");
      dao.insert(pizza);

      Response response = target("/pizza/").path(pizza.getId().toString()).request().delete();

      assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

      Pizza result = dao.findById(pizza.getId());
      assertEquals(result, null);
   }
    

    @Test
    public void testDeleteNotExistingPizza() {
      Response response = target("/pizza").path(UUID.randomUUID().toString()).request().delete();
      assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    
    @Test
    public void testGetPizzaName() {
      Pizza pizza = new Pizza();
      pizza.setName("MARGHERITA");
      dao.insert(pizza);

      Response response = target("pizza").path(pizza.getId().toString()).path("name").request().get();

      assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

      assertEquals("MARGHERITA", response.readEntity(String.class));
   }

   @Test
   public void testGetNotExistingPizzaName() {
     Response response = target("pizzas").path(UUID.randomUUID().toString()).path("name").request().get();

     assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
   }
   
   @Test
   public void testCreateWithForm() {
       Form form = new Form();
       form.param("name", "MARGHERITA");

       Entity<Form> formEntity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
       Response response = target("pizza").request().post(formEntity);

       assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
       String location = response.getHeaderString("Location");
       String id = location.substring(location.lastIndexOf('/') + 1);
       Pizza result = dao.findById(UUID.fromString(id));

       assertNotNull(result);
   }
}
