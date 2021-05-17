package fr.ulille.iut.pizzaland.resources;

import java.sql.SQLException;

import fr.ulille.iut.pizzaland.BDDFactory;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * BDDClearRessource
 */
@Path("clearDatabase")
public class BDDClearRessource {

    @GET
    public void clearDatabase()  throws SQLException {
        BDDFactory.dropTables();
    }
}
