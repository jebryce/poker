package Wire;

import java.awt.*;
import java.awt.geom.Point2D;

import Main.Constants;

public class WireSegment {
    private final Point2D     start;
    private final Point2D     end;
    private       SegmentType segmentType = SegmentType.NEITHER;
    private final Wire        containingWire;
    private       boolean     attachedToGate;
    private       WireSegment next        = null;
    boolean test = false;

    protected WireSegment( final Wire wire, final Point2D start, final Point2D end, final boolean attachedToGate ) {
        this.containingWire = wire;
        this.start          = start;
        this.end            = end;
        this.attachedToGate = attachedToGate;
        setSegmentType();
    }

    protected void setNext( final WireSegment next ) {
        this.next = next;
    }

    protected WireSegment getNext() {
        return next;
    }

    protected boolean isAttachedToGate() {
        return attachedToGate;
    }

    protected void detachFromGate() {
        attachedToGate = false;
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

    protected SegmentType getSegmentType() {
        return segmentType;
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

    protected Point2D getStartClone() {
        return (Point2D) start.clone();
    }

    protected Point2D getEndClone() {
        return (Point2D) end.clone();
    }

    protected Point2D getStartPoint() {
        return start;
    }

    protected Point2D getEndPoint() {
        return end;
    }

    protected void moveStartX( final double delta ) {
        start.setLocation( start.getX() + delta, start.getY() );
    }

    protected void repaint( final Graphics2D graphics2D ) {
        graphics2D.drawLine( (int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY() );
        if ( test ) {
//            graphics2D.drawOval( (int) start.getX() - 10, (int) start.getY() - 10, 20, 20 );
            graphics2D.drawOval( (int) end.getX() - 10, (int) end.getY() - 10, 20, 20 );
        }
    }
}
