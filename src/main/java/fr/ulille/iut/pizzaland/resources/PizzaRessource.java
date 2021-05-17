package fr.ulille.iut.pizzaland.resources;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fr.ulille.iut.pizzaland.BDDFactory;
import fr.ulille.iut.pizzaland.beans.Pizza;
import fr.ulille.iut.pizzaland.dao.PizzaDao;
import fr.ulille.iut.pizzaland.dto.PizzaCreateDto;
import fr.ulille.iut.pizzaland.dto.PizzaDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/pizza")
public class PizzaRessource {

    private static final Logger LOGGER = Logger.getLogger(PizzaRessource.class.getName());

	
	private PizzaDao pizzas;
	
	@Context
	public UriInfo uriInfo;
	
	
	 public PizzaRessource() {
	    	pizzas = BDDFactory.buildDao(PizzaDao.class);
	        pizzas.createPizzaTable();

	    }

	    @GET
	    public List<PizzaDto> getAll() {
	        LOGGER.info("PizzaRessource:getAll");
	        
	        List<PizzaDto> l = pizzas.getAll().stream().map(Pizza::toDto).collect(Collectors.toList());
	        LOGGER.info(l.toString());
	        return l;
	    }
	    
	    @GET
	    @Path("{id}")
	    @Produces({"application/json", "application/xml"})
	    public PizzaDto getOnePizza(@PathParam("id") UUID id) {
	    	LOGGER.info("getOnePizza("+id+")");
	    	try {
	        	Pizza pizza = pizzas.findById(id);
	        	LOGGER.info(pizza.toString());
	        	return  Pizza.toDto(pizza);

	    	}catch(Exception e) {
	    		throw new WebApplicationException(Response.Status.NOT_FOUND);
	    	}
	    	
	    }
	    
	    
	    @POST
	    @Consumes("application/x-www-form-urlencoded")
	    public Response createPizza(@FormParam("name") String name) {
	      Pizza existing = pizzas.findByName(name);
	      if (existing != null) {
	        throw new WebApplicationException(Response.Status.CONFLICT);
	      }

	      try {
	        Pizza pizza = new Pizza();
	        pizza.setName(name);

	        pizzas.insert(pizza);

	        PizzaDto ingredientDto = Pizza.toDto(pizza);

	        URI uri = uriInfo.getAbsolutePathBuilder().path("" + pizza.getId()).build();

	        return Response.created(uri).entity(ingredientDto).build();
	      } catch (Exception e) {
	          e.printStackTrace();
	          throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
	      }
	    }

	    
	    @DELETE
	    @Path("{id}")
	    public Response deletePizza(@PathParam("id") UUID id) {
	      if ( pizzas.findById(id) == null ) {
	        throw new WebApplicationException(Response.Status.NOT_FOUND);
	      }

	      pizzas.remove(id);

	      return Response.status(Response.Status.ACCEPTED).build();
	    }
	    
	    @GET
	    @Path("{id}/name")
	    public String getPizzaName(@PathParam("id") UUID id) {
	        Pizza pizza = pizzas.findById(id);

	        if (pizza == null) {
	            throw new WebApplicationException(Response.Status.NOT_FOUND);
	        }

	        return pizza.getName();
	    }

	
	
}
