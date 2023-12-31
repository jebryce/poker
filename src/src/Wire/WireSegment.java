package Wire;

import java.awt.*;
import java.awt.geom.Point2D;
import Container.ListItem;

import Main.Constants;

public class WireSegment extends ListItem {
    private final WireSegmentNode start;
    private final WireSegmentNode end;
    private       SegmentType     segmentType = SegmentType.NEITHER;
    private final Wire            containingWire;
    private       boolean         attachedToGate;

    protected WireSegment( final Wire wire, final WireSegmentNode start, final WireSegmentNode end, final boolean attachedToGate ) {
        this.containingWire = wire;
        this.start          = start;
        this.end            = end;
        this.attachedToGate = attachedToGate;
        setSegmentType();
    }

    protected boolean isAttachedToGate() {
        return attachedToGate;
    }

    protected void detachFromGate() {
        attachedToGate = false;
    }

    protected void detachFromSegmentNodes() {
        end.removeSegment(this);
        start.removeSegment(this);
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

    protected WireSegmentNode getNonConnectedPoint( final WireSegment wireSegment ) {
        if ( this.start == wireSegment.start ) {
            return this.end;
        }
        if ( this.start == wireSegment.end   ) {
            return this.end;
        }
        if ( this.end   == wireSegment.start ) {
            return this.start;
        }
        if ( this.end   == wireSegment.end   ) {
            return this.start;
        }
        return null;
    }

    public Wire getContainingWire() {
        return containingWire;
    }

    protected WireSegmentNode getStartClone() {
        return (WireSegmentNode) start.clone();
    }

    protected WireSegmentNode getEndClone() {
        return (WireSegmentNode) end.clone();
    }

    protected WireSegmentNode getStartPoint() {
        return start;
    }

    protected WireSegmentNode getEndPoint() {
        return end;
    }

    protected void moveStartX( final double delta ) {
        start.setLocation( start.getX() + delta, start.getY() );
    }

    protected WireSegmentNode getOtherNode( final WireSegmentNode wireSegmentNode ) {
        if ( start == wireSegmentNode ) {
            return end;
        }
        else if ( end == wireSegmentNode ) {
            return start;
        }
        assert false : "wireSegmentNode not part of this wire segment.";
        return null;
    }

    protected void repaint( final Graphics2D graphics2D ) {
        graphics2D.drawLine( (int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY() );

//        graphics2D.drawString( String.valueOf( start.getNumSegments() ), (int) start.getX(), (int) start.getY() );
//        graphics2D.drawString( String.valueOf( end.getNumSegments() ), (int) end.getX(), (int) end.getY() );
    }
}
