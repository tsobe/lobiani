# Motivation

This project is a personal exercise to gain/improve skills in various software engineering practices, will it be coding disciplines, architecture, technologies and anything in between or around them.

# Desired areas to examine
- Event Sourcing
- DDD/CQRS
- Ports and Adapters
- TDD/BDD
- Microservices and Service Mesh
- CI/CD
- Cloud native solutions
- Machine Learning

# High level requirements
Develop eCommerce system capable of:

 - Managing stock levels in warehouse
 - Providing ways of searching and browsing through the available products and purchasing them
 - Handling billing and shipping
 - Recommending products to customers based on their purchase history

# Doman model
## Bounded contexts
Below are listed fairly good candidates for bounded contexts:

- Warehouse - managing stock levels
- Sales - searching and collecting products to be purchased
- Billing - executing purchases initiated by customers
- Shipping - delivering purchased products
- Recommender - recommending products to customers

## Ubiquitous language
### Warehouse
- Warehouse - collection of items to be sold
- Manager - person who's responsible for adding items to warehouse
- Manager adds item to inventory

### Sales
- Product - representation of saleable warehouse item
- Customer - person who's willing to purchase products
- Basket - container for products to be purchased later at once
- Customer adds product to basket
- Customer removes product from basket
- Customer proceeds to payment

### Billing
- Account - personal and payment information about customer
- Product - pricing information about product
- Product delivery is requested for provided account info
- Customer cancels the purchase

### Shipping
- Address - address do deliver purchased products
- Product - description of a product to deliver
- Product is shipped to specified address

### Recommender
- Product - product which was purchased previously
- Customer - person who purchased product
- Product is associated to customers who purchased it
- Customer has list of products they purchased
- Recommendation - recommended product for specific customer

# High level architecture
TODO

# Building and running
TODO

# Backlog
TODO

# Resources
- [Microservices, bounded context, cohesion. What do they have in common?](https://hackernoon.com/microservices-bounded-context-cohesion-what-do-they-have-in-common-1107b70342b3)
- [Introduction into Microservices](https://specify.io/concepts/microservices)
- [Example of Service Boundaries Identification Example 1](https://hackernoon.com/example-of-service-boundaries-identification-e9077c513560)
- [Example of Service Boundaries Identification Example 2](https://hackernoon.com/service-boundaries-identification-example-in-e-commerce-a2c01a1b8ee9)
- [Tackling Complexity in Microservices](https://vladikk.com/2018/02/28/microservices/)
- [Bounded Contexts are NOT Microservices](https://vladikk.com/2018/01/21/bounded-contexts-vs-microservices/)
- [Backend for frontend](https://samnewman.io/patterns/architectural/bff/)
- [Service mesh](https://www.thoughtworks.com/radar/techniques/service-mesh)
- [Scaling Microservices with an Event Stream](https://www.thoughtworks.com/de/insights/blog/scaling-microservices-event-stream)
- [Ports and Adapters](https://softwarecampament.wordpress.com/portsadapters/)
