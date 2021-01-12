# 9. Develop admin tool as SPA

Date: 2021-01-11

## Status

Accepted

## Context

As part of the functional requirements, we need to have a means of managing the content through an intuitive web 
interface. There are couple of options to achieve this

### 1. As a "traditional" web application in the MVC architectural style, embedded within the backend (monolith)
#### Pros

- server-side rendering sometimes can provide better user experience since content is immediately visible once 
the page is loaded
- compatibility issues between frontend and backend can be detected at the earlier stage

#### Cons

- tools and libraries for backend and frontend are mixed in the same project, making it a bit messy and mentally harder to grasp
- strong coupling between web layer and backend discourages us to design general-purpose API for other types of potential consumers
- provides limited level of interactivity

### 2. As a Single Page Application in the MVVM architectural style, packaged and deployed separately from the backend
#### Pros

- frontend is more decoupled from backend technologies since they interact with each other via API
- backend API can be potentially used by other consumers too: CLI, Native & Mobile (as long as there it is general-purpose)
- frontend and backend can be delivered independently (and hence faster) from each other
- provides greater level of interactivity

#### Cons

- more pipelines need to be maintained in CI
- compatibility issues between frontend and backend may be detected later, during the integration stage

## Decision

We will go for the SPA approach

## Consequences

- initial development pace might be slower, since we will have to learn the new frontend technologies as we go
- we will explore modern frontend technologies and approaches
