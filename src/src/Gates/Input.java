package Gates;


import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Input extends IO_Gate {
    public Input( final int x, final int y ) {
        super( x, y);
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        output_pts[0] = new Point2D.Float( 50, 25 );
        outputs[0]    = new Wire();
    }

    public void flipState() {
        outputs[0].flipState();
    }
}
