package GsonAdapters;

import Main.Constants;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;

public class GsonWireAdapter extends GsonAdapter< Wire > {
    protected final GsonPoint2DAdapter pointAdapter = new GsonPoint2DAdapter();

    @Override
    public void write( JsonWriter jsonWriter, Wire wire ) throws IOException {
        jsonWriter.beginArray();
        for ( Node node : wire ) {
            jsonWriter.beginObject();
            jsonWriter.name( "nodeType" ).value( node.getNodeType().ordinal() );
            jsonWriter.name( "index" ).value( wire.getIndex( node ) );
            pointAdapter.write( jsonWriter, node.getLocation() );
            jsonWriter.name( "connectedNodeIndexes" );
            jsonWriter.beginArray();
            for ( Node connectedNode : node.getNextNodes() ) {
                jsonWriter.value( wire.getIndex( connectedNode ) );
            }
            for ( Node connectedNode : node.getPreviousNodes() ) {
                jsonWriter.value( wire.getIndex( connectedNode ) );
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public Wire read( JsonReader jsonReader ) throws IOException {
        Wire     wire             = null;
        NodeType nodeType         = null;
        int      index            = -1;
        Node[]   nodes            = new Node[Constants.MAX_NUM_WIRE_NODES];
        Point2D  location;

        jsonReader.beginArray();
        while ( jsonReader.hasNext() ) {
            if ( jsonReader.peek() == JsonToken.BEGIN_OBJECT ) {
                jsonReader.beginObject();
            }
            String fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "nodeType" ) ) {
                nodeType = NodeType.values()[jsonReader.nextInt()];
            }
            fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "index" ) ) {
                index = jsonReader.nextInt();
            }
            assert index != -1;
            location = pointAdapter.read( jsonReader );
            fieldName = getNextField( jsonReader );
            int[]    connectedIndexes = new int[Constants.NUM_NEXT_NODES * 2];
            Arrays.fill( connectedIndexes, -1 );
            if ( fieldName.equals( "connectedNodeIndexes" ) ) {
                jsonReader.beginArray();
                int i = 0;
                while ( jsonReader.peek() == JsonToken.NUMBER ) {
                    connectedIndexes[i++] = jsonReader.nextInt();
                }
                jsonReader.endArray();
            }
            if ( wire == null ) {
                wire = new Wire( nodeType, location );
                wire.removeAll();
            }
            nodes[index] = wire.add( new Node( nodeType, location ) );
            for ( int connectedIndex : connectedIndexes ) {
                if ( connectedIndex == -1 ) {
                    break;
                }
                if ( nodes[connectedIndex] == null ) {
                    continue;
                }
                nodes[index].connectNode( nodes[connectedIndex] );
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();
        return wire;
    }
}
