package fr.ulille.iut.pizzaland.dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import fr.ulille.iut.pizzaland.beans.Ingredient;
import fr.ulille.iut.pizzaland.beans.Pizza;
	
  public interface PizzaDao {
	
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Pizzas(id VARCHAR(128) PRIMARY KEY, name VARCHAR UNIQUE NOT NULL, ingredients VARCHAR)")
    void createPizzaTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS PizzaIngredientsAssociation .....")
    void createAssociationTable();

    @Transaction
    default void createTableAndIngredientAssociation() {
      createAssociationTable();
      createPizzaTable();
    }
    
	@SqlUpdate("DROP TABLE IF EXISTS Pizzas")
	void dropTable();
	
	@SqlUpdate("Insert INTO Pizzas(id,name,ingredients) VALUES(:id, :name, :ingredients)")
	void insert(@BindBean Pizza pizza);
	
	@SqlUpdate("DELETE FROM Pizzas WHERE id = :id")
	void remove(@Bind("id") UUID id);
	
	@SqlQuery("Select * from Pizzas where name = :name")
	@RegisterBeanMapper(Pizza.class)//permet d'associer une classe à un résultat 
	Pizza findByName(@Bind("name") String name);
	
	@SqlQuery("Select ingredients from Pizzas")
	@RegisterBeanMapper(Pizza.class)
	List<Ingredient> getAllIngredients();
	
	@SqlQuery("Select * from Pizzas")
	@RegisterBeanMapper(Pizza.class)
	List<Pizza> getAll();
	
	@SqlQuery("SELECT * from Pizzas where id = :id")
	@RegisterBeanMapper(Pizza.class)
	Pizza findById(@Bind("id") UUID id);
  }
