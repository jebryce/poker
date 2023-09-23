package Gate;

import Main.Colors;
import Main.Constants;
import Node.Node;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Gate {
    protected final Path2D  body         = new Path2D.Float();
    protected final Point2D location     = new Point2D.Float( 0, 0 );
    protected final Node[]  nodes        = new Node[Constants.MAX_NUM_IO];
    protected final Wire[]  wires        = new Wire[Constants.MAX_NUM_IO];

    public Gate( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public void setLocation( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public Point2D getCenterOffset() {
        return new Point2D.Double( body.getBounds().getCenterX() , body.getBounds().getCenterY() );
    }

    public Point2D getLocation() {
        return location;
    }

    public Node[] getNodes() { return nodes; }

    public void attachWire( final Node wireNode, final Wire wire ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            Node node = nodes[index];
            if ( node == null ) {
                break;
            }
            if ( node == wireNode ) {
                wires[index] = wire;
            }
        }
    }

    public Wire getWireAtNode( final Node wireNode ) {
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( nodes[index] == null ) {
                break;
            }
            if ( nodes[index] == wireNode ) {
                return wires[index];
            }
        }
        return null;
    }

    public Point2D getCenter() {
        Point2D centerOffset = getCenterOffset();
        return new Point2D.Double(
                location.getX() + centerOffset.getX(), location.getY() + centerOffset.getY()
        );
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.draw(body);
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    public void flipState() {};

    public void update() {};

    public Wire[] getWires() {
        return wires;
    }
}
