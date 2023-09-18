package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Gate {
    protected final Path2D    body       = new Path2D.Float();
    protected final Point2D[] input_pts  = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final boolean[] inputs     = new boolean[Constants.MAX_NUM_IO];
    protected final Point2D[] output_pts = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final boolean[] outputs    = new boolean[Constants.MAX_NUM_IO];


    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( 6 ) );
        graphics2D.draw(body);
    }
}
