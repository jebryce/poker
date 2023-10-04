package Gate.IOGates;


import Gate.IO_Gate;
import Node.Node;
import Node.NodeType;
import Wire.Wire;

import java.awt.geom.Ellipse2D;

public class Input extends IO_Gate {
    public Input( final int x, final int y ) {
        super( x, y);
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        nodes[0] = new Node( this, NodeType.OUTPUT, 50, 25 );
        wires[0] = new Wire( nodes[0] );
    }

    @Override
    public void select() {
        if ( wires[0] == null ) {
            return;
        }
        wires[0].flipState();
    }
}
