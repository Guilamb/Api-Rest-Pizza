package fr.ulille.iut.pizzaland.resources;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fr.ulille.iut.pizzaland.BDDFactory;
import fr.ulille.iut.pizzaland.beans.Commande;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;
import fr.ulille.iut.pizzaland.dao.CommandeDao;
import fr.ulille.iut.pizzaland.dao.IngredientDao;
import fr.ulille.iut.pizzaland.dto.CommandeCreateDto;
import fr.ulille.iut.pizzaland.dto.CommandeDto;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/commandes")
public class CommandeRessource {
	private static final Logger LOGGER = Logger.getLogger(CommandeRessource.class.getName());

	private CommandeDao commandes;
	
	@Context
	public UriInfo uriInfo;
	
	public CommandeRessource() {
		commandes = BDDFactory.buildDao(CommandeDao.class);
		commandes.createTable();
	}
	
	@GET
	public List<CommandeDto> getAll() {
		LOGGER.info("CommandeResource:getAll");

		List<CommandeDto> l = commandes.getAll().stream().map(Commande::toDto).collect(Collectors.toList());
		LOGGER.info(l.toString());
		return l;
	}
	
	@GET
	@Path("{id}")
	@Produces({ "application/json", "application/xml" })
	public CommandeDto getOneCommande(@PathParam("id") UUID id) {
		LOGGER.info("getOneCommande(" + id + ")");
		try {
			Commande commande = commandes.findById(id);
			LOGGER.info(commande.toString());
			return Commande.toDto(commande);

		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Path("{id}/name")
	public String getCommandeName(@PathParam("id") UUID id) {
		Commande commande = commandes.findById(id);

		if (commande == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return commande.getName();
	}
	
	@POST
	public Response createCommande(CommandeCreateDto commandeCreateDto) {
		Commande existing = commandes.findById(commandeCreateDto.getId());

		if (existing != null) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}

		try {
			Commande commande = Commande.fromCommandeCreateDto(commandeCreateDto);
			commandes.insert(commande);
			CommandeDto commandeDto = Commande.toDto(commande);

			URI uri = uriInfo.getAbsolutePathBuilder().path(commande.getId().toString()).build();

			return Response.created(uri).entity(commandeDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
		}
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteCommande(@PathParam("id") UUID id) {
		if (commandes.findById(id) == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		commandes.remove(id);

		return Response.status(Response.Status.ACCEPTED).build();
	}
	
	@DELETE
	@Path("{id}/{pizza}")
	public Response deleteCommande(@PathParam("id") UUID id, @PathParam("pizza") String pizza) {
		if (commandes.findById(id) == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		Commande commande = commandes.findById(id);
		List<Pizza> lesPizzas = commande.getPizzas();
		lesPizzas.remove(Pizza.toCreateDto(new Pizza(pizza)));
		
		commandes.replace(new Commande(commande.getName(), commande.getId(), lesPizzas));
		

		return Response.status(Response.Status.ACCEPTED).build();
	}

}
