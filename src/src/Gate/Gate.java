package Gate;

import Main.Colors;
import Main.Constants;
import Node.Node;
import Wire.Wire;
import Wire.SegmentType;
import Wire.WireType;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

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

    public boolean isPointWithin( final Point2D point2D ) {
        return body.contains( point2D.getX() - location.getX(), point2D.getY() - location.getY() );
    }

    public boolean isPointNear( final Point2D point2D ) {
        double x = point2D.getX() - location.getX();
        double y = point2D.getY() - location.getY();
        Rectangle2D bounds = body.getBounds2D();
        final double maxX = bounds.getMaxX() + Constants.MIN_LINE_LENGTH;
        final double minX = bounds.getMinX() - Constants.MIN_LINE_LENGTH;
        final double maxY = bounds.getMaxY() + Constants.MIN_LINE_LENGTH;
        final double minY = bounds.getMinY() - Constants.MIN_LINE_LENGTH;

        if ( x > maxX ) {
            return false;
        }
        if ( x < minX ) {
            return false;
        }
        if ( y > maxY ) {
            return false;
        }
        if ( y < minY ) {
            return false;
        }
        return true;
    }

    public double getNearestEdge( final double coord, final SegmentType segmentType ) {
        assert segmentType == SegmentType.HORIZONTAL || segmentType == SegmentType.VERTICAL;
        Rectangle2D bounds = body.getBounds2D();
        if ( segmentType == SegmentType.HORIZONTAL ) {
            double y = coord - location.getY();
            final double centerY = bounds.getCenterY();

            if ( y > centerY ) {
                return bounds.getMaxY() + Constants.MIN_LINE_LENGTH + location.getX() + 1;
            }
            else {
                return bounds.getMinY() - Constants.MIN_LINE_LENGTH + location.getY() - 1;
            }
        }
        double x = coord - location.getX();
        final double centerX = bounds.getCenterX();
        if ( x > centerX ) {
            return bounds.getMaxX() + Constants.MIN_LINE_LENGTH + location.getX() + 1;
        }
        else {
            return bounds.getMinX() - Constants.MIN_LINE_LENGTH + location.getX() - 1;
        }
    }

    public Node[] getNodes() { return nodes; }

    public Wire attachWire( final Node wireNode, final Wire wire ) {
        Wire disconnectedWire = null;
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            Node node = nodes[index];
            if ( node == null ) {
                break;
            }
            if ( node == wireNode ) {
                disconnectedWire = wires[index];
                wires[index] = wire;
                break;
            }
        }
        return disconnectedWire;
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

    public void setWireTypes( final WireType wireType ) {
        for( Wire wire : wires ) {
            if ( wire == null ) {
                return;
            }
            wire.setWireType( wireType );
        }
    }
}
