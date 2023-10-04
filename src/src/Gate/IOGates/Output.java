package Gate.IOGates;


import Gate.IO_Gate;
import Node.Node;
import Node.NodeType;
import Wire.Wire;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends IO_Gate {
    public Output( final Point2D location ) {
        super( location );
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        nodes[0] = new Node( this, NodeType.INPUT, 0, 25 );
        wires[0] = new Wire( nodes[0] );
    }
}
