# Migration Service - pgsql4mongo

Este projeto é uma aplicação **Spring Boot** para migrar dados de um banco **PostgreSQL** para **MongoDB** de forma eficiente, 
utilizando processamento paralelo e inserção em lotes. Inclui suporte para monitoramento do progresso via API.

---

## 🛠 Funcionalidades

- **Migração paralela**: Utiliza múltiplas threads para processamento eficiente.
- **Inserções em lotes**: Manipula grandes volumes de dados de forma eficiente.
- **Monitoramento de progresso**: Consulta o número de registros processados durante a migração.
- **Configuração flexível**: Ajuste de threads e tamanho dos lotes configuráveis diretamente no código.

## 🚀 Como executar o projeto

### Pré-requisitos

-   Java 17 ou superior
- Maven 3.8+
- Banco de dados **PostgreSQL** configurado
- Banco de dados **MongoDB** configurado
- Spring Boot 3+

### Passos para execução

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/wagnerdba/pgsql4mongo.git

2. **Configure as credenciais dos bancos de dados no arquivo application.yml:**

   ```yaml
   spring:
    datasource:
      url: jdbc:postgresql://<host>:<port>/<database>
      username: <usuario>
      password: <senha>
    mongodb:
      uri: mongodb://<host>:<port>/test 
   ```
   
3.**Compile e execute a aplicação:** 

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4.**Acesse os endpoints disponíveis:**

Iniciar migração: http://localhost:8080/migrate

Acompanhar progresso: http://localhost:8080/progress

## 🛠 Dependências

As dependências configuradas no arquivo `pom.xml` incluem:

### Dependências principais

- **spring-boot-starter-data-mongodb**: Suporte para integração com MongoDB.
- **spring-boot-starter-web**: Criação de APIs RESTful.
- **spring-boot-starter-data-jpa**: Suporte para interagir com PostgreSQL via JPA.
- **postgresql**: Driver JDBC para PostgreSQL.

### Dependências de desenvolvimento

- **spring-boot-devtools**: Suporte a recarregamento automático durante o desenvolvimento.
- **lombok**: Reduz código boilerplate, como getters e setters.
- **spring-boot-starter-test**: Ferramentas para criar testes automatizados.

### Plugins configurados

- **maven-compiler-plugin**: Configuração do compilador para suportar anotações do Lombok.
- **spring-boot-maven-plugin**: Facilita o build e a execução de aplicações Spring Boot.

## Estrutura do Projeto

### 1. Controller
- **MigrationController**: Define endpoints para iniciar a migração e consultar o progresso.

### 2. Service
- **ParallelMigrationService**: Lógica de migração paralela com CompletableFuture e processamento em lotes.

### 3. Repositórios
- **SensorDataPgSqlRepository**: Manipulação de dados do PostgreSQL.
- **MongoBatchInsertService**: Inserção de dados em MongoDB.

## Endpoints da API

### 1. Iniciar migração
- **Método**: GET
- **URL**: /migrate
- **Descrição**: Inicia o processo de migração dos dados.
- **Resposta**: Confirmação de que a migração foi iniciada.

#### Exemplo de resposta:

```Migração iniciada! Para acompanhar acesse:```http://localhost:8080/progress

### 2. Consultar progresso
- **Método**: GET
- **URL**: /progress
- **Descrição**: Retorna o número de registros processados.
- **Resposta**: Número de registros processados em JSON.

#### Exemplo de resposta:

```Saída: 15000```

## Monitoramento de Migração

Durante a execução, o progresso também é exibido no console. O contador é atualizado a cada lote processado. Ao final, o tempo total de execução também é exibido.

#### Exemplo de saída no console:

```yaml
Progresso: 1000 registros processados.
Progresso: 2000 registros processados.
Migração concluída! Tempo gasto: 00:15:42. Total de registros processados: 100000
 ```

## Configurações de Migração

### Configuração de paralelismo

O número de threads pode ser ajustado no ParallelMigrationService:

```java
this.executor = Executors.newFixedThreadPool(4); // Configura 4 threads
```
### Tamanho dos lotes

O tamanho dos lotes é configurado no método migrateData():

```java
int pageSize = 1000; // Tamanho de cada lote
```

## Boas Práticas

- **Ajuste de threads**: Adeque o número de threads à capacidade do servidor.
- **Tratamento de erros**: Implemente mecanismos para lidar com falhas durante a migração.
- **Validação de dados**: Certifique-se de que os dados são consistentes antes da migração.

## Licença

Este projeto está licenciado sob a **MIT License**. Você é livre para usar e modificar conforme necessário.

## Autor

Wagner Pires - [LinkedIn](https://www.linkedin.com/in/wagner-pires-013a722b3) | [GitHub](https://github.com/wagnerdba)
