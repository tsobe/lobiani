package dev.baybay.lobiani.testutil

import org.axonframework.eventhandling.EventData
import org.axonframework.serialization.SerializedMetaData
import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.SimpleSerializedObject
import org.axonframework.serialization.upcasting.event.EventUpcaster
import org.axonframework.serialization.upcasting.event.InitialEventRepresentation
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation
import spock.lang.Specification

import java.time.Instant
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Base spec for upcasters
 */
class UpcasterSpec extends Specification {

    private Serializer serializer
    private EventUpcaster upcaster

    def use(EventUpcaster upcaster, Serializer serializer) {
        this.upcaster = upcaster
        this.serializer = serializer
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
