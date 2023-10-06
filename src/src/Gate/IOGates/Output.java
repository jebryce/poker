package Gate.IOGates;


import Gate.GateType;
import Gate.IO_Gate;
import Main.Colors;
import Wire.Node.NodeType;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends IO_Gate {
    public Output( final Point2D location ) {
        super( location, GateType.OUTPUT );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        addWire( NodeType.INPUT, 0, 25 );
    }

    @Override
    public void repaint( final Graphics2D graphics2D  ) {
        if ( inputWires.getFirst().getState() ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        super.repaint( graphics2D );
    }

    @Override
    public void repaintInHand( final Graphics2D graphics2D ) {
        if ( inputWires.getFirst().getState() ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        super.repaintInHand( graphics2D );
    }
}
