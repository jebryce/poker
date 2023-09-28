package Wire;

import java.awt.*;
import java.awt.geom.Point2D;

import Main.Constants;

public class WireSegment {
    private final Point2D     start       = new Point2D.Double();
    private final Point2D     end         = new Point2D.Double();
    private       SegmentType segmentType = SegmentType.NEITHER;

    public WireSegment( final Point2D start, final Point2D end ) {
        this.start.setLocation( start );
        this.end.setLocation( end );
        setSegmentType();
    }

    public WireSegment( final Point2D start, final double endX, final double endY ) {
        this.start.setLocation( start );
        this.end.setLocation( endX, endY );
        setSegmentType();
    }

    public WireSegment( final double startX, final double startY, final Point2D end ) {
        start.setLocation( startX, startY );
        end.setLocation( end );
        setSegmentType();
    }

    public WireSegment( final double startX, final double startY, final double endX, final double endY ) {
        start.setLocation( startX, startY );
        end.setLocation( endX, endY );
        setSegmentType();
    }

    private void setSegmentType() {
        if ( start.getY() == end.getY() ) {
            segmentType = SegmentType.HORIZONTAL;
        }
        else if ( start.getX() == end.getX() ) {
            segmentType = SegmentType.VERTICAL;
        }
        else {
            segmentType = SegmentType.NEITHER;
        }
    }

    protected double getLength() {
        return start.distance( end );
    }

    protected boolean isPointNear( final Point2D point ) {
        switch ( segmentType ) {
            case NEITHER -> { return false; }
            case VERTICAL -> {
                if ( point.getX() > Math.max( start.getX(), end.getX() ) + Constants.LINE_GRAB_RADIUS ) {
                    return false;
                }
                if ( point.getX() < Math.min( start.getX(), end.getX() ) + Constants.LINE_GRAB_RADIUS ) {
                    return false;
                }
                if ( point.getY() > Math.max( start.getY(), end.getY() ) ) {
                    return false;
                }
                if ( point.getY() < Math.min( start.getY(), end.getY() ) ) {
                    return false;
                }
            }
            case HORIZONTAL -> {
                if ( point.getX() > Math.max( start.getX(), end.getX() ) ) {
                    return false;
                }
                if ( point.getX() < Math.min( start.getX(), end.getX() ) ) {
                    return false;
                }
                if ( point.getY() > Math.max( start.getY(), end.getY() ) + Constants.LINE_GRAB_RADIUS ) {
                    return false;
                }
                if ( point.getY() < Math.min( start.getY(), end.getY() ) + Constants.LINE_GRAB_RADIUS ) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void repaint( final Graphics2D graphics2D ) {
        graphics2D.drawLine( (int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY() );
    }
}
