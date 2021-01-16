package dev.baybay.lobiani.app

import org.axonframework.commandhandling.CommandBus
import org.axonframework.queryhandling.QueryGateway
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(HealthController)
class HealthControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc
    @SpringBean
    CommandBus commandBus = Mock()
    @SpringBean
    QueryGateway queryGateway = Stub() {
        query(*_) >> CompletableFuture.completedFuture([])
    }

    def "health endpoint should be publicly accessible"() {
        when:
        def response = performHealthCheck()

        then:
        ![401, 404].contains(response.status)
    }

    def "should be healthy when QueryGateway is accessible"() {
        when:
        def response = performHealthCheck()

        then:
        response.status == 200
    }

    def "should be unhealthy when QueryGateway is not accessible"() {
        when:
        def response = performHealthCheck()

        then:
        queryGateway.query(*_) >> CompletableFuture.failedFuture(new RuntimeException("Boom"))
        response.status == 500
    }

    private MockHttpServletResponse performHealthCheck() {
        mockMvc.perform(get("/healthz")).andReturn().response
    }
}
