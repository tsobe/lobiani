workspace "Lobiani" "Simple eCommerce system" {

    model {
        admin = person "Admin" "Administrative user"
        customer = person "Customer" "User of the public shopping system"
        authServer = softwareSystem "Auth0" "Authorization server" Existing
        lobiani = softwareSystem "Lobiani" "Main eCommerce system" {
            adminApp = container "Admin Tool" "Administrative interface for managing inventory and products" "SPA, Javascript, Vue.js" "Frontend"
            shoppingApp = container "Shopping" "Main frontend intended for public use where products are sold" "SSR, Javascript, Nuxt.js, Node.js" "Frontend"
            eventStore = container "Event Store" "Append only database for storing events" "AxonServer" "Existing, Storage"
            messageBus = container "Message Bus" "Message bus to exchange messages (commands, queries and events)" "AxonServer" "Existing, MessageBus"
            backend = container "Backend" "Modular monolith backend exposing API for Admin Tool and Shopping" "Kotlin, Spring Boot, AxonFramework" {
                securityConfig = component "Security Config" "Configures Admin area to require authentication and authorization" "Spring Configuration"

                adminProductController = component "Admin Product Controller" "Exposes Product API for Admin Tool" "Spring RestController"
                adminProductFacade = component "Admin Product Facade" "Facade for administrative behavior over Products" "Interface" "TODO"
                adminProductController -> adminProductFacade "Uses"
                securityConfig -> adminProductController "Secures"

                adminInventoryItemController = component "Admin InventoryItem Controller" "Exposes InventoryItem API for Admin Tool" "Spring RestController"
                adminInventoryItemFacade = component "Admin InventoryItem Facade" "Facade for administrative behavior over Inventory Items" "Interface" "TODO"
                adminInventoryItemController -> adminInventoryItemFacade "Uses"
                securityConfig -> adminInventoryItemController "Secures"

                shoppingProductController = component "Shopping Product Controller" "Exposes Product API for Shopping" "Spring RestController"
                shoppingProductFacade = component "Shopping Product Facade" "Facade for shopping behavior over Products" "Interface" "TODO"
                shoppingProductController -> shoppingProductFacade "Uses"
            }
            adminApp -> backend "Uses" "HTTP"
            adminApp -> authServer "Uses" "OAuth 2.0"
            shoppingApp -> backend "Uses" "HTTP"
            backend -> adminApp "Authorizes" "JWT"
            backend -> eventStore "Stored events" "gRPC"
            backend -> messageBus "Dispatches commands, queries and events" "gRPC"
        }
        admin -> lobiani "Manages"
        lobiani -> authServer "Uses"
        authServer -> admin "Authenticates"
        customer -> lobiani "Uses"
    }

    views {
        systemContext lobiani {
            include *
            autoLayout
        }

        container lobiani {
            include *
            autoLayout
        }

        component backend {
            include *
            autoLayout
        }

         styles {
            element "Software System" {
                background #1168bd
                color #ffffff
            }
            element "Existing" {
                background #666666
                color #ffffff
            }
            element "TODO" {
                background #ff6666
                color #ffffff
            }
            element "Container" {
                background #0363b8
                color #ffffff
            }
            element "Storage" {
                shape Cylinder
                background #666666
                color #ffffff
            }
            element "MessageBus" {
                shape Pipe
                background #666666
                color #ffffff
            }
            element "Frontend" {
                shape WebBrowser
                background #0363b8
                color #ffffff
            }
            element "Component" {
                shape Component
                background #0363b8
                color #ffffff
            }
            element "Person" {
                shape Person
                background #08427b
                color #ffffff
            }
        }
    }

}
