package Gates;


import Main.Colors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends IO_Gate {
    public Output( final int x, final int y ) {
        super( x, y);
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        input_pts[0] = new Point2D.Float( 0, 25 );
    }
}
