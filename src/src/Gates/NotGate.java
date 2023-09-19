package Gates;


import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class NotGate extends IO_Gate {
    public NotGate( final int x, final int y ) {
        super( x, y);
        body.append( new Line2D.Float( 0, 0, 50, 30 ), false );
        body.append( new Line2D.Float( 50, 30, 0, 60 ), false );
        body.append( new Line2D.Float( 0, 60, 0, 0 ), false );

        output_pts[0] = new Point2D.Float( 50, 30 );
        outputs[0]    = new Wire();
    }
}
