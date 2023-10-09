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
    private boolean initialized = false;

    public ChipIO( Point2D location ) {
        super( location, GateType.CHIP_IO );
    }

    public void setInput() {
        if ( initialized ) {
            return;
        }
        initialized = true;
        addWire( NodeType.OUTPUT, 50, 25 );
    }

    public void setOutput() {
        if ( initialized ) {
            return;
        }
        initialized = true;
        addWire( NodeType.OUTPUT, 0, 25 );
    }

    @Override
    protected Wire addWire( final NodeType nodeType, final double x, final double y ) {
        assert nodeType == NodeType.INPUT || nodeType == NodeType.OUTPUT :
                "NodeType needs to be either INPUT or OUTPUT to set direction of wire.";
        Node start = new Node( NodeType.OUTPUT, x, y );
        Node end;
        if ( nodeType == NodeType.INPUT ) {
            end = new Node( x - Constants.MIN_LINE_LENGTH, y );
        } else {
            end = new Node( x + Constants.MIN_LINE_LENGTH, y );
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
