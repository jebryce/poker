package Gate;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Container.ListItem;

public class Gate extends ListItem {
    protected final Path2D  body         = new Path2D.Float();
    protected final Point2D location     = new Point2D.Float( 0, 0 );

    public Gate( final Point2D location ) {
        this.location.setLocation( location );
    }

    public void setLocation( final double x, final double y ) {
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

    public void update() {};
}
