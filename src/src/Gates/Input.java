package Gates;


import java.awt.geom.Ellipse2D;

public class Input extends IO_Gate {
    public Input( final int x, final int y ) {
        super( x, y);
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        output_nodes[0] = new Node( this, 50, 25 );
    }

    @Override
    public void flipState() {
        if ( outputs[0] == null ) {
            return;
        }
        outputs[0].flipState();
    }
}
