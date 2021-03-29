package dev.baybay.lobiani.testutil


import org.axonframework.eventhandling.*
import org.axonframework.messaging.responsetypes.ResponseType
import org.axonframework.queryhandling.DefaultQueryGateway
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.SimpleQueryBus
import org.axonframework.queryhandling.annotation.AnnotationQueryHandlerAdapter
import spock.lang.Specification

/**
 * Base abstract spec for Projections. A typical usage:
 * <p/>
 * <pre>
 * class SomeProjectionSpec extends ProjectionSpec {
 *
 *     {@literal @}Override
 *     def getProjection() {
 *         new SomeProjection()
 *     }
 *
 *     def "spec"() {
 *         given:
 *         event new SomeEvent()
 *
 *         when:
 *         def result = query new QuerySomething(), ResponseTypes.multipleInstancesOf(QueryResult)
 *
 *         then:
 *         // assert result
 *     }
 * }
 * </pre>
 */
abstract class ProjectionSpec extends Specification {

    private SimpleQueryBus queryBus = SimpleQueryBus.builder().build()
    private QueryGateway queryGateway = DefaultQueryGateway.builder()
            .queryBus(queryBus)
            .build()
    private SimpleEventBus eventBus = SimpleEventBus.builder().build()
    private EventProcessor eventProcessor

    void setup() {
        def proj = getProjection()
        new AnnotationQueryHandlerAdapter(proj).subscribe queryBus
        eventProcessor = SubscribingEventProcessor.builder()
                .messageSource(eventBus)
                .name("TestEventProcessor")
                .eventHandlerInvoker(SimpleEventHandlerInvoker.builder()
                        .eventHandlers(proj)
                        .build())
                .build()
        eventProcessor.start()
    }

    void cleanup() {
        eventProcessor.shutDown()
    }

    def event(Object event) {
        eventBus.publish new GenericEventMessage<>(event)
    }

    def <R, Q> R query(Q query, ResponseType<R> responseType) {
        queryGateway.query(query, responseType).get()
    }

    abstract def getProjection()
}
