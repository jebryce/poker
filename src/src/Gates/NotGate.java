package Gates;


import java.awt.geom.Line2D;

public class NotGate extends IO_Gate {
    public NotGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 50, 30 ), false );
        body.append( new Line2D.Float( 50, 30, 0, 60 ), false );
        body.append( new Line2D.Float( 0, 60, 0, 0 ), false );

        input_nodes[0]  = new Node( this, 0, 30 );

        output_nodes[0] = new Node( this, 50, 30 );
    }
}
