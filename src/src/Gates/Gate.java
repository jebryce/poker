package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Gate {
    protected final Path2D  body         = new Path2D.Float();
    protected final Point2D location     = new Point2D.Float( 0, 0 );
    protected final Node[]  input_nodes  = new Node[Constants.MAX_NUM_IO];
    protected final Wire[]  inputs       = new Wire[Constants.MAX_NUM_IO];
    protected final Node[]  output_nodes = new Node[Constants.MAX_NUM_IO];
    protected final Wire[]  outputs      = new Wire[Constants.MAX_NUM_IO];

    public Gate( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public void setLocation( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public Point2D getCenterOffset() {
        return new Point2D.Double( body.getBounds().getCenterX() , body.getBounds().getCenterY() );
    }

    public Point2D getCenter() {
        Point2D centerOffset = getCenterOffset();
        return new Point2D.Double(
                location.getX() + centerOffset.getX(), location.getY() + centerOffset.getY()
        );
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( 6 ) );
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.draw(body);

        graphics2D.translate( -location.getX(), -location.getY() );
    }

    public void denoteOpenNodes( final Graphics2D graphics2D ) {

    }
}
