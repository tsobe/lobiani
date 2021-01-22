package dev.baybay.lobiani.app.common


import dev.baybaydev.lobiani.testutil.AggregateSpec
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateRoot
import org.axonframework.modelling.command.TargetAggregateIdentifier

import static org.axonframework.modelling.command.AggregateLifecycle.apply
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted

class AggregateNotFoundHandlerSpec extends AggregateSpec {

    def static DELETE_RETURN_VALUE = 10
    def id = "aggregate-id"

    void setup() {
        useAggregate(TestAggregate) {
            it.registerCommandHandlerInterceptor(new AggregateNotFoundHandler())
        }
    }

    def "should create new aggregate"() {
        when:
        actingWith new CreateTestAggregate(id: id)

        then:
        expectSuccess()

        and:
        expectEvent new TestAggregateCreated(id: id)
    }

    def "should delete existing aggregate"() {
        given:
        pastEvent new TestAggregateCreated(id: id)

        when:
        actingWith new DeleteTestAggregate(id: id)

        then:
        expectSuccess()

        and:
        expectEvent new TestAggregateDeleted(id: id)
    }

    def "should throw CommandExecutionException when deleting deleted aggregate"() {
        given:
        pastEvent new TestAggregateCreated(id: id)
        pastEvent new TestAggregateDeleted(id: id)

        when:
        actingWith new DeleteTestAggregate(id: id)

        then:
        expectException CommandExecutionException
    }

    def "result message payload should match value returned from command handler"() {
        given:
        pastEvent new TestAggregateCreated(id: id)

        when:
        actingWith new DeleteTestAggregate(id: id)

        then:
        expectResultMessagePayload DELETE_RETURN_VALUE
    }

    @AggregateRoot
    static class TestAggregate {

        @AggregateIdentifier
        String id

        TestAggregate() {

        }

        @CommandHandler
        TestAggregate(CreateTestAggregate create) {
            apply new TestAggregateCreated(id: create.id)
        }

        @CommandHandler
        def handleDelete(DeleteTestAggregate delete) {
            apply new TestAggregateDeleted(id: delete.id)
            return DELETE_RETURN_VALUE
        }

        @EventSourcingHandler
        def onCreate(TestAggregateCreated e) {
            id = e.id
        }

        @EventSourcingHandler
        def onDelete(TestAggregateDeleted e) {
            markDeleted()
        }
    }

    static class CreateTestAggregate {
        @TargetAggregateIdentifier
        String id
    }

    static class DeleteTestAggregate {
        @TargetAggregateIdentifier
        String id
    }

    static class TestAggregateCreated implements Serializable {
        String id
    }

    static class TestAggregateDeleted implements Serializable {
        String id
    }
}
