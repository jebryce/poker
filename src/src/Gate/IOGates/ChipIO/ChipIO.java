package Gate.IOGates.ChipIO;

import Gate.GateType;
import Gate.IO_Gate;
import Main.Constants;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.geom.Point2D;

public class ChipIO extends IO_Gate {
    private       IO_Type      ioType      = IO_Type.INPUT;
    private final IO_Direction ioDirection;

    public ChipIO( final double x, final double y, final IO_Direction direction ) {
        super( x, y, GateType.CHIP_IO );
        ioDirection = direction;
        addWire();
    }

    public ChipIO( final Point2D location, final IO_Direction direction ) {
        super( location, GateType.CHIP_IO );
        ioDirection = direction;
        addWire();
    }

    protected void addWire() {
        if ( ioDirection == IO_Direction.LEFT ) {
            Node start = new Node( NodeType.INPUT, 0, 25 );
            Node end   = new Node( -Constants.MIN_LINE_LENGTH, 25 );
            ioType = IO_Type.INPUT;
            Wire newWire = new Wire( start, end  );
            inputWires.add( newWire );
        } else {
            Node start = new Node( NodeType.OUTPUT, 50, 25 );
            Node end   = new Node( 50 + Constants.MIN_LINE_LENGTH, 25 );
            ioType = IO_Type.OUTPUT;
            Wire newWire = new Wire( start, end  );
            outputWires.add( newWire );
        }
    }

    @Override
    public void update() {
        if ( ioType == IO_Type.INPUT ) {
            state = inputWires.getFirst().getState();
        }
        else {
            outputWires.getFirst().setState( state );
        }
    }

    public void setState( final boolean newState ) {
        state = newState;
    }

    public boolean getState() {
        return state;
    }

    public IO_Type getIO_Type() {
        return ioType;
    }

    public IO_Direction getIO_Direction() {
        return ioDirection;
    }
}
