### Documentação da Dumper-API

---




###### Rotas de Ponto de descarte

- Cadastro: POST

- Rota: /savePoint

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

- Rota: /deletePoint/:_id_

Retorno: JSON

```json
{
  "message": "Ponto de ID 5d701be84fcbe000176ab481 excluído"
}
```



- Listar todos os pontos: GET

- Rota: /listPoint

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

- Rota: /validatePoint/:__id

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
