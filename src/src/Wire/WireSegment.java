package Wire;

import java.awt.*;
import java.awt.geom.Point2D;

public class WireSegment {
    private final Point2D start = new Point2D.Double();
    private final Point2D end   = new Point2D.Double();

    public WireSegment( final Point2D start, final Point2D end ) {
        this.start.setLocation( start );
        this.end.setLocation( end );
    }

    public WireSegment( final Point2D start, final double endX, final double endY ) {
        this.start.setLocation( start );
        this.end.setLocation( endX, endY );
    }

    public WireSegment( final double startX, final double startY, final Point2D end ) {
        start.setLocation( startX, startY );
        end.setLocation( end );
    }

    public WireSegment( final double startX, final double startY, final double endX, final double endY ) {
        start.setLocation( startX, startY );
        end.setLocation( endX, endY );
    }

    protected void repaint( final Graphics2D graphics2D ) {
        graphics2D.drawLine( (int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY() );
    }
}
