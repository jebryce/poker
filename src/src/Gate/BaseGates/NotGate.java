package Gate.BaseGates;


import Gate.IO_Gate;
import Node.Node;
import Node.NodeType;
import Wire.Wire;

import java.awt.geom.Line2D;

public class NotGate extends IO_Gate {
    public NotGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 50, 30 ), false );
        body.append( new Line2D.Float( 50, 30, 0, 60 ), false );
        body.append( new Line2D.Float( 0, 60, 0, 0 ), false );

        nodes[0] = new Node( this, NodeType.INPUT, 0, 30 );
        wires[0] = new Wire( nodes[0] );
        nodes[1] = new Node( this, NodeType.OUTPUT, 50, 30 );
        wires[1] = new Wire( nodes[1] );

    }

    @Override
    public void update() {
        wires[1].setState( !wires[0].getState() );
    }
}
