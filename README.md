# Motivation

This project is a personal exercise to gain/improve skills in various software engineering practices, will it be coding disciplines, architecture, technologies and anything in between or around them.

# Desired areas to examine
- Event Sourcing
- DDD/CQRS
- Ports and Adapters
- TDD/BDD
- Microservices
- CI/CD
- Cloud native solutions
- BPMN workflows
- Machine Learning

# High level requirements
Develop eCommerce system capable of:

 - Managing stock levels in the warehouse
 - Providing ways of searching and browsing through the available products and purchasing them
 - Handling billing and shipping
 - Recommending products to customers based on their purchase history

# Doman model
## Bounded contexts
Below are bounded context candidates:

- Warehouse - managing stock levels
- Sales - searching and collecting products to be purchased
- Billing - executing purchases initiated by customers
- Shipping - delivering purchased products
- Recommender - recommending products to customers

## Ubiquitous language
### Warehouse
- Warehouse - collection of items to be sold
- Operator - responsible for adding items to warehouse

### Sales
- Manager - defines and publishes products
- Product - representation of saleable warehouse item
- Customer - person who's willing to purchase products
- Basket - container for products to be purchased later at once

### Billing
- Account - personal and payment information about customer
- Product - pricing information about product

### Shipping
- Address - address to deliver purchased products to
- Product - description of a product to deliver
- Courrier - delivers purchased products to the specified address

### Recommender
- Product - product that was purchased previously
- Customer - person who purchased product
- Recommendation - recommended product for specific customer

## Event storming
- Warehouse item defined
- Warehouse item(s) added
- Product described in the sales
- Product published/unpublished in the sales
- Pricing info defined in Billing
- Product added to the basket
- Product removed from the basket
- Products reserved in the warehouse
	- Billing information added
	- Shipping information added
		- Purchase confirmed by customer
			- Customer recommendations updated
				- Customer charged successfully
					- Courrier is notified
					- Courrier starts delivery
					- Customer is notified that product(s) have shipped
					- Courrier updates delivery progress
					- Product(s) failed to be delivered at the specified address
						- Customer is notified
						- Reservation removed in the warehouse
					- Purchase delivered
						- Customer is notified - successfull end of story
				- Customer charge failed
				   - Customer is notified
				   - Reervation removed in the warehouse
	   - Purchase cancelled by customer
- Products' reservation failed in the warehouse
	- Customer is notified




# High level architecture
TODO

# Building and running
TODO

# Backlog
- As a warehouse operator I want to define the item so that instances of items can be added later
- As a warehouse operator I want to add item so that it can be purchased by customers
- As a sales manager I want to describe product so that all the important information is presented to the customer
- As a sales manager I want to publish/unpublish product to control whether a product will be visible (and hence sellable) to customer
- As a billing manager I want to assign price to a product so that customer can be charged appropriately
- As a customer I want to browse through the list of all available products so that I can get some idea about what's available for sale
- As a customer I want to search a particular product(s) by name or description so that I can filter out irrelevant items
- As a customer I want to see the details of the particular product so that I can get more information
- As a customer I want to add product to the basket so that I can purchase it later
- As a customer I want to remove possibly mistakenly added prouct from the basket so that I can select a better option
- As a customer I want to proceed to payment so that I can see the summary before I actually pay
- As a customer I want to cancel the purchase if I changed my mind
- As a customer I want to fill billing and shipping information so that I can be charged and product is deilvered
- As a customer I want to see recommended products based on my purchase history so that suggestions are personalized for me
- As a courrier I want to update delivery status so that customers see the progress

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
