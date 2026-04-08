# Guião do Vídeo — TascaEats CSS Fase 1

> - Escrever a **letra do caso de uso** no bloco de notas antes de cada demonstração
> - Manter o terminal e o Swagger sempre legíveis no ecrã
---

## PASSO 1 — Clone do projeto

Mostrar claramente o terminal e executar:

```bash
git clone https://git.alunos.di.fc.ul.pt/fc61880/tascaeats.git
cd tascaeats
```

---

## PASSO 2 — Arrancar o Docker

```bash
docker-compose up --build
```

Esperar até aparecer no terminal:

```
[DataLoader] A popular base de dados...
[DataLoader] Concluido!
```

> O seed popula automaticamente: 3 clientes, 2 admins, 2 entregadores, 2 restaurantes, 4 produtos.
> A Tasca do Brunao fica OPEN. O produto "batata frita" fica SOLD_OUT para os testes de validação.

Abrir o Swagger em: **http://localhost:8080/swagger-ui/index.html**

---

## PASSO 3 — Casos de Uso

> Para cada caso de uso: **escrever a letra no bloco de notas** antes de executar.
> Para cada validação: mostrar **primeiro o caso positivo (válido)**, depois o **negativo (inválido)**.

---

### Caso A — Login

**Escrever "A" no bloco de notas.**

**✅ Positivo** — login com user válido:
```
POST /api/users/login
{
  "userName": "moscateira1",
  "password": "naoInteressa"
}
```
> Esperado: 200 OK com dados do utilizador.

**❌ Negativo** — login com user inexistente:
```
POST /api/users/login
{
  "userName": "lalala",
  "password": "qualquer"
}
```
> Esperado: 404 Not Found.

---

### Caso B — Registar utilizadores

**Escrever "B" no bloco de notas.**

> Alguns utilizadores base já estão na BD. Aqui regista-se um novo para demonstrar o caso de uso.

**✅ Positivo** — registar novo cliente:
```
POST /api/users/register/client
{
  "username": "clienteNovo",
  "password": "123",
  "name": "Serafim Seca",
  "phoneNumber": "910000099",
  "nif": "999888777",
  "city": "Lisboa",
  "street": "rua das laranjeiras",
  "postalCode": "1000-001"
}
```
> Esperado: 200 OK com id do novo utilizador.

**❌ Negativo** — registar com username já existente:
```
POST /api/users/register/client
{
  "username": "moscateira1",
  "password": "abc",
  "name": "Duplicada",
  "phoneNumber": "910000001",
  "nif": "111111111",
  "city": "Porto",
  "street": "rua x",
  "postalCode": "4000-001"
}
```
> Esperado: 400 Bad Request — username já em uso.

---

### Caso C — Verificar, atualizar e remover utilizadores

**Escrever "C" no bloco de notas.**

**✅ Atualizar cliente** (id 1 — moscateira1):
```
PUT /api/users/client/1
{
  "username": "moscateira1",
  "password": "123456",
  "name": "Lana Melo",
  "phoneNumber": "912456322",
  "nif": "234509221",
  "city": "Ilha do Lua",
  "street": "avenida bem penteada",
  "postalCode": "2352-123"
}
```
> Esperado: 200 OK — morada atualizada de "Ilha do Sol" para "Ilha do Lua" e street de "avenida de mal penteada" para "avenida bem penteada" 

**✅ Atualizar admin** (id 4 — diabinho_joelho):
```
PUT /api/users/admin/4
{
  "username": "diabinho_no_joelho",
  "password": "hehehe",
  "name": "Brunao Panettonao",
  "phoneNumber": "911222000",
  "nif": "12225976"
}
```
> Esperado: 200 OK — username e telefone atualizados.

**✅ Atualizar entregador** (id 8 — entregador2):
```
PUT /api/users/delivery/8
{
  "username": "entregador2",
  "password": "777",
  "name": "Lana Foguete",
  "phoneNumber": "945678902",
  "nif": "100000005",
  "status": "BUSY"
}
```
> Esperado: 200 OK — entregador2 agora está BUSY.

**✅ Remover utilizador** — remover o clienteNovo criado no caso B:
```
DELETE /api/users/{id do clienteNovo}
```
> Sem body. Esperado: 204 No Content.

**❌ Negativo** — remover utilizador que não existe:
```
DELETE /api/users/9999
```
> Esperado: 404 Not Found.

---

