package dev.baybay.lobiani.app.inventory.command

import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateLifecycle.markDeleted
import org.axonframework.modelling.command.AggregateRoot
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.Serializable

internal class AggregateNotFoundHandlerTest {

    private val id = "unique-id"
    private lateinit var fixture: FixtureConfiguration<TestAggregate>

    companion object {
        const val DELETE_RETURN_VALUE = 10
    }

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(TestAggregate::class.java)
                .registerCommandHandlerInterceptor(AggregateNotFoundHandler())
    }

    @Test
    internal fun `should create new aggregate`() {
        fixture
                .givenNoPriorActivity()
                .`when`(CreateTestAggregate(id))
                .expectSuccessfulHandlerExecution()
                .expectEvents(TestAggregateCreated(id))
    }

    @Test
    internal fun `should delete existing aggregate`() {
        fixture.given(TestAggregateCreated(id))
                .`when`(DeleteTestAggregate(id))
                .expectSuccessfulHandlerExecution()
                .expectEvents(TestAggregateDeleted(id))
    }

    @Test
    internal fun `should throw CommandExecutionException when deleting deleted aggregate`() {
        fixture.given(TestAggregateCreated(id))
                .andGiven(TestAggregateDeleted(id))
                .`when`(DeleteTestAggregate(id))
                .expectException(CommandExecutionException::class.java)
    }

    @Test
    internal fun `result message payload should match value returned from command handler`() {
        fixture.given(TestAggregateCreated(id))
                .`when`(DeleteTestAggregate(id))
                .expectResultMessagePayload(DELETE_RETURN_VALUE)
    }

    @AggregateRoot
    class TestAggregate {

        @AggregateIdentifier
        private lateinit var id: String

        constructor()

        @CommandHandler
        constructor(create: CreateTestAggregate) {
            apply(TestAggregateCreated(create.id))
        }

        @CommandHandler
        fun handleDelete(delete: DeleteTestAggregate): Int {
            apply(TestAggregateDeleted(delete.id))
            return DELETE_RETURN_VALUE
        }

        @EventSourcingHandler
        fun onCreate(e: TestAggregateCreated) {
            id = e.id
        }

        @EventSourcingHandler
        fun onDelete(e: TestAggregateDeleted) {
            markDeleted()
        }

    }

    data class CreateTestAggregate(@TargetAggregateIdentifier val id: String)

    data class DeleteTestAggregate(@TargetAggregateIdentifier val id: String)

    data class TestAggregateCreated(val id: String) : Serializable

    data class TestAggregateDeleted(val id: String) : Serializable
}
