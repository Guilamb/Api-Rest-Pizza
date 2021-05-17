package fr.ulille.iut.pizzaland.beans;

import java.util.List;
import java.util.UUID;

import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;
import fr.ulille.iut.pizzaland.dto.PizzaCreateDto;
import fr.ulille.iut.pizzaland.dto.PizzaDto;

public class Pizza {
	private UUID id = UUID.randomUUID();
	private String name;
	private List<Ingredient> ingredients;
	
	public Pizza() {

	}

	public Pizza(String name) {
		this.name = name;
	}

	public Pizza(UUID id) {
		this.id = id;
	}

	public Pizza(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public static PizzaDto toDto(Pizza i) {
		PizzaDto dto = new PizzaDto();
		dto.setId(i.getId());
		dto.setName(i.getName());
		dto.setIngredients(i.getIngredients());

		return dto;
	}

	public static Pizza fromDto(PizzaDto dto) {
		Pizza pizza = new Pizza();
		pizza.setId(dto.getId());
		pizza.setName(dto.getName());

		return pizza;
	}

	public static PizzaCreateDto toCreateDto(Pizza pizza) {
		PizzaCreateDto dto = new PizzaCreateDto();
		dto.setName(pizza.getName());

		return dto;
	}

	public static Pizza fromPizzaCreateDto(PizzaCreateDto dto) {
		Pizza pizza = new Pizza();
		pizza.setName(dto.getName());

		return pizza;
	}

	@Override
	public String toString() {
		return "Pizza [id=" + id + ", name=" + name + ", ingredients=" + ingredients + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pizza other = (Pizza) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	



}
