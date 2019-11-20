### Documentação da Dumper-API

---





###### Rotas de usuário

- Login: POST

- Rota: http://dumper-app.herokuapp.com/login

```json
{
	"email": "_email",
	"password" : "_password"
}
```

Retorno: True/False





- Cadastro: POST

- Rota: http://dumper-app.herokuapp.com/saveUser

```json
{
    "nome": "_nome",
	"email": "_email",
	"password" : "_password"
}
```

Retorno: JSON 

```json
{
  "usuario": {
    "_id": "5d701fe6e6d4de00178629aa",
    "nome": "Fulano",
    "email": "Fulano_@gmail.com",
    "password": "$2a$10$7LTzQgcQ2tjFF7f9oKSdsuS0aFMvpuG6LnlYFanMTHSkHCKOK9ZcS",
    "createdAt": "2019-09-04T20:34:46.971Z",
    "updatedAt": "2019-09-04T20:34:46.971Z",
    "__v": 0
  }
}
```





- Exclusão: GET

- Rota: http://dumper-app.herokuapp.com/saveUser/:_id_

Retorno: JSON

```json
{
  "message": "Usuario de ID 5d701040cdfd6200171ef968 excluído"
}
```



- Listar todos os usuários:  GET

- Rota: http://dumper-app.herokuapp.com/listUser

Retorno: JSON

```json
{
  "userList": [
    {
      "_id": "5d70189e6c846e00179a1d83",
      "nome": "Gilberto",
      "email": "gilbertocharles1@gmail.com",
      "password": "$2a$10$6p.X6ibXkM3GuO31nusvDuyzYLTfIjvwXwHbT9r0XJdy7G2qsVQcG",
      "createdAt": "2019-09-04T20:03:42.212Z",
      "updatedAt": "2019-09-04T20:03:42.212Z",
      "__v": 0
    }
  ]
}
```



###### Rotas de Ponto de descarte

- Cadastro: POST

- Rota: https://dumper-app.herokuapp.com/savePoint

```json
{
	"nome": "IESB",
	"latitude": "123",
	"longitude": "4324",
	"descricao": "Ponto de descarte de baterias próximo à biblioteca",
	"userEmail": "fulano_@gmail.com",
	"validado": "false"
}
```

Retorno: JSON

```json
{
  "pontoUsuario": {
    "_id": "5d701fafe6d4de00178629a9",
    "nome": "IESB",
    "latitude": "123",
    "longitude": "4324",
    "descricao": "Ponto de descarte de baterias próximo à biblioteca",
    "userEmail": "fulano_@gmail.com",
    "validado": false,
    "createdAt": "2019-09-04T20:33:51.726Z",
    "updatedAt": "2019-09-04T20:33:51.726Z",
    "__v": 0
  }
}
```





- Deletar ponto: GET

- Rota: https://dumper-app.herokuapp.com/deletePoint/:_id_

Retorno: JSON

```json
{
  "message": "Ponto de ID 5d701be84fcbe000176ab481 excluído"
}
```



- Listar todos os pontos: GET

- Rota: http://dumper-app.herokuapp.com/listPoint

Retorno: JSON

```json
{
  "pontosList": [
    {
      "_id": "5d701fafe6d4de00178629a9",
      "nome": "IESB",
      "latitude": "123",
      "longitude": "4324",
      "descricao": "Ponto de descarte de baterias próximo à biblioteca",
      "userEmail": "fulano_@gmail.com",
      "validado": true,
      "createdAt": "2019-09-04T20:33:51.726Z",
      "updatedAt": "2019-09-04T20:53:02.355Z",
      "__v": 0
    }
  ]
}
```



- Validar ponto: GET

- Rota: https://dumper-app.herokuapp.com/validatePoint/:__id

Retorno: JSON

```json
{
  "_id": "5d701fafe6d4de00178629a9",
  "nome": "IESB",
  "latitude": "123",
  "longitude": "4324",
  "descricao": "Ponto de descarte de baterias próximo à biblioteca",
  "userEmail": "fulano_@gmail.com",
  "validado": true,
  "createdAt": "2019-09-04T20:33:51.726Z",
  "updatedAt": "2019-09-04T20:53:02.355Z",
  "__v": 0
}
```
