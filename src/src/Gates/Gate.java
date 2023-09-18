package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Gate {
    final Path2D body    = new Path2D.Float();
    final Point2D[] inputs  = new Point2D.Float[Constants.MAX_NUM_IO];
    final Point2D[] outputs = new Point2D.Float[Constants.MAX_NUM_IO];


    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( 4 ) );
        graphics2D.draw(body);
    }
}
