package dev.baybay.lobiani.testutil

import dev.baybay.lobiani.app.common.AxonConfig
import org.axonframework.eventhandling.EventData
import org.axonframework.serialization.SerializedMetaData
import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.SimpleSerializedObject
import org.axonframework.serialization.upcasting.event.EventUpcaster
import org.axonframework.serialization.upcasting.event.InitialEventRepresentation
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Instant
import java.util.stream.Collectors
import java.util.stream.Stream

@SpringBootTest(classes = AxonConfig)
class UpcasterSpec extends Specification {

    @Autowired
    Serializer serializer
    private EventUpcaster upcaster

    def use(EventUpcaster upcaster) {
        this.upcaster = upcaster
    }

    protected <T> T upcastSinglePayload(SerializedObject serializedEvent) {
        def eventData = new PayloadEventData(serializedEvent)
        def upcastedRepresentation = upcastSingle new InitialEventRepresentation(eventData, serializer)
        serializer.deserialize upcastedRepresentation.data
    }

    protected IntermediateEventRepresentation upcastSingle(InitialEventRepresentation representation) {
        upcaster.upcast(Stream.of(representation))
                .collect(Collectors.toList())
                .get(0)
    }

    static class StringSerializedObject extends SimpleSerializedObject<String> {

        StringSerializedObject(String data, String type, String revision) {
            super(data, String, type, revision)
        }
    }

    static class PayloadEventData implements EventData {

        SerializedObject payload

        PayloadEventData(SerializedObject payload) {
            this.payload = payload
        }

        @Override
        String getEventIdentifier() {
            return "1"
        }

        @Override
        Instant getTimestamp() {
            return Instant.now()
        }

        @Override
        SerializedObject getMetaData() {
            return new SerializedMetaData("", String)
        }

        @Override
        SerializedObject getPayload() {
            return payload
        }
    }
}
