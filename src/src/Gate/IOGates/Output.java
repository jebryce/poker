package Gate.IOGates;


import Gate.IO_Gate;
import Wire.Node.NodeType;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends IO_Gate {
    public Output( final Point2D location ) {
        super( location );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        addWire( NodeType.INPUT, 0, 25 );
    }
}
