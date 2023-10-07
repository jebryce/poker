package GsonAdapters;

import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonWireAdapter extends GsonAdapter< Wire > {
    protected final GsonPoint2DAdapter pointAdapter = new GsonPoint2DAdapter();

    @Override
    public void write( JsonWriter jsonWriter, Wire wire ) throws IOException {
        jsonWriter.beginArray();
        for ( Node node : wire ) {
            jsonWriter.beginObject();
            jsonWriter.name( "type" );
            jsonWriter.value( node.getNodeType().ordinal() );
            pointAdapter.write( jsonWriter, node.getLocation() );
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public Wire read( JsonReader jsonReader ) throws IOException {
        Wire     wire     = null;
        NodeType nodeType = null;
        Point2D  location;

        jsonReader.beginArray();
        while ( jsonReader.hasNext() ) {
            if ( jsonReader.peek() == JsonToken.BEGIN_OBJECT ) {
                jsonReader.beginObject();
            }
            String fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "type" ) ) {
                nodeType = NodeType.values()[jsonReader.nextInt()];
            }
            location = pointAdapter.read( jsonReader );
            if ( nodeType == NodeType.INPUT ) {
                wire = new Wire( nodeType, location );
                wire.remove( wire.getHead() );
                jsonReader.endObject();
                continue;
            }
            assert wire != null;
            wire.add( new Node( nodeType, location ) );
            jsonReader.endObject();

        }
        jsonReader.endArray();
        return null;
    }
}
