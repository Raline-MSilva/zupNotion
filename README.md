# zupNotion

#### Essa API foi desenvolvida para o gerenciamento de tarefas em um projeto e conta com dois perfis de usários:

#### ADMIN
- Tem permissão para cadastrar outro ADMIN
- Alterar sua senha
- Cadastrar tarefas unitárias 
- Cadastrar tarefas massivas através de um arquivo .csv
- Exibir lista de tarefas
- Exibir lista de usuários
- Atribuir a tarefa para algum usuário
- Atualizar informações da tarefa
- Remover tarefas ou usuários do sistema

#### USER
- Tem permissão para se cadastrar
- Alterar sua senha
- Visualizar suas tarefas
- Alterar o status da tarefa

## Regras de Negócio
- Somente email zup é aceito no cadastro
- Email repetido não é aceito em novo cadastro
- A senha deve ser forte

## Como Rodar a API localmente

> Pré-requisitos:

- JAVA JDK
- Maven
- MariaDB/MySQL

[Link para instalar o Maven](https://maven.apache.org/download.cgi)

Após instalar as dependencias através do terminal na pasta do projeto voce deve agora ser capaz de iniciar a aplicaçao na IDE. 

Será possível testar a aplicaçao em: localhost:8080/

## Tecnologias utilizadas

- JAVA 11
- Springboot
- Spring Security
- JPA
- Hibernate
- Swagger
- Maven

## Serviços

Funcionalidades documentadas via Swagger e Postman Collection.

- Swagger:  http://localhost:8080/swagger-ui/index.html

- Postman collection: [Zup-Notion.postman_collection.zip](https://github.com/Raline1/zupNotion/blob/main/Zup-Notion.postman_collection.zip)


