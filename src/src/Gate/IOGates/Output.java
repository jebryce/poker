package Gate.IOGates;


import Gate.IO_Gate;
import Node.Node;
import Node.NodeType;

import java.awt.geom.Ellipse2D;

public class Output extends IO_Gate {
    public Output( final int x, final int y ) {
        super( x, y);
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        nodes[0] = new Node( this, NodeType.INPUT, 0, 25 );
    }
}
