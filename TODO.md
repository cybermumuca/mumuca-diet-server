# Deploy

Tudo que essa API precisa para estar pronta para produção

## **1. Configurações para Produção**

### **1.1. Perfil de Produção**
- [x] Configurar um perfil específico para produção no arquivo `application-prod.yml`.

### **1.2. Configurações de Banco de Dados**
- [x] Utilizar o PostgreSQL.
- [ ] Configurar o Pool de conexões do **Hikari** para o PostgreSQL.
- [ ] Configurar o Redis.

### **1.3. Logs**
- [x] Fazer logging
- [ ] Usar Logs estruturados em JSON.

### **1.4. Migrações**
- [x] Gerar Migrations
- [ ] Ajustes finos na definição dos campos das migrations

## **2. Segurança**

### **2.1. HTTPS**
- [ ] Comprar Domínio
- [ ] Habilitar HTTPS no Spring Cloud Gateway

### **2.2. Cabeçalhos de Segurança**
- [ ] Configurar o Spring Security na API para adicionar cabeçalhos de segurança

### **2.3. Propriedades Sensíveis**
- [ ] Usar variáveis de ambiente ou um cofre de segredos.

### **2.4. Controle de Taxa de Requisição**
- [ ] Configurar um **Rate Limiter** no Spring Cloud Gateway com o Redis.

### **2.5. Logout**
- [ ] Adicionar endpoint para logout.
- [ ] Fazer uma lista negra para os JWTs revogados.

### **2.6. Segredos JWT**
- [ ] Configurar o RSA do JWT para produção.

## **3. Desempenho**

### **3.1. Compressão**
- [ ] Ativar a compressão HTTP no Spring Cloud Gateway.

### **3.2. Cache**
- [ ] Usar Redis para cachear dados na API.

### **3.3. Queries SQL**
- [ ] Fazer queries mais especificas na API.

### **3.4. Virtual Threads**
- [ ] Habilitar Threads virtuais no Gateway e na API.

## **4. Deploy**

### **4.1. Binário Nativo**
- [ ] Utilizar GraalVM para gerar um binario nativo otimizado da aplicação.

### **4.2. Docker**
- [ ] Containerizar o Spring Cloud Gateway.
- [ ] Containerizar a API.

### **4.4. Balanceador de Carga**
- [ ] Configurar um balanceador de carga.

## **5. Monitoramento e Observabilidade**

### **5.1. Monitoramento**
- [ ] Configurar o **Spring Boot Actuator**: 
- [ ] Usar Prometheus e Grafana.

## **6. Testes**

### **6.1. Testes Automatizados**
- [ ] Testes de Integração.
- [ ] Testes de Ponta a Ponta.

### **6.2. Testes em Produção**
- [] Fazer um ambiente de Staging.






## **7. CI/CD**

### 7.1. CI

- [ ] Configurar o Github Actions
- [ ] Rodar os testes no CI
- [ ] Rodar o lint do código no CI
- [ ] Rodar o lint no código das Dockerfiles

### 7.2. Dependências
- [] Configurar Dependabot para manter o projeto atualizado.