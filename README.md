# Motivation

This project is a personal exercise to gain/improve skills in various software engineering practices, will it be coding disciplines, architecture, technologies and anything in between or around them.

# Desired areas to examine
- Event Sourcing
- DDD/CQRS
- Machine Learning
- Microservices
- TDD/BDD
- CI/CD
- Monitoring

# High level requirements
Develop eCommerce system capable of:

 - Managing inventory items
 - Providing ways of searching and browsing through the available products and purchasing them
 - Recommending products to customers based on their purchase history

# Doman model
## Bounded contexts
Following are listed fairly good candidates for bounded contexts (clear from the requirements):

- Inventory - managing inventory items
- Sales - searching and purchasing products
- Recommender - recommending products to customers

## Ubiquitous language
### Inventory
- Inventory - collection of potentially saleable items
- Manager - person who's responsible of adding items to inventory and then updating them as needed
- Manager adds item(s) to inventory
- Manager marks item(s) as saleable/unsaleable

### Sales
- Product - representation of saleable inventory item
- Customer - person who's willing to purchase products
- Basket - container for products to be purchased later at once
- Customer adds product to basket
- Customer removes product from basket
- Customer purchases product(s) from basket 

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
- [Example of Service Boundaries Identification Part 1](https://hackernoon.com/example-of-service-boundaries-identification-e9077c513560)
- [Example of Service Boundaries Identification Part 2](https://hackernoon.com/service-boundaries-identification-example-in-e-commerce-a2c01a1b8ee9)
- [Tackling Complexity in Microservices](https://vladikk.com/2018/02/28/microservices/)
- [Bounded Contexts are NOT Microservices](https://vladikk.com/2018/01/21/bounded-contexts-vs-microservices/)