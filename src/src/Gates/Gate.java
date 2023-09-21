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

    public void attachWire( final Node wireNode, final Wire wire ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( input_nodes[index] == null && output_nodes[index] == null ) {
                break;
            }
            if ( input_nodes[index] == wireNode ) {
                inputs[index] = wire;
            }
            if ( output_nodes[index] == wireNode ) {
                outputs[index] = wire;
            }
        }
    }

    public void  detachWire( final Wire wire ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( inputs[index] == null && outputs[index] == null ) {
                break;
            }
            if ( inputs[index] == wire ) {
                inputs[index] = null;
            }
            if ( outputs[index] == wire ) {
                outputs[index] = null;
            }
        }
    }

    public Point2D getCenter() {
        Point2D centerOffset = getCenterOffset();
        return new Point2D.Double(
                location.getX() + centerOffset.getX(), location.getY() + centerOffset.getY()
        );
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( Constants.LINE_THICKNESS ) );
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.draw(body);
        repaintIO( graphics2D );
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    private void repaintIO( final Graphics2D graphics2D ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            Node input  = input_nodes[index];
            Node output = output_nodes[index];
            if ( input == null && output == null ) {
                break;
            }
            if ( input != null && inputs[index] == null ) {
                Point2D inputLocation = input.getLocation();
                graphics2D.drawLine( (int) ( inputLocation.getX()-30), (int) inputLocation.getY(),
                        (int) inputLocation.getX(), (int) inputLocation.getY() );
            }
            if ( output != null && outputs[index] == null ) {
                Point2D outputLocation = output.getLocation();
                graphics2D.drawLine( (int) ( outputLocation.getX()+30), (int) outputLocation.getY(),
                        (int) outputLocation.getX(), (int) outputLocation.getY() );
            }
        }
    }

    public void flipState() {};
}
