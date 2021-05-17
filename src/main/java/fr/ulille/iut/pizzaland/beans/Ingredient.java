package fr.ulille.iut.pizzaland.beans;

import java.util.UUID;

import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;

public class Ingredient {
	private UUID id = UUID.randomUUID();
	private String name;

	public Ingredient() {

	}

	public Ingredient(String name) {
		this.name = name;
	}

	public Ingredient(UUID id) {
		this.id = id;
	}

	public Ingredient(UUID id, String name) {
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

	public static IngredientDto toDto(Ingredient i) {
		IngredientDto dto = new IngredientDto();
		dto.setId(i.getId());
		dto.setName(i.getName());

		return dto;
	}

	public static Ingredient fromDto(IngredientDto dto) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId(dto.getId());
		ingredient.setName(dto.getName());

		return ingredient;
	}

	public static IngredientCreateDto toCreateDto(Ingredient ingredient) {
		IngredientCreateDto dto = new IngredientCreateDto();
		dto.setName(ingredient.getName());

		return dto;
	}

	public static Ingredient fromIngredientCreateDto(IngredientCreateDto dto) {
		Ingredient ingredient = new Ingredient();
		ingredient.setName(dto.getName());

		return ingredient;
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Ingredient other = (Ingredient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
