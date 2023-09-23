package Gate.BaseGates;


import Gate.Gate;
import Node.Node;
import Node.NodeType;
import Wire.Wire;

import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class NandGate extends Gate {

    public NandGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 75, 0 ), false );
        body.append( new Arc2D.Float( 25, 0, 100, 100, 270, 180, Arc2D.OPEN ), false );
        body.append( new Line2D.Float( 0, 100, 75, 100 ), false );
        body.append( new Line2D.Float( 0, 100, 0, 0 ), false );

        body.append( new Ellipse2D.Float( 125, 40, 20, 20 ), false );

        nodes[0] = new Node( this, NodeType.INPUT, 45, 30 );
        wires[0] = new Wire( nodes[0] );
        nodes[1] = new Node( this, NodeType.INPUT, 45, 70 );
        wires[1] = new Wire( nodes[1] );

        nodes[2] = new Node( this, NodeType.OUTPUT, 145, 50 );
        wires[2] = new Wire( nodes[2] );
    }

    @Override
    public void update() {
        wires[2].setState( !( wires[0].getState() && wires[1].getState() ) );
    }
}
