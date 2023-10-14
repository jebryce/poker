package GsonAdapters;

import Gate.BaseGates.*;
import Gate.Gate;
import Gate.Gates;
import Gate.Chip;
import Gate.GateType;
import Gate.IOGates.*;
import Gate.IOGates.ChipIO.ChipIO;
import Gate.IOGates.ChipIO.IO_Direction;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonGateAdapter extends GsonAdapter< Gate > {
    protected final GsonPoint2DAdapter pointAdapter = new GsonPoint2DAdapter();

    @Override
    public void write( final JsonWriter jsonWriter, final Gate gate ) throws IOException {
        GateType gateType = gate.getGateType();
        jsonWriter.beginObject();
        jsonWriter.name( "gateType" );
        jsonWriter.value( gateType.ordinal() );
        pointAdapter.write( jsonWriter, gate.getLocation() );
        if ( gateType == GateType.CHIP ) {
            jsonWriter.name( "chipContents" );
            new GsonGatesAdapter().write( jsonWriter, ( (Chip) gate ).getContents() );
        } else if ( gateType == GateType.CHIP_IO ) {
            jsonWriter.name( "ioDirection" );
            jsonWriter.value( ( (ChipIO) gate).getIO_Direction().ordinal() );
        }
        jsonWriter.endObject();
    }

    @Override
    public Gate read( final JsonReader jsonReader ) throws IOException {
        Gate     gate     = null;
        GateType gateType = null;
        Point2D  location;
        Object   parameters = null;
        if ( jsonReader.peek() == JsonToken.BEGIN_OBJECT ) {
            jsonReader.beginObject();
        }
        String fieldName = getNextField( jsonReader );
        if ( fieldName.equals( "gateType" ) ) {
            gateType = GateType.values()[jsonReader.nextInt()];
        }
        location = pointAdapter.read( jsonReader );
        if ( gateType == GateType.CHIP ) {
            fieldName = getNextField( jsonReader );
            assert fieldName.equals( "chipContents" );
            parameters = new GsonGatesAdapter().read( jsonReader );
        } else if ( gateType == GateType.CHIP_IO ) {
            fieldName = getNextField( jsonReader );
            assert fieldName.equals( "ioDirection" );
            parameters = IO_Direction.values()[jsonReader.nextInt()];
        }
        if ( location != null && gateType != null ) {
            gate = getNewGate( gateType, location, parameters );
            jsonReader.endObject();
        }
        return gate;
    }

    private Gate getNewGate( final GateType gateType, final Point2D location, final Object... params ) {
        assert gateType != GateType.NULL;
        switch ( gateType ) {
            case AND     -> { return new gateAND(  location ); }
            case NAND    -> { return new gateNAND( location ); }
            case OR      -> { return new gateOR(   location ); }
            case NOR     -> { return new gateNOR(  location ); }
            case XOR     -> { return new gateXOR(  location ); }
            case XNOR    -> { return new gateXNOR( location ); }
            case NOT     -> { return new gateNOT(  location ); }
            case INPUT   -> { return new Input(    location ); }
            case OUTPUT  -> { return new Output(   location ); }
            case CHIP    -> { return new Chip(     location, (Gates) params[0] ); }
            case CHIP_IO -> { return new ChipIO(   location, (IO_Direction) params[0] ); }
            default      -> { assert false : "Invalid gateType."; }
        }
        return null;
    }
}
