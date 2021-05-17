package fr.ulille.iut.pizzaland;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.ulille.iut.pizzaland.beans.Commande;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;
import fr.ulille.iut.pizzaland.dao.CommandeDao;
import fr.ulille.iut.pizzaland.dao.IngredientDao;
import fr.ulille.iut.pizzaland.dto.CommandeCreateDto;
import fr.ulille.iut.pizzaland.dto.CommandeDto;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class CommandeRessourceTest extends JerseyTest{
    private static final Logger LOGGER = Logger.getLogger(CommandeRessourceTest.class.getName());
    private CommandeDao dao;
	
    @Override
    protected Application configure() {
        return new ApiV1();
    }
    
    @Before
    public void setEnvUp() {
    	dao = BDDFactory.buildDao(CommandeDao.class);
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
        Response response = target("/commandes").request().get();

        // On vérifie le code de la réponse (200 = OK)
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // On vérifie la valeur retournée (liste vide)
        // L'entité (readEntity() correspond au corps de la réponse HTTP.
        // La classe jakarta.ws.rs.core.GenericType<T> permet de définir le type
        // de la réponse lue quand on a un type paramétré (typiquement une liste).
        List<CommandeDto> commandes;
        commandes = response.readEntity(new GenericType<List<CommandeDto>>() {
        });

        assertEquals(0, commandes.size());

    }
    

    @Test
    public void testGetExistingCommande() {
    	
    	Commande commande = new Commande();
    	
    	commande.setName("Jean");
    	List<Pizza> lesPizzas = new ArrayList<Pizza>();
    	lesPizzas.add(new Pizza());
    	commande.setPizzas(lesPizzas);
    	dao.insert(commande);
    	
    	Response response = target("/commandes").path(commande.getId().toString()).request(MediaType.APPLICATION_JSON).get();

    	assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
    	
    	
    	Commande result = Commande.fromDto(response.readEntity(CommandeDto.class));
    	assertEquals(commande, result);



    }
    
    @Test
    public void testGetNotExistingCommande() {
    	Response response = target("/commandes").path(UUID.randomUUID().toString()).request().get();
    	assertEquals(Response.Status.NOT_FOUND.getStatusCode(),response.getStatus());
    }
    
    @Test
    public void testCreateCommande() {
    	CommandeCreateDto commandeCreateDto = new CommandeCreateDto();
    	commandeCreateDto.setName("Marc");
    	commandeCreateDto.setId(UUID.fromString("1234"));
    	List<Pizza> lesPizzas = new ArrayList<Pizza>();
    	lesPizzas.add(new Pizza());
    	commandeCreateDto.setPizzas(lesPizzas);
    	
    	Response response = target("/commandes").request().post(Entity.json(commandeCreateDto));
    	
    	assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    	
    	CommandeDto returnedEntity = response.readEntity(CommandeDto.class);
    	
    	assertEquals(target("/commandes/"+returnedEntity.getId()).getUri(), response.getLocation());
    	assertEquals(returnedEntity.getName(), commandeCreateDto.getName());
    }
    
    @Test
    public void testCreateSameCommande() {
    	Commande commande = new Commande();
    	commande.setId(UUID.fromString("1234"));
    	
    	dao.insert(commande);
    	
    	CommandeCreateDto commandeCreateDto = Commande.toCreateDto(commande);
    	Response response = target("/commandes").request().post(Entity.json(commandeCreateDto));
    	
    	assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testCreateCommandeWithoutPizzas() {
    	CommandeCreateDto commandeCreateDto = new CommandeCreateDto();
    	
    	Response response = target("/commandes").request().post(Entity.json(commandeCreateDto));
    	
    	assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteNotExistingCommande() {
      Response response = target("/commandes").path(UUID.randomUUID().toString()).request().delete();
      assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetCommandeName() {
      Commande commande = new Commande();
      commande.setName("Marc");
      dao.insert(commande);

      Response response = target("commandes").path(commande.getId().toString()).path("name").request().get();

      assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

      assertEquals("Marc", response.readEntity(String.class));
   }
    
    @Test
    public void testGetNotExistingCommandeName() {
      Response response = target("commandes").path(UUID.randomUUID().toString()).path("name").request().get();

      assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    

}
