






# URI


| URI                      | Opération   | MIME                                                         | Requête         | Réponse                                                              |
| :----------------------- | :---------- | :---------------------------------------------               | :--             | :----------------------------------------------------                |
| /pizza             | GET         | <-application/json<br><-application/xml                      |                 | liste des pizzas (I2)                                           |
/pizza/ingredients             | GET         | <-application/json<br><-application/xml                      |                 | liste des ingredients de pizza (I2)                                           |
| /pizza/(id)    | GET         | <-application/json<br><-application/xml                      |                 | une pizza (I2) ou 404                                            |
| /pizza/(id)/name  | GET         | <-text/plain                                                 |                 | le nom de la pizza ou 404                                        |
| /pizza             | POST        | <-/->application/json<br>->application/x-www-form-urlencoded |  | Nouvelle pizza (I2)<br>409 si l'ingrédient existe déjà (même nom) |
| /pizza/(id)        | DELETE      |                                                              |                 |                   supprime la pizza                                                   |



# Representation

Une pizza comporte uniquement un identifiant, un nom et des ingredients. Sa
représentation JSON (I2) prendra donc la forme suivante :

    {
      "id": "f38806a8-7c85-49ef-980c-149dcd81d306",
      "name": "Margarita",
      "liste des ingrédients": "Sauce tomate, Mozzarella, Basilic, Huile d’olive"
    }

# classes

public PizzaDto getOnePizza(@PathParam("id") UUID id)

public Response createPizza(@FormParam("name") String name)

public List<PizzaDto> getAll()

public Response deletePizza(@PathParam("id") UUID id)

public String getPizzaName(@PathParam("id") UUID id)




