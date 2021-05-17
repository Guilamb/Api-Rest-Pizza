package fr.ulille.iut.pizzaland.dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import fr.ulille.iut.pizzaland.beans.Commande;
import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;

public interface CommandeDao {
	@SqlUpdate("CREATE TABLE IF NOT EXISTS commandes(id VARCHAR(128) PRIMARY KEY, name VARCHAR UNIQUE NOT NULL, pizzas VARCHAR NOT NULL)")
	void createTable();
	
	@SqlUpdate("DROP TABLE IF EXISTS commandes")
	void dropTable();
	
	@SqlUpdate("Insert INTO commandes(id,name,pizzas) VALUES(:id, :name, :pizzas)")
	void insert(@BindBean Commande commande);
	
	@SqlUpdate("Insert INTO commandes(id,name,pizzas) VALUES(:id, :name, :pizzas) where id=:id")
	void replace(@BindBean Commande commande);
	
	@SqlUpdate("DELETE FROM commandes WHERE id = :id")
	void remove(@Bind("id") UUID id);
	
	@SqlQuery("Select * from commandes where name = :name")
	@RegisterBeanMapper(Commande.class)//permet d'associer une classe à un résultat 
	Commande findByName(@Bind("name") String name);
	
	@SqlQuery("Select * from commandes where id = :id")
	@RegisterBeanMapper(Commande.class)//permet d'associer une classe à un résultat 
	Commande findById(@Bind("id") UUID id);
	
	@SqlQuery("Select * from commandes")
	@RegisterBeanMapper(Commande.class)
	List<Commande> getAll();
	
	@SqlQuery("Select pizza from commandes")
	@RegisterBeanMapper(Commande.class)
	String getPizzas();
	
	//On va aller chercher les pizza en faisant une 2 eme requete 
	//Même si on peut en faire seulement 1.
	@SqlQuery("Select * from pizzas where name = :name")
	@RegisterBeanMapper(Commande.class)
	List<Pizza> getAllPizza(@Bind("name") String name);
}
