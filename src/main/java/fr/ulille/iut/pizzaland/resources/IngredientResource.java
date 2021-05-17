package fr.ulille.iut.pizzaland.resources;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import fr.ulille.iut.pizzaland.BDDFactory;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.dao.IngredientDao;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;

@Path("/ingredients")
public class IngredientResource {
	private static final Logger LOGGER = Logger.getLogger(IngredientResource.class.getName());

	private IngredientDao ingredients;

	@Context
	public UriInfo uriInfo;

	public IngredientResource() {
		ingredients = BDDFactory.buildDao(IngredientDao.class);
		ingredients.createTable();

	}

	@GET
	public List<IngredientDto> getAll() {
		LOGGER.info("IngredientResource:getAll");

		List<IngredientDto> l = ingredients.getAll().stream().map(Ingredient::toDto).collect(Collectors.toList());
		LOGGER.info(l.toString());
		return l;
	}

	@GET
	@Path("{id}")
	@Produces({ "application/json", "application/xml" })
	public IngredientDto getOneIngredient(@PathParam("id") UUID id) {
		LOGGER.info("getOneIngredient(" + id + ")");
		try {
			Ingredient ingredient = ingredients.findById(id);
			LOGGER.info(ingredient.toString());
			return Ingredient.toDto(ingredient);

		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

	}

	@POST
	public Response createIngredient(IngredientCreateDto ingredientCreateDto) {
		Ingredient existing = ingredients.findByName(ingredientCreateDto.getName());

		if (existing != null) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}

		try {
			Ingredient ingredient = Ingredient.fromIngredientCreateDto(ingredientCreateDto);
			ingredients.insert(ingredient);
			IngredientDto ingredientDto = Ingredient.toDto(ingredient);

			URI uri = uriInfo.getAbsolutePathBuilder().path(ingredient.getId().toString()).build();

			return Response.created(uri).entity(ingredientDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
		}
	}

	@DELETE
	@Path("{id}")
	public Response deleteIngredient(@PathParam("id") UUID id) {
		if (ingredients.findById(id) == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		ingredients.remove(id);

		return Response.status(Response.Status.ACCEPTED).build();
	}

	@GET
	@Path("{id}/name")
	public String getIngredientName(@PathParam("id") UUID id) {
		Ingredient ingredient = ingredients.findById(id);

		if (ingredient == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return ingredient.getName();
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createIngredient(@FormParam("name") String name) {
		Ingredient existing = ingredients.findByName(name);
		if (existing != null) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}

		try {
			Ingredient ingredient = new Ingredient();
			ingredient.setName(name);

			ingredients.insert(ingredient);

			IngredientDto ingredientDto = Ingredient.toDto(ingredient);

			URI uri = uriInfo.getAbsolutePathBuilder().path("" + ingredient.getId()).build();

			return Response.created(uri).entity(ingredientDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
		}
	}

}
