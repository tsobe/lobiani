package dev.baybaydev.lobiani.testutil


import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.aggregate.ResultValidator
import org.axonframework.test.aggregate.TestExecutor
import spock.lang.Specification

class AggregateSpec extends Specification {

    private FixtureConfiguration fixture
    private TestExecutor testExecutor
    private ResultValidator resultValidator

    def <T> void useAggregate(Class<T> aggregateType, FixtureConfigurer<T> fixtureConfigurer = {}) {
        fixture = new AggregateTestFixture(aggregateType)
        fixtureConfigurer.configure(fixture)
        testExecutor = fixture.givenNoPriorActivity()
    }

    def pastEvent(event) {
        if (testExecutor == null) {
            testExecutor = fixture.given(event)
        } else {
            testExecutor.andGiven(event)
        }
    }

    def actingWith(command) {
        resultValidator = testExecutor.when(command)
    }

    def expectSuccess() {
        resultValidator.expectSuccessfulHandlerExecution()
    }

    def expectEvent(event) {
        resultValidator.expectEvents(event)
    }

    def <T> T expectException(Class<T> c) {
        def captor = new ActualCaptor<T>(c)
        resultValidator.expectException(captor)
        return captor.actualItem
    }

    def <T> void expectResultMessagePayload(T payload) {
        resultValidator.expectResultMessagePayload(payload)
    }

    def <T> T expectResultMessagePayload(Class<T> c = Object) {
        def captor = new ActualCaptor<T>(c)
        resultValidator.expectResultMessagePayloadMatching(captor)
        return captor.actualItem
    }

    interface FixtureConfigurer<T> {
        void configure(FixtureConfiguration<T> configuration)
    }
}
