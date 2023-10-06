package Gate;

import Gate.BaseGates.*;
import Gate.IOGates.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonGateAdapter extends TypeAdapter<Gate> {
    @Override
    public void write( final JsonWriter jsonWriter, final Gate gate ) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name( "type" );
        jsonWriter.value( gate.getGateType().ordinal() );
        jsonWriter.name( "x" );
        jsonWriter.value( gate.getLocation().getX() );
        jsonWriter.name( "y" );
        jsonWriter.value( gate.getLocation().getY() );
        jsonWriter.endObject();
    }

    @Override
    public Gate read( final JsonReader jsonReader ) throws IOException {
        Gate     gate     = null;
        GateType gateType = null;
        Point2D  location = null;
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
            gate = getNewGate( gateType, location );
            jsonReader.endObject();
        }
        return gate;
    }

    private Gate getNewGate( final GateType gateType, final Point2D location ) {
        assert gateType != GateType.NULL;
        switch ( gateType ) {
            case AND    -> { return new gateAND(  location ); }
            case NAND   -> { return new gateNAND( location ); }
            case OR     -> { return new gateOR(   location ); }
            case NOR    -> { return new gateNOR(  location ); }
            case XOR    -> { return new gateXOR(  location ); }
            case XNOR   -> { return new gateXNOR( location ); }
            case NOT    -> { return new gateNOT(  location ); }
            case INPUT  -> { return new Input(    location ); }
            case OUTPUT -> { return new Output(   location ); }
            default     -> { assert false : "Invalid gateType."; }
        }
        return null;
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