### Caso D — Buscar restaurantes

**Escrever "D" no bloco de notas.**

**✅ Buscar por nome:**
```
GET /api/restaurants/search?designation=Tasca
```
> Esperado: lista com a Tasca do Brunao.

**✅ Buscar por cidade:**
```
GET /api/restaurants/search?city=Porto
```
> Esperado: lista com o Cantinho da budega.

---

### Caso E — Criar restaurante e gerir estado

**Escrever "E" no bloco de notas.**

> Alguns restaurantes já existem pelo seed.

```
POST /api/restaurants/
{
"designation": "Boteco da Sogra",
"status": "OPEN",
"nif": "54635353",
"city": "Viseu,
"postalCode": "1234-123",
"street": "rua da rotunda",
"adminId": 6
}

```
> Esperado: 200 OK

---

> Demonstrar a gestão de estado.

**✅ Fechar restaurante** (id 1):
```
PATCH /api/restaurants/1/status?status=CLOSED
```
> Esperado: 200 OK — Tasca do Brunao agora CLOSED.

**✅ Reabrir restaurante** (id 1) — necessário para o Caso H:
```
PATCH /api/restaurants/1/status?status=OPEN
```
> Esperado: 200 OK — Tasca do Brunao agora OPEN.

---

### Caso F — Adicionar, atualizar e remover produtos

**Escrever "F" no bloco de notas.**

**✅ Adicionar produto** ao restaurante 1:
```
POST /api/restaurants/1/products
{
  "price": 3.00,
  "description": "sumo fresco de laranja",
  "designation": "sumo de laranja",
  "availability": "AVAILABLE"
}
```
> Esperado: 200 OK — novo produto criado (guardar o id retornado).

**✅ Atualizar produto** (id 2 — batata frita, restaurante 1):
```
PUT /api/restaurants/1/products/2
{
  "price": 4.50,
  "description": "acompanhamento que brunao afinal ja aprecia",
  "designation": "dose de batata frita",
  "availability": "SOLD_OUT"
}
```
> Esperado: 200 OK — produto atualizado, continua SOLD_OUT.

**✅ Remover produto** (sumo de laranja criado acima — sem pedidos associados):
```
DELETE /api/restaurants/1/products/{id do sumo de laranja}
```
> Esperado: 204 No Content — produto eliminado da BD (hard delete porque não tem pedidos).

**❌ Negativo — soft-delete** — remover produto com pedidos associados (id 1 — arroz doce):

> Primeiro criar e pagar um pedido com produto 1 (fazer Casos H e I antes), depois voltar aqui.
```
DELETE /api/restaurants/1/products/1
```
> Esperado: 204 No Content — mas verificar no Swagger (GET /api/restaurants/1/products) que o produto já não aparece na lista

---

### Caso G — Buscar produtos de um restaurante

**Escrever "G" no bloco de notas.**

```
GET /api/restaurants/1/products
```
> Esperado: 200 OK — lista dos produtos da Tasca do Brunao.

---

### Caso H — Criar pedido

**Escrever "H" no bloco de notas.**

> Garantir que o restaurante 1 está OPEN antes deste passo.

**✅ Positivo** — pedido válido:
```
POST /api/orders
{
  "clientId": 2,
  "restaurantId": 1,
  "productIds": [1, 3]
}
```
> Esperado: 200 OK — pedido criado com estado CREATED e total 7.00 (arroz doce 2.50 + arroz 4.50).

**❌ Negativo — restaurante fechado:**

Fechar o restaurante: `PATCH /api/restaurants/1/status?status=CLOSED`

```
POST /api/orders
{
  "clientId": 1,
  "restaurantId": 1,
  "productIds": [1]
}
```
> Esperado: 400 Bad Request — não é possível fazer pedido a restaurante fechado.

Reabrir: `PATCH /api/restaurants/1/status?status=OPEN`

**❌ Negativo — produto esgotado** (produto 2 está SOLD_OUT pelo seed):
```
POST /api/orders
{
  "clientId": 1,
  "restaurantId": 1,
  "productIds": [2]
}
```
> Esperado: 400 Bad Request — produto esgotado.

---

### Caso I — Registar e processar pagamento

**Escrever "I" no bloco de notas.**

**✅ Positivo** — pagar o pedido 1:
```
POST /api/orders/payment
{
  "orderId": 1,
  "paymentMethod": "MBWAY",
  "amount": 7.00
}
```
> Esperado: 200 OK — estado do pedido passa para PAID.

