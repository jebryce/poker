package Gate;

import Gate.IOGates.*;
import Gate.BaseGates.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonGatesAdapter extends TypeAdapter<Gates> {
    GsonGateAdapter gateAdapter = new GsonGateAdapter();

    @Override
    public void write( final JsonWriter jsonWriter, final Gates gates ) throws IOException {
        jsonWriter.beginArray();
        for ( Gate gate : gates ) {
            gateAdapter.write( jsonWriter, gate );
        }
        jsonWriter.endArray();
    }

    @Override
    public Gates read( final JsonReader jsonReader ) throws IOException {
        Gates gates = new Gates();
        jsonReader.beginArray();
        while ( jsonReader.hasNext() ) {
            Gate newGate = gateAdapter.read( jsonReader );
            gates.add( newGate ).place();
        }
        jsonReader.endArray();
        return gates;
    }
}
