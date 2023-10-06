package Gate;

import Gate.IOGates.*;
import Gate.BaseGates.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class gsonGatesAdapter extends TypeAdapter<Gates> {
    @Override
    public void write( final JsonWriter jsonWriter, final Gates gates ) throws IOException {
        jsonWriter.beginArray();
        for ( Gate gate : gates ) {
            jsonWriter.beginObject();
            jsonWriter.name( "type" );
            jsonWriter.value( gate.getGateType().ordinal() );
            jsonWriter.name( "x" );
            jsonWriter.value( gate.getLocation().getX() );
            jsonWriter.name( "y" );
            jsonWriter.value( gate.getLocation().getY() );
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public Gates read( final JsonReader jsonReader ) throws IOException {
        Gates gates = new Gates();
        jsonReader.beginArray();

        while ( jsonReader.hasNext() ) {
            GateType gateType  = null;
            Point2D  location  = null;
            if ( jsonReader.peek() == JsonToken.BEGIN_OBJECT ) {
                jsonReader.beginObject();
            }
            String fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "type" ) ) {
                gateType = GateType.values()[jsonReader.nextInt()];
            }
            fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "x" ) ) {
                location = new Point2D.Double( jsonReader.nextDouble(), 0 );
            }
            fieldName = getNextField( jsonReader );
            if ( fieldName.equals( "y" ) ) {
                assert location != null;
                location.setLocation( location.getX(), jsonReader.nextDouble() );
            }
            if ( location != null && gateType != null ) {
                addNewGate( gates, gateType, location );
                jsonReader.endObject();
            }
        }
        jsonReader.endArray();
        return gates;
    }

    private void addNewGate( final Gates gates, final GateType gateType, final Point2D location ) {
        assert gateType != GateType.NULL;
        switch ( gateType ) {
            case AND    -> gates.add( new gateAND(  location ) ).place();
            case NAND   -> gates.add( new gateNAND( location ) ).place();
            case OR     -> gates.add( new gateOR(   location ) ).place();
            case NOR    -> gates.add( new gateNOR(  location ) ).place();
            case XOR    -> gates.add( new gateXOR(  location ) ).place();
            case XNOR   -> gates.add( new gateXNOR( location ) ).place();
            case NOT    -> gates.add( new gateNOT(  location ) ).place();
            case INPUT  -> gates.add( new Input(    location ) ).place();
            case OUTPUT -> gates.add( new Output(   location ) ).place();
        }
    }

    private String getNextField( final JsonReader jsonReader ) throws IOException {
        String fieldName = null;
        JsonToken token = jsonReader.peek();
        if ( token.equals( JsonToken.NAME ) ) {
            fieldName = jsonReader.nextName();
        }
        assert fieldName != null;
        return fieldName;
    }
}