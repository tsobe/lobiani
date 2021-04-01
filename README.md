|   |   |
|---|---|
| Backend | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lobiani-app-backend&metric=alert_status)](https://sonarcloud.io/dashboard?id=lobiani-app-backend) |
| Admin | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lobiani-admin&metric=alert_status)](https://sonarcloud.io/dashboard?id=lobiani-admin) |
| Shopping | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lobiani-shopping&metric=alert_status)](https://sonarcloud.io/dashboard?id=lobiani-shopping) |
| CI     | [![CircleCI](https://circleci.com/gh/tsobe/lobiani.svg?style=shield)](https://circleci.com/gh/tsobe/lobiani) |


# Motivation
This project is a personal exercise to gain/improve skills in various software engineering practices, will it be coding 
disciplines, architecture, technologies and anything in between or around them.

# Desired areas to examine
- Event Sourcing
- DDD/CQRS
- TDD/BDD
- Microservices
- CI/CD
- Cloud native solutions
- Machine Learning

# High level requirements
Develop eCommerce system capable of:

 - Managing stock levels in the inventory
 - Providing ways of searching and browsing through the available products and purchasing them
 - Handling billing and shipping
 - Recommending products to customers based on their purchase history

# Domain model
Disclaimer: I don't have a practical experience in the eCommerce domain, so model described here may seem utterly inadequate.
However, for the sake of this exercise (despite DDD being one of the primary focus), I hope that something will work out.   

## Bounded contexts
- Inventory - managing stock levels
- Marketing - describing and presenting products to sell
- Sales - dealing with pricing rules, and the overall shopping experience 
- Billing - charging customers to execute the orders
- Shipping - delivering orders to customers
- Personalization - recommending products to customers

## Event Storming - Ubiquitous Language
[![Event Storming](doc/images/Event%20Storming.jpg)](https://miro.com/app/board/o9J_lavSVJ0=/)
(Click on the image for the interactive diagram)

# CI/CD

Continuous Deployment is implemented as a [GitOps](https://www.gitops.tech/) fashion. This monorepo contains project 
sources as well as manifest files (config).

[![Monorepo CI/CD](doc/images/Monorepo%20CICD.jpeg)](https://miro.com/app/board/o9J_lMZvG8M=/)
(Click on the image for the interactive diagram)

# Backlog
- As an inventory operator I want to define an item so that actual items can be added to stock
- As an inventory operator I want to add item to stock so that it can be purchased by customers
- As a marketing manager I want to describe product so that all the important information is presented to the customer
- As a marketing manager I want to publish/unpublish product to control whether a product will be visible (and hence sellable) to customer
- As a sales manager I want to assign price to a product so that customer can be charged appropriately
- As a customer I want to browse through the list of all available products so that I can get some idea about what's available for sale
- As a customer I want to search a particular product(s) by name or description so that I can filter out irrelevant items
- As a customer I want to see the details of the particular product so that I can get more information
- As a customer I want to add the product to the cart so that I can purchase it later
- As a customer I want to remove possibly mistakenly added product from the cart so that I can select a better option
- As a customer I want to proceed to payment so that I can see the summary before I actually pay
- As a customer I want to cancel the purchase if I changed my mind
- As a customer I want to fill billing and shipping information so that I can be charged and product can be shipped
- As a customer I want to see recommended products based on my purchase history so that suggestions are personalized for me
- As a courier I want to update delivery status so that customers see the progress

# Development process and roadmap
Monolith-first approach will be used where initially entire backend system is delivered as
a single deployment unit, however it will be still modular internally, to allow easier transition
to microservices. There's going to be a separate deployment units for the admin and frontend (SPAs).
See [ADR](doc/adr) for more info.

## Milestones
| Milestone |  High level objectives | Date |
| --- | --- | --- |
| [Milestone 1](https://github.com/tsobe/lobiani/milestone/1?closed=1) |✓ System allows to define and add inventory items to stock<br/> ✓ System allows to define and see the products available for sale<br/> ✓ User interface is not ugly<br/> ✓ Admin area is accessible to authenticated users only<br/> ✓ Basic metrics such as HTTP success rate, latency and etc. are available<br/> ✓ Basic CI/CD pipeline exists<br/> | April 1 2021 |

# Building and running
## The backend

from `app-backend` directory

1. Build and test
    ```
    ./gradlew build
    ```
2. Start [Axon server](https://axoniq.io/)
    ```
    docker run -d --restart unless-stopped --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver:4.4
    ```
3. Start the backend
    ```
    export SERVER_PORT=9090 && ./gradlew bootRun
    ```
## The admin

To build test and run, execute following commands from `admin` directory 

    npm install
    npm run test:unit
    npm run serve

## The shopping

To build test and run, execute following commands from `shopping` directory

    npm install
    npm run test
    npm run dev
    

## Run E2E tests locally

### Prerequisites
- backend, admin and shopping are running
- JDK 11 (hint [jenv](https://www.jenv.be/))

### With a remote WebDriver 
1. Download and start [selenoid](https://github.com/aerokube/selenoid) and optionally [selenoid-ui](https://github.com/aerokube/selenoid-ui)
    ```
    curl -s https://aerokube.com/cm/bash | bash \
        && ./cm selenoid start --vnc --tmpfs 128 \
        && ./cm selenoid-ui start
    ```
2. Execute the tests from `e2e-tests` directory
    ```
    ./gradlew test \
        -Dtest.admin.user=<user> \
        -Dtest.admin.password=<password>
    ```
### With a local Firefox
1. Download [geckodriver](https://github.com/mozilla/geckodriver/releases) (0.29.0 or later)
2. Execute the tests from `e2e-tests` directory
    ```
    ./gradlew testLocalFirefox \
        -Dtest.admin.user=<user> \
        -Dtest.admin.password=<password>
    ```

# Known issues
- Gradle for Groovy DSL doesn't work with JDK 14, more info [here](https://github.com/gradle/gradle/issues/10248)

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
- [Exposing CQRS Through a RESTful API](https://www.infoq.com/articles/rest-api-on-cqrs/)
- [A Comprehensive Guide to Contract Testing APIs in a Service Oriented Architecture](https://medium.com/@liran.tal/a-comprehensive-guide-to-contract-testing-apis-in-a-service-oriented-architecture-5695ccf9ac5a)
- [Strategic Domain Driven Design with Context Mapping](https://www.infoq.com/articles/ddd-contextmapping/)
- [Modelling Reactive Systems with Event Storming and Domain-Driven Design](https://blog.redelastic.com/corporate-arts-crafts-modelling-reactive-systems-with-event-storming-73c6236f5dd7)
- [CQRS: What? Why? How?](https://medium.com/@sderosiaux/cqrs-what-why-how-945543482313)
- [DDD and Messaging Architectures](https://verraes.net/2019/05/ddd-msg-arch/)
