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

 - adding items to inventory
 - providing ways of searching and browsing through the available products and purchasing them
 - recommending products to customers based on their purchase history

## User stories

- As an inventory manager I want to add item(s) to the inventory so that make them available for sale
- As an inventory manager I want to mark item(s) saleable/unsaleable to control which items can be sold 
- As a customer I want to add product to basket so that they can be purchased later
- As a customer I want to remove product from basket to control the products I want to purchase 
- As a customer I want to purchase products from the basket

# Doman model
## Bounded contexts
Following are listed fairly good candidates for bounded contexts (clear from the requirements):

- Inventory - adding items to the inventory
- Sales - searching and purchasing products
- Recommender - recommending products to customers

## Ubiquitous language
### Inventory
- Inventory - collection of potentially saleable items
- Manager - person who's responsible of adding items to inventory
- Manager adds item(s) to inventory
- Manager marks item(s) as saleable/unsaleable

### Sales
- Product - representation of saleable inventory item
- Customer - person who's willing to purchase products
- Basket - container for products to be purchased at once
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
