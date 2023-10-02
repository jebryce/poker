package Wire;

import Gate.Gate;

import Container.LinkedList;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wires extends LinkedList<Wire> {

    public void addWiresFromGate( final Gate newGate) {
        for ( Wire wire : newGate.getWires() ) {
            if ( wire == null ) {
                break;
            }
            add( wire );
        }
    }

    public WireSegment findContainingWireSegment( final Point2D point ) {
        WireSegment shortestSegment = null;
        for ( Wire wire : this ) {
            if ( wire == null ) {
                break;
            }
            if ( !wire.isPointWithinBounds( point ) ) {
                continue;
            }
            WireSegment segmentNear = wire.isPointNear( point );
            if ( segmentNear != null ) {
                if ( shortestSegment == null ) {
                    shortestSegment = segmentNear;
                }
                else if ( segmentNear.getLength() < shortestSegment.getLength() ) {
                    shortestSegment = segmentNear;
                }
            }
        }
        return shortestSegment;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Wire wire : this ) {
            wire.repaint( graphics2D );
        }
    }
}
