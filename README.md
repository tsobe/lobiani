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

 - Managing stock levels in the inventory
 - Providing ways of searching and browsing through the available products and purchasing them
 - Handling billing and shipping
 - Recommending products to customers based on their purchase history

# Domain model
Disclaimer: I don't have a practical experience in the eCommerce domain, so model described here may seem utterly inadequate.
However for the sake of this exercise, I believe it should be fine.   

## Bounded contexts
- Inventory - managing stock levels
- Sales - searching and collecting products to be purchased
- Billing - executing purchases initiated by customers
- Shipping - delivering purchased products
- Personalization - recommending products to customers

## Glossary
### Inventory
- Inventory - collection of items to be sold
- Operator - responsible for adding items to inventory

### Sales
- Manager - defines and publishes products
- Product - representation of saleable inventory item
- Customer - person who's willing to purchase products
- Cart - container for products to be purchased later at once

### Billing
- Account - personal and payment information about customer
- Product - pricing information about product

### Shipping
- Address - address to deliver purchased products to
- Product - description of a product to deliver
- Courier - delivers purchased products to the specified address

### Personalization
- Product - product that was purchased previously
- Customer - person who purchased product
- Recommendation - recommended product for specific customer

## Ubiquitous language (brief event storming)
- Inventory item defined
- Inventory item(s) added
- Product described in the sales
- Product published/unpublished in the sales
- Pricing info defined in Billing
- Product added to the cart
- Product removed from the cart
- Products reserved in the inventory
	- Billing information added
	- Shipping information added
		- Purchase confirmed by customer
			- Customer recommendations updated
				- Customer charged successfully
					- Courier is notified
					- Courier starts delivery
					- Customer is notified that product(s) have shipped
					- Courier updates delivery progress
					- Product(s) failed to be delivered at the specified address
						- Customer is notified
						- Reservation released in the inventory
					- Purchase delivered
						- Customer is notified - successful end of story
				- Customer charge failed
				   - Customer is notified
				   - Reservation released from the inventory
	   - Purchase cancelled by customer
	       - Reservation released from the inventory
- Products' reservation failed in the inventory
	- Customer is notified

# Backlog
- As an inventory operator I want to define an item so that actual items can be added to stock
- As an inventory operator I want to add item to stock so that it can be purchased by customers
- As a sales manager I want to describe product so that all the important information is presented to the customer
- As a sales manager I want to publish/unpublish product to control whether a product will be visible (and hence sellable) to customer
- As a billing manager I want to assign price to a product so that customer can be charged appropriately
- As a customer I want to browse through the list of all available products so that I can get some idea about what's available for sale
- As a customer I want to search a particular product(s) by name or description so that I can filter out irrelevant items
- As a customer I want to see the details of the particular product so that I can get more information
- As a customer I want to add product to the cart so that I can purchase it later
- As a customer I want to remove possibly mistakenly added product from the cart so that I can select a better option
- As a customer I want to proceed to payment so that I can see the summary before I actually pay
- As a customer I want to cancel the purchase if I changed my mind
- As a customer I want to fill billing and shipping information so that I can be charged and product can be shipped
- As a customer I want to see recommended products based on my purchase history so that suggestions are personalized for me
- As a courier I want to update delivery status so that customers see the progress

# Development process roadmap
Monolith-first approach will be used where initially entire backend system is delivered as
a single deployment unit, however it will be still modular internally, to allow easier transition
to microservices. There's gonna be a separate deployment unit for frontend (SPA).

## Milestones
### Milestone 1 objectives
- System allows to define and add inventory items to stock
- System allows to see the products available for sale
- User interface is not ugly
- System is accessible to authenticated users only
- Basic metrics such as HTTP success rate, latency and etc. are available
- Basic CI/CD pipeline exists 
    - Build and deployment is automated, preferably via GitOps approach
    - Deployments to production are made after end-to-end tests pass in test env
    - No blue/green and canary deployments
    - No automatic rollback in case of production failures

# Building and running
## See in action 
1. Build and test
    ```
    ./gradlew build
    ```
2. Start [Axon server](https://axoniq.io/)
    ```
    docker run -d --restart unless-stopped --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver:4.4
    ```
3. After running the command below, you can access the page on `http://localhost:8080/inventory-items`
    ```
    ./gradlew bootRun
    ```

## Run e2e tests
1. Download and start [selenoid](https://github.com/aerokube/selenoid) and optionally [selenoid-ui](https://github.com/aerokube/selenoid-ui)
    ```
    curl -s https://aerokube.com/cm/bash | bash \
        && ./cm selenoid start --vnc --tmpfs 128 \
        && ./cm selenoid-ui start
    ```
2. First ensure the app is running, then execute the tests 
    ```
    ./gradlew e2eTestRemote
    ```
 

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
- [Ports and Adapters](https://jmgarridopaz.github.io/content/hexagonalarchitecture.html)
- [Domain-Driven Design: Everything You Always Wanted to Know About it, But Were Afraid to Ask](https://medium.com/ssense-tech/domain-driven-design-everything-you-always-wanted-to-know-about-it-but-were-afraid-to-ask-a85e7b74497a)
- [Feature flags](https://featureflags.io/)
- [Microservices as an Evolutionary Architecture](https://www.thoughtworks.com/insights/blog/microservices-evolutionary-architecture)
