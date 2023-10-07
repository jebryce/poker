package GsonAdapters;

import Gate.BaseGates.*;
import Gate.Gate;
import Gate.GateType;
import Gate.IOGates.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonGateAdapter extends GsonAdapter< Gate > {
    protected final GsonPoint2DAdapter pointAdapter = new GsonPoint2DAdapter();

    @Override
    public void write( final JsonWriter jsonWriter, final Gate gate ) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name( "type" );
        jsonWriter.value( gate.getGateType().ordinal() );
        pointAdapter.write( jsonWriter, gate.getLocation() );
        jsonWriter.endObject();
    }

    @Override
    public Gate read( final JsonReader jsonReader ) throws IOException {
        Gate     gate     = null;
        GateType gateType = null;
        Point2D  location;
        if ( jsonReader.peek() == JsonToken.BEGIN_OBJECT ) {
            jsonReader.beginObject();
        }
        String fieldName = getNextField( jsonReader );
        if ( fieldName.equals( "type" ) ) {
            gateType = GateType.values()[jsonReader.nextInt()];
        }
        location = pointAdapter.read( jsonReader );
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
}
