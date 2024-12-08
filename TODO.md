# Meal Log

Registros de refeições feitas pelo usuário

`GET /meal-logs/{id}` - Retorna um registro de refeição específico  
`GET /meal-logs?date={string}` - Retorna registros de refeições com base na data fornecida  
`GET /meal-logs/{id}/foods` - Retorna os alimentos de um registro de refeição específico  
`GET /meal-logs/{id}/meals` - Retorna as refeições de um registro de refeição específico  
`GET /meal-logs/{id}/nutritional-info` - Retorna as informações nutricionais de um registro de refeição específico  
`POST /meal-logs` - Cria um novo registro de refeição  
`POST /meal-logs/{id}/foods` - Adiciona alimentos a um registro de refeição específico  
`POST /meal-logs/{id}/meals` - Adiciona refeições a um registro de refeição específico  
`PUT /meal-logs/{id}` - Atualiza um registro de refeição específico  
`DELETE /meal-logs/{id}` - Deleta um registro de refeição específico  
`DELETE /meal-logs/{id}/foods` - Deleta os alimentos de um registro de refeição específico  
`DELETE /meal-logs/{id}/meals` - Deleta as refeições de um registro de refeição específico  


