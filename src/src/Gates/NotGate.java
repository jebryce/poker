package Gates;


import java.awt.geom.Line2D;

public class NotGate extends IO_Gate {
    public NotGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 50, 30 ), false );
        body.append( new Line2D.Float( 50, 30, 0, 60 ), false );
        body.append( new Line2D.Float( 0, 60, 0, 0 ), false );

        input_nodes[0]  = new Node( this, 0, 30 );
        inputs[0]       = new Wire( input_nodes[0] );

        output_nodes[0] = new Node( this, 50, 30 );
        outputs[0]      = new Wire( output_nodes[0] );
    }

    @Override
    public void update() {
        outputs[0].setState( !inputs[0].getState() );
    }
}
