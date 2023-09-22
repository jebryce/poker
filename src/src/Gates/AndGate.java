package Gates;


import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public class AndGate extends Gate {

    public AndGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 75, 0 ), false );
        body.append( new Arc2D.Float( 25, 0, 100, 100, 270, 180, Arc2D.OPEN ), false );
        body.append( new Line2D.Float( 0, 100, 75, 100 ), false );
        body.append( new Line2D.Float( 0, 100, 0, 0 ), false );

        input_nodes[0]  = new Node( this, 0, 30 );
        inputs[0]       = new Wire( input_nodes[0] );
        input_nodes[1]  = new Node( this, 0, 70 );
        inputs[1]       = new Wire( input_nodes[1] );

        output_nodes[0] = new Node( this, 125, 50 );
        outputs[0]      = new Wire( output_nodes[0] );
    }
}
