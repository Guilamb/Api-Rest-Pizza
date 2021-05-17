# Projet REST avec Jersey

	
## Développement d'une ressource *ingredients*

### API et représentation des données

Nous pouvons tout d'abord réfléchir à l'API REST que nous allons offrir pour la ressource *ingredients*. Celle-ci devrait répondre aux URI suivantes :

| URI                      | Opération   | MIME                                                         | Requête         | Réponse                                                              |
| :----------------------- | :---------- | :---------------------------------------------               | :--             | :----------------------------------------------------                |
| /ingredients             | GET         | <-application/json<br><-application/xml                      |                 | liste des ingrédients (I2)                                           |
| /ingredients/{id}        | GET         | <-application/json<br><-application/xml                      |                 | un ingrédient (I2) ou 404                                            |
| /ingredients/{id}/name   | GET         | <-text/plain                                                 |                 | le nom de l'ingrédient ou 404                                        |
| /ingredients             | POST        | <-/->application/json<br>->application/x-www-form-urlencoded | Ingrédient (I1) | Nouvel ingrédient (I2)<br>409 si l'ingrédient existe déjà (même nom) |
| /ingredients/{id}        | DELETE      |                                                              |                 |                                                                      |


Un ingrédient comporte uniquement un identifiant et un nom. Sa
représentation JSON (I2) prendra donc la forme suivante :

    {
      "id": "f38806a8-7c85-49ef-980c-149dcd81d306",
      "name": "mozzarella"
    }

Lors de la création, l'identifiant n'est pas connu car il sera fourni
par le JavaBean qui représente un ingrédient. Aussi on aura une
représentation JSON (I1) qui comporte uniquement le nom :

    { "name": "mozzarella" }

