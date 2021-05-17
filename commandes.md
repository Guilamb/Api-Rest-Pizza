# URI


| URI                      | Opération   | MIME                                                         | Requête         | Réponse                                                              |
| :----------------------- | :---------- | :---------------------------------------------               | :--             | :----------------------------------------------------                |
| /commandes             | GET         | <-application/json<br><-application/xml                      |                 | liste des commandes (I2)                                           |
/commandes/pizza             | GET         | <-application/json<br><-application/xml                      |                 | liste des commandes de la pizza spécifié(I2)                                           |
| /commandes/(id)    | GET         | <-application/json<br><-application/xml                      |                 | une commande (I2) ou 404                                            |
| /commandes/(id)/name  | GET         | <-text/plain                                                 |                 | le nom de la personne qui a commandé ou 404                                        |
| /commande             | POST        | <-/->application/json<br>->application/x-www-form-urlencoded |  | Nouvelle commande (I2) |
| /commandes/(id)        | DELETE      |                                                              |                 |                   supprime la commande                                                   |
| /commandes/(id)/pizza        | DELETE      |                                                              |                 |                   supprime la pizza de la commande                                                   |


# Representation

Une commande comporte uniquement un identifiant, un nom et des pizzas. Sa
représentation JSON (I2) prendra donc la forme suivante :

    {
      "id": "f38806a8-7c85-49ef-980c-149dcd81d306",
      "name": "Jean René",
      "liste des pizzas": "Margarita, Rene, 4 fromages, Margarita"
    }

Si on prends plusieurs fois une même pizza, elle sera ajouté à la liste. En effet, il est rare de   
prendre 6x la même pizza donc autant ne pas s'embêter avec une map. 

# classes

public PizzaDto getCommande(@PathParam("id") UUID id)

public Response createCommande(@FormParam("name") String name, @FormParam("pizzas") List<Pizza> pizzas)

public List<CommandeDto> getAll()


public Response deleteCommande(@PathParam("id") UUID id)

public Response deleteCommandePizza(@PathParam("id") UUID id, @PathParam("pizzaName")  String pizza)

public String getCommandeName(@PathParam("id") UUID id)