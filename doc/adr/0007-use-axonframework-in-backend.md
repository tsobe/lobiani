# 7. Use AxonFramework in backend

Date: 2021-01-10

## Status

Accepted

## Context

In [ADR 5](0005-use-cqrs-architectural-style.md) and [ADR 6](0006-use-ddd-tactical-design-patterns.md) we decided to 
adopt CQRS and tactical DDD patterns respectively. We need to implement them in practice

## Decision

We will use AxonFramework 

## Consequences

- Domain specific code will be coupled with AxonFramework and Spring Boot/Framework (see [ADR 4](0004-accept-coupling-with-spring-boot-in-backend.md))
- We'll focus on domain behaviour more since building blocks and common patterns are already provided by frameworks
- AxonFramework also provides great support for Event Sourcing. It is not a strict requirement though, each 
Bounded Context can decide on its own whether to be EventSourced or not 
