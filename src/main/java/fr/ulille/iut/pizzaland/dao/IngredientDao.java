package fr.ulille.iut.pizzaland.dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import fr.ulille.iut.pizzaland.beans.Ingredient;

public interface IngredientDao {
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS ingredients(id VARCHAR(128) PRIMARY KEY, name VARCHAR UNIQUE NOT NULL)")
	void createTable();
	
	@SqlUpdate("DROP TABLE IF EXISTS ingredients")
	void dropTable();
	
	@SqlUpdate("Insert INTO ingredients(id,name) VALUES(:id, :name)")
	void insert(@BindBean Ingredient ingredient);
	
	@SqlUpdate("DELETE FROM ingredients WHERE id = :id")
	void remove(@Bind("id") UUID id);
	
	@SqlQuery("Select * from ingredients where name = :name")
	@RegisterBeanMapper(Ingredient.class)//permet d'associer une classe à un résultat 
	Ingredient findByName(@Bind("name") String name);
	
	@SqlQuery("Select * from ingredients")
	@RegisterBeanMapper(Ingredient.class)
	List<Ingredient> getAll();
	
	@SqlQuery("SELECT * from ingredients where id = :id")
	@RegisterBeanMapper(Ingredient.class)
	Ingredient findById(@Bind("id") UUID id);
	


	



}








































