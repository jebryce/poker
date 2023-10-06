package Gate.IOGates;


import Gate.IO_Gate;
import Wire.Wire;
import Wire.Node.NodeType;
import Main.Colors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Input extends IO_Gate {
    public Input( final Point2D location ) {
        super( location );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        addWire( NodeType.OUTPUT, 50, 25 );
    }

    public void flipState() {
        outputWires.getFirst().flipState();
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
