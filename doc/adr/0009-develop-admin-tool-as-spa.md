# 9. Develop admin tool as SPA

Date: 2021-01-11

## Status

Accepted

## Context

As part of the functional requirements, we need to have a means of managing the content through an intuitive web 
interface. There are couple of options to achieve this:

### 1. As a "traditional" web application, packaged and deployed together with the backend
#### Pros

- server-side rendering sometimes can provide better user experience since all the content is immediately visible once 
the page is loaded
- compatibility issues can be detected at the earlier stages

#### Cons

- tools and libraries for backend and frontend are mixed in the same project, making it a bit messy and mentally hard to grasp
- entire 

### 2. Single Page Application, packaged and deployed separately from the backend
#### Pros

- frontend is more decoupled from backend technologies since they interact with each other via web API
- backend API can be potentially used by other consumers too: CLI, Native & Mobile (as long as there it is general-purpose)
- frontend and backend can be delivered independently (and hence faster) from each other

#### Cons

- more pipelines need to be maintained
- compatibility issues may be detected at the later stages, during the integration

## Decision

The change that we're proposing or have agreed to implement.

## Consequences

What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated.
