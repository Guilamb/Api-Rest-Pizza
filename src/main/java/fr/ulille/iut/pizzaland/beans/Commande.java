package fr.ulille.iut.pizzaland.beans;

import java.util.List;
import java.util.UUID;

import fr.ulille.iut.pizzaland.dto.CommandeCreateDto;
import fr.ulille.iut.pizzaland.dto.CommandeDto;
import fr.ulille.iut.pizzaland.dto.IngredientCreateDto;
import fr.ulille.iut.pizzaland.dto.IngredientDto;

public class Commande {
	private String name;
	private UUID id = UUID.randomUUID();
	private List<Pizza> pizzas;
	
	
	public Commande() {
		
	}
	public Commande(String name, List<Pizza> pizzas) {
		this.name = name;
		this.pizzas = pizzas;
		
	}
	public Commande(UUID id, List<Pizza> pizzas) {
		this.id = id;
		this.pizzas = pizzas;
	}
	
	public Commande(String name,UUID id, List<Pizza> pizzas) {
		this.id = id;
		this.name = name;
		this.pizzas = pizzas;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public List<Pizza> getPizzas() {
		return pizzas;
	}
	public void setPizzas(List<Pizza> pizzas) {
		this.pizzas = pizzas;
	}
	@Override
	public String toString() {
		return "Commande [name=" + name + ", id=" + id + ", pizzas=" + pizzas + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pizzas == null) ? 0 : pizzas.hashCode());
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
		Commande other = (Commande) obj;
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
		if (pizzas == null) {
			if (other.pizzas != null)
				return false;
		} else if (!pizzas.equals(other.pizzas))
			return false;
		return true;
	}
	
	public static CommandeDto toDto(Commande i) {
		CommandeDto dto = new CommandeDto();
		dto.setId(i.getId());
		dto.setName(i.getName());
		dto.setPizzas(i.getPizzas());

		return dto;
	}

	public static Commande fromDto(CommandeDto dto) {
		Commande commande = new Commande();
		commande.setId(dto.getId());
		commande.setName(dto.getName());
		commande.setPizzas(dto.getPizzas());

		return commande;
	}

	public static CommandeCreateDto toCreateDto(Commande commande) {
		CommandeCreateDto dto = new CommandeCreateDto();
		dto.setName(commande.getName());
		dto.setPizzas(commande.getPizzas());

		return dto;
	}

	public static Commande fromCommandeCreateDto(CommandeCreateDto dto) {
		Commande commande = new Commande();
		commande.setName(dto.getName());
		commande.setPizzas(dto.getPizzas());

		return commande;
	}

	

}
