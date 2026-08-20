![Java](https://img.shields.io/badge/Java-17-orange)
![JDBC](https://img.shields.io/badge/JDBC-PostgreSQL-blue)
![Maven](https://img.shields.io/badge/Build-Maven-red)
# Gerenciador de Tarefas — Projeto de Estudo em JDBC

Projeto desenvolvido como prática de estudo da API **JDBC (Java Database Connectivity)**, 
utilizando **PostgreSQL** hospedado no **Supabase** como banco de dados.

O objetivo é consolidar, na prática, os principais conceitos de acesso a dados em Java 
sem o uso de frameworks ORM (como JPA/Hibernate), entendendo o que acontece "por baixo dos panos" 
antes de trabalhar com abstrações de mais alto nível.

## Funcionalidades

- Conexão com banco de dados PostgreSQL via JDBC
- CRUD completo de usuários e tarefas
- Relacionamento entre tabelas (1:N) com consultas utilizando `JOIN`
- Validação de regras de negócio (ex: e-mail único por usuário)
- Menu interativo via terminal (`Scanner`)

## Conceitos praticados

- `Connection`, `DriverManager` e `PreparedStatement`
- Leitura de dados com `ResultSet`
- Operações de `INSERT`, `UPDATE`, `DELETE` e `SELECT`
- Recuperação de chaves geradas automaticamente (`RETURN_GENERATED_KEYS`)
- Boas práticas de segurança: uso de `PreparedStatement` (proteção contra SQL Injection) 
  e ocultação de credenciais via arquivo de configuração ignorado pelo Git

## Tecnologias

- Java
- JDBC
- PostgreSQL (Supabase) 
- Maven 

---

> Projeto criado com fins **educacionais**, como parte dos estudos da disciplina de 
> Banco de Dados II.