**❌ Negativo — pagar pedido já pago:**
```
POST /api/orders/payment
{
  "orderId": 1,
  "paymentMethod": "CASH",
  "amount": 7.00
}
```
> Esperado: 400 Bad Request — pedido já foi pago.

---

### Caso J — Atualizar estado do pedido

**Escrever "J" no bloco de notas.**

> Progressão válida: CREATED → PAID → PREPARING → READY → DELIVERED

**✅ Positivo** — avançar estado para PREPARING (pedido 1, já está PAID):
```
PATCH /api/orders/1/status
"PREPARING"
```
> Esperado: 200 OK — estado PREPARING.

**✅** Avançar para READY:
```
PATCH /api/orders/1/status
"READY"
```
> Esperado: 200 OK — estado READY.

**❌ Negativo — saltar estado** (criar pedido 2 para este teste):
```
POST /api/orders
{
  "clientId": 3,
  "restaurantId": 1,
  "productIds": [3]
}
```
Tentar avançar de CREATED direto para PREPARING (sem pagar):
```
PATCH /api/orders/2/status
"PREPARING"
```
> Esperado: 400 Bad Request — transição de estado inválida.

---

### Caso K — Atribuir entregador

**Escrever "K" no bloco de notas.**

> O pedido 1 está READY. O entregador1 (id 7) está AVAILABLE.

**✅ Positivo:**
```
PATCH /api/orders/1/deliveryPerson/7
```
> Sem body. Esperado: 200 OK — entregador1 atribuído, fica BUSY.

**❌ Negativo — entregador BUSY** (entregador2 foi marcado BUSY no Caso C):
```
PATCH /api/orders/2/deliveryPerson/8
```
> Esperado: 400 Bad Request — entregador não está disponível.

**❌ Negativo — pedido não está READY** (pedido 2 está em CREATED):
```
PATCH /api/orders/2/deliveryPerson/7
```
> Esperado: 400 Bad Request — pedido ainda não está pronto para entrega.

---

### Caso L — Cancelar pedido

**Escrever "L" no bloco de notas.**

> Usar o pedido 2 (estado CREATED) para cancelar com sucesso.

**✅ Positivo** — cancelar pedido em CREATED:
```
DELETE /api/orders/2
```
> Esperado: 204 No Content.

**❌ Negativo — cancelar pedido em PREPARING:**

> O pedido 1 está em READY (já passou de PREPARING). Para demonstrar esta validação, criar um pedido 3, pagar, avançar para PREPARING, e tentar cancelar:

```
POST /api/orders
{ "clientId": 1, "restaurantId": 1, "productIds": [1] }
```
```
POST /api/orders/payment
{ "orderId": 3, "paymentMethod": "CASH", "amount": 2.50 }
```
```
PATCH /api/orders/3/status
"PREPARING"
```
```
DELETE /api/orders/3
```
> Esperado: 400 Bad Request — pedido em preparação não pode ser cancelado.

---

### Conclusão do fluxo principal pro cliente nao ficar sem o seu pedido claro

Entregar o pedido 1 (atualizar para DELIVERED):
```
PATCH /api/orders/1/status
"DELIVERED"
```
> Esperado: 200 OK — estado DELIVERED. O entregador1 volta a ficar AVAILABLE automaticamente.

---

## Checklist final

- [ ] Clone visível no início do vídeo
- [ ] Diretoria do `docker-compose up` visível
- [ ] Seed a correr no terminal ao arrancar
- [ ] Caso A demonstrado (positivo + negativo)
- [ ] Caso B demonstrado (positivo + negativo)
- [ ] Caso C demonstrado (atualizar cliente, admin, entregador + remover)
- [ ] Caso D demonstrado (busca por nome e por cidade)
- [ ] Caso E demonstrado (abrir e fechar restaurante)
- [ ] Caso F demonstrado (adicionar, atualizar, remover + soft-delete)
- [ ] Caso G demonstrado (listar produtos)
- [ ] Caso H demonstrado (positivo + restaurante fechado + produto esgotado)
- [ ] Caso I demonstrado (positivo + pagar duas vezes)
- [ ] Caso J demonstrado (positivo + saltar estado)
- [ ] Caso K demonstrado (positivo + entregador BUSY + pedido não READY)
- [ ] Caso L demonstrado (positivo + cancelar em PREPARING)

