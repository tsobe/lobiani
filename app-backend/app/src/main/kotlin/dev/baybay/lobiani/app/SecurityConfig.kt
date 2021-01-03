package dev.baybay.lobiani.app

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke

@Configuration
class SecurityConfig {

    @Profile("secure")
    @Configuration
    class Secure : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity) {
            http {
                authorizeRequests {
                    authorize("/**", authenticated)
                }
                oauth2ResourceServer {
                    jwt {  }
                }
            }
        }
    }

    @Profile("!secure")
    @Configuration
    class Open : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity) {
            http {
                authorizeRequests {
                    authorize("/**", permitAll)
                }
                csrf {
                    disable()
                }
            }
        }
    }
}
