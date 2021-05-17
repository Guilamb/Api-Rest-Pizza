package fr.ulille.iut.pizzaland;

import fr.ulille.iut.pizzaland.ApiV1;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.dao.IngredientDao;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/*
 * JerseyTest facilite l'écriture des tests en donnant accès aux
 * méthodes de l'interface jakarta.ws.rs.client.Client.
 * la méthode configure() permet de démarrer la ressource à tester
 */
public class IngredientResourceTest extends JerseyTest {
    private static final Logger LOGGER = Logger.getLogger(IngredientResourceTest.class.getName());
    private IngredientDao dao;
    
    @Override
    protected Application configure() {
        return new ApiV1();
    }

    // Les méthodes setEnvUp() et tearEnvDown() serviront à terme à initialiser la
    // base de données
    // et les DAO

    // https://stackoverflow.com/questions/25906976/jerseytest-and-junit-throws-nullpointerexception
    @Before
    public void setEnvUp() {
    	dao = BDDFactory.buildDao(IngredientDao.class);
    	dao.createTable();
    }

    @After
    public void tearEnvDown() throws Exception {
    	dao.dropTable();
    }

    @Test
    public void testGetEmptyList() {
        // La méthode target() permet de préparer une requête sur une URI.
        // La classe Response permet de traiter la réponse HTTP reçue.
        Response response = target("/ingredients").request().get();

        // On vérifie le code de la réponse (200 = OK)
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // On vérifie la valeur retournée (liste vide)
        // L'entité (readEntity() correspond au corps de la réponse HTTP.
        // La classe jakarta.ws.rs.core.GenericType<T> permet de définir le type
        // de la réponse lue quand on a un type paramétré (typiquement une liste).
        List<IngredientDto> ingredients;
        ingredients = response.readEntity(new GenericType<List<IngredientDto>>() {
        });

        assertEquals(0, ingredients.size());

    }
    
    @Test
    public void testGetExistingIngredient() {
    	
    	Ingredient ingredient = new Ingredient();
    	
    	ingredient.setName("Chorizo");
    	dao.insert(ingredient);
    	
    	Response response = target("/ingredients").path(ingredient.getId().toString()).request(MediaType.APPLICATION_JSON).get();

    	assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
    	
    	
    	Ingredient result = Ingredient.fromDto(response.readEntity(IngredientDto.class));
    	assertEquals(ingredient, result);



    }
    
    
    @Test
    public void testGetNotExistingIngredient() {
    	Response response = target("/ingredients").path(UUID.randomUUID().toString()).request().get();
    	assertEquals(Response.Status.NOT_FOUND.getStatusCode(),response.getStatus());
    }
    
    
    @Test
    public void testCreateIngredient() {
    	IngredientCreateDto ingredientCreateDto = new IngredientCreateDto();
    	ingredientCreateDto.setName("Chorizo");
    	
    	Response response = target("/ingredients").request().post(Entity.json(ingredientCreateDto));
    	
    	assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    	
    	IngredientDto returnedEntity = response.readEntity(IngredientDto.class);
    	
    	assertEquals(target("/ingredients/"+returnedEntity.getId()).getUri(), response.getLocation());
    	assertEquals(returnedEntity.getName(), ingredientCreateDto.getName());
    }
    
    
    @Test
    public void testCreateSameIngredient() {
    	Ingredient ingredient = new Ingredient();
    	ingredient.setName("chorizo");
    	dao.insert(ingredient);
    	
    	IngredientCreateDto ingredientCreateDto = Ingredient.toCreateDto(ingredient);
    	Response response = target("/ingredients").request().post(Entity.json(ingredientCreateDto));
    	
    	assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testCreateIngredientWithoutName() {
    	IngredientCreateDto ingredientCreateDto = new IngredientCreateDto();
    	
    	Response response = target("/ingredients").request().post(Entity.json(ingredientCreateDto));
    	
    	assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteExistingIngredient() {
      Ingredient ingredient = new Ingredient();
      ingredient.setName("Chorizo");
      dao.insert(ingredient);

      Response response = target("/ingredients/").path(ingredient.getId().toString()).request().delete();

      assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

      Ingredient result = dao.findById(ingredient.getId());
      assertEquals(result, null);
   }

   @Test
   public void testDeleteNotExistingIngredient() {
     Response response = target("/ingredients").path(UUID.randomUUID().toString()).request().delete();
     assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
   }
   
   
   @Test
   public void testGetIngredientName() {
     Ingredient ingredient = new Ingredient();
     ingredient.setName("Chorizo");
     dao.insert(ingredient);

     Response response = target("ingredients").path(ingredient.getId().toString()).path("name").request().get();

     assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

     assertEquals("Chorizo", response.readEntity(String.class));
  }

  @Test
  public void testGetNotExistingIngredientName() {
    Response response = target("ingredients").path(UUID.randomUUID().toString()).path("name").request().get();

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  
  @Test
  public void testCreateWithForm() {
      Form form = new Form();
      form.param("name", "chorizo");

      Entity<Form> formEntity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
      Response response = target("ingredients").request().post(formEntity);

      assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
      String location = response.getHeaderString("Location");
      String id = location.substring(location.lastIndexOf('/') + 1);
      Ingredient result = dao.findById(UUID.fromString(id));

      assertNotNull(result);
  }

   
}
















































