package Gate.IOGates;

import Gate.GateType;
import Gate.IO_Gate;
import Main.Colors;
import Main.Constants;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Point2D;

public class ChipIO extends IO_Gate {

    public ChipIO( final Point2D location, final IO_Direction direction ) {
        super( location, GateType.CHIP_IO );
        addWire( direction );
    }

    public ChipIO( final double x, final double y, final IO_Direction direction ) {
        super( x, y, GateType.CHIP_IO );
        addWire( direction );
    }

    protected Wire addWire( final IO_Direction direction ) {
        Node start;
        Node end;
        if ( direction == IO_Direction.LEFT ) {
            start = new Node( NodeType.OUTPUT, 0, 25 );
            end   = new Node( -Constants.MIN_LINE_LENGTH, 25 );
        } else {
            start = new Node( NodeType.OUTPUT, 50, 25 );
            end   = new Node( 50 + Constants.MIN_LINE_LENGTH, 25 );
        }
        Wire newWire = new Wire( start, end  );
        outputWires.add( newWire );
        return newWire;
    }

    @Override
    public void repaint( final Graphics2D graphics2D  ) {
        if ( outputWires.getFirst().getState() ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        super.repaint( graphics2D );
    }
}
