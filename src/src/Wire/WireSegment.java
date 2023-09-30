package Wire;

import java.awt.*;
import java.awt.geom.Point2D;

import Main.Constants;

public class WireSegment {
    private final Point2D     start;
    private final Point2D     end;
    private       SegmentType segmentType = SegmentType.NEITHER;
    private final Wire        containingWire;

    protected WireSegment( final Wire wire, final Point2D start, final Point2D end ) {
        this.containingWire = wire;
        this.start          = start;
        this.end            = end;
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
                if ( point.getX() < Math.min( start.getX(), end.getX() ) - Constants.LINE_GRAB_RADIUS ) {
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
                if ( point.getY() < Math.min( start.getY(), end.getY() ) - Constants.LINE_GRAB_RADIUS ) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void moveSegment( final Point2D location ) {
        switch ( segmentType ) {
            case NEITHER -> {}
            case VERTICAL -> {
                start.setLocation( location.getX(), start.getY() );
                end.setLocation(   location.getX(), end.getY() );
            }
            case HORIZONTAL -> {
                start.setLocation( start.getX(), location.getY() );
                end.setLocation(   end.getX(),   location.getY() );
            }
        }
    }

    public Wire getContainingWire() {
        return containingWire;
    }

    protected Point2D getStartPoint() {
        return (Point2D) start.clone();
    }

    protected Point2D getEndPoint() {
        return (Point2D) start.clone();
    }

    protected void repaint( final Graphics2D graphics2D ) {
        graphics2D.drawLine( (int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY() );
    }
}
