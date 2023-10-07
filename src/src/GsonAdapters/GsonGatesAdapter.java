package GsonAdapters;

import Gate.Gate;
import Gate.Gates;
import Wire.Wire;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GsonGatesAdapter extends TypeAdapter< Gates > {
    GsonGateAdapter gateAdapter = new GsonGateAdapter();
    GsonWireAdapter wireAdapter = new GsonWireAdapter();

    @Override
    public void write( final JsonWriter jsonWriter, final Gates gates ) throws IOException {
        jsonWriter.beginArray();
        for ( Gate gate : gates ) {
            jsonWriter.beginArray();
            gateAdapter.write( jsonWriter, gate );
            for ( Wire wire : gate.getOutputWires() ) {
                wireAdapter.write( jsonWriter, wire );
            }
            jsonWriter.endArray();
        }
        jsonWriter.endArray();
    }

    @Override
    public Gates read( final JsonReader jsonReader ) throws IOException {
        Gates gates = new Gates();
        jsonReader.beginArray();
        while ( jsonReader.hasNext() ) {
            jsonReader.beginArray();
            Gate newGate = gateAdapter.read( jsonReader );
            gates.add( newGate ).place();
            while ( jsonReader.peek() == JsonToken.BEGIN_ARRAY ) {
                wireAdapter.read( jsonReader );
            }
            if ( jsonReader.peek() == JsonToken.END_ARRAY ) {
                jsonReader.endArray();
            }
        }
        jsonReader.endArray();
        return gates;
    }
}