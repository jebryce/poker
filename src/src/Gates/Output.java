package Gates;


import Main.Colors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Output extends Gate {
    public Output() {
        body.append( new Ellipse2D.Float( 0, 0, 50, 50), false );

        input_pts[0] = new Point2D.Float( 0, 25 );
    }

    @Override
    public void repaint( Graphics2D graphics2D ) {
        if ( inputs[0] ) {
            graphics2D.setColor( Colors.GREEN );
        } else {
            graphics2D.setColor( Colors.RED );
        }
        graphics2D.fill( body );
        super.repaint(graphics2D);
    }
}
