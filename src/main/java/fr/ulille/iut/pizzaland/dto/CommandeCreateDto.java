package fr.ulille.iut.pizzaland.dto;

import java.util.List;
import java.util.UUID;

import fr.ulille.iut.pizzaland.beans.Pizza;

public class CommandeCreateDto {
	private String name;
	private UUID id;
	private List<Pizza> pizzas;
	
	public CommandeCreateDto() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(List<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	

}
