package Wire;

import Gate.Gate;
import Main.Colors;
import Main.Constants;
import Node.Node;

import javax.swing.event.MenuDragMouseListener;
import java.awt.*;
import java.awt.geom.Point2D;

public class Wire {
    private       boolean       state         = false;
    private final Node[]        attachedNodes = new Node[Constants.MAX_NUM_WIRE_NODES];
    private       int           numNodes      = 0;
    private final Gate[]        attachedGates = new Gate[Constants.MAX_NUM_WIRE_NODES];
    private       WireSegment   headWireSegment;
    private       int           numSegments   = 0;
    private       WireType      wireType      = WireType.UNCONNECTED;
    private       Point2D       minBound;
    private       Point2D       maxBound;

    public Wire() {
    }

    public Wire( final Node node ) {
        attachToNode( node );
    }

    public WireType getWireType() {
        return wireType;
    }

    public void setWireType( final WireType wireType ) {
        this.wireType = wireType;
    }

    public void attachToPlayer( final Point2D playerLocation ) {
        attachedNodes[numNodes] = new Node( playerLocation );
    }

    public void detachFromPlayer() {
        attachedNodes[numNodes] = null;
        if ( numNodes < 2 ) {
            wireType = WireType.UNCONNECTED;
        }
        else {
            wireType = WireType.CONNECTED;
        }
    }

    public Wire attachToNode( final Node node ) {
        if ( node == null ) {
            return null;
        }
        Gate attachedGate = node.getAttachedGate();
        Wire disconnectedWire = attachedGate.attachWire( node, this );
        this.attachedNodes[numNodes++] = node;
        for( int index = 0; index < Constants.MAX_NUM_WIRE_NODES; index++ ) {
            if( attachedGates[index] == null ) {
                attachedGates[index] = attachedGate;
            }
            if( attachedGates[index] == attachedGate ) {
                break;
            }
        }
        if ( numNodes > 1 ) {
            wireType = WireType.CONNECTED;
            generateSegments();
        }
        return disconnectedWire;
    }

    private void generateSegments() {
        if ( numNodes < 2 ) {
            return;
        }
        Point2D node0   = attachedNodes[numNodes-2].getTrueLocation();
        Point2D node1   = attachedNodes[numNodes-1].getTrueLocation();
        double middleX  = ( node0.getX() + node1.getX() ) / 2;
        Point2D middle0 = new Point2D.Double( middleX, node0.getY() );
        Point2D middle1 = new Point2D.Double( middleX, node1.getY() );
        addSegment( node0, middle0 );
        addSegment( middle1, node1 );
        addSegment( middle0, middle1 );
    }

    public boolean hasAttachedNode( final Node node ) {
        if ( node == null ) {
            return false;
        }
        for ( Node attachedNode : attachedNodes ) {
            if ( attachedNode == null ) {
                return false;
            }
            if ( attachedNode == node ) {
                return true;
            }
        }

        return false;
    }

    public boolean getState() {
        return state;
    }

    public void setState( final boolean newState ) {
        if ( state != newState ) {
            state = newState;
        }
    }

    public void flipState() {
        state = !state;
    }

    protected boolean isPointWithinBounds( final Point2D point ) {
        if ( wireType != WireType.CONNECTED ) {
            return false;
        }
        if ( point.getX() < minBound.getX() - Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getX() > maxBound.getX() + Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getY() < minBound.getY() - Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getY() > maxBound.getY() + Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        return true;
    }

    protected WireSegment isPointNear( final Point2D point ) {
        WireSegment nearestSegment = null;
        for ( WireSegment wireSegment = headWireSegment; wireSegment != null; wireSegment = wireSegment.getNext() ) {
            if ( wireSegment.isPointNear( point ) ) {
                if ( nearestSegment == null ) {
                    nearestSegment = wireSegment;
                }
                else if ( wireSegment.getLength() < nearestSegment.getLength() ) {
                    nearestSegment = wireSegment;
                }
            }
        }
        return nearestSegment;
    }

    public WireSegment moveSegment( final WireSegment wireSegment, final Point2D location ) {
        assert wireSegment != null;
        assert location    != null;
        Point2D gatePoint      = null;
        Point2D wirePoint      = null;
        Gate    overlappedGate = null;
        WireSegment returnSegment = wireSegment;

        if ( wireSegment.isAttachedToGate() ) {
            gatePoint = wireSegment.getStartClone();
            wirePoint = wireSegment.getStartPoint();
        }

        wireSegment.moveSegment( location );
        updateBounds( wireSegment );

        if ( gatePoint != null && gatePoint.getY() != location.getY()  ) {
            // if the wireSegment we are trying to move is attached to a gate, split the segment in two and leave a little bit attached
            double offset = Constants.MIN_LINE_LENGTH;
            if ( wirePoint.getX() > wireSegment.getEndClone().getX() ) {
                offset *= -1;
            }
            wireSegment.moveStartX( offset );
            wireSegment.detachFromGate();
            Point2D midPoint = new Point2D.Double( wirePoint.getX(), gatePoint.getY() );
            addSegment( gatePoint, midPoint );
            addSegment( midPoint, wirePoint );
            return returnSegment;
        }

        for ( Gate gate : attachedGates ) { // if we try to move a wire segment, don't allow it to overlap an attached gate
            if ( gate == null ) {
                break;
            }
            Point2D start = wireSegment.getStartPoint();
            Point2D end   = wireSegment.getEndPoint();
            if ( gate.isPointNear( start ) ) {
                gatePoint = start;
                wirePoint = end;
            }
            else if ( gate.isPointNear( end ) ) {
                gatePoint = end;
                wirePoint = start;
            }
            else {
                continue;
            }
            overlappedGate = gate;
            break;
        }

        if ( overlappedGate != null && wireSegment.getSegmentType() == SegmentType.VERTICAL ) {
            assert gatePoint != null;
            double edge = overlappedGate.getNearestEdge( gatePoint.getX(), SegmentType.VERTICAL );
            double y = overlappedGate.getNearestEdge( location.getY(), SegmentType.HORIZONTAL );

            if ( edge != location.getX() && y != gatePoint.getY() && y != wirePoint.getY() ) {
                Point2D cornerPoint = new Point2D.Double( edge, y );
                Point2D edgePoint   = new Point2D.Double( location.getX(), y );

                gatePoint.setLocation( edge, gatePoint.getY() );
                addSegment( gatePoint, cornerPoint );
                addSegment( cornerPoint, edgePoint );
                removeSegment( wireSegment );
                returnSegment = addSegment( edgePoint, wirePoint );
            }
        }
        return returnSegment;
    }

    public void remove0LengthSegments() {
        for ( WireSegment wireSegment = headWireSegment; wireSegment != null; wireSegment = wireSegment.getNext() ) {
            if ( wireSegment.getLength() == 0.0 ) {
                removeSegment( wireSegment );
                reconnectSegmentsFrom0LengthSegment( wireSegment );
            }
        }
    }

    private void reconnectSegmentsFrom0LengthSegment( final WireSegment wireSegment0Length ) {
        final Point2D start = wireSegment0Length.getStartPoint();
        final Point2D end   = wireSegment0Length.getEndPoint();
        Point2D newStart = null;
        Point2D newEnd   = null;
        for ( WireSegment wireSegment = headWireSegment; wireSegment != null; wireSegment = wireSegment.getNext() ) {
            assert wireSegment != wireSegment0Length;
            if ( wireSegment.getStartPoint() == start ) {
                newStart = wireSegment.getEndPoint();
                removeSegment( wireSegment );
            }
            if ( wireSegment.getEndPoint()   == start ) {
                newStart = wireSegment.getStartPoint();
                removeSegment( wireSegment );
            }
            if ( wireSegment.getStartPoint() == end ) {
                newEnd = wireSegment.getEndPoint();
                removeSegment( wireSegment );
            }
            if ( wireSegment.getEndPoint()   == end ) {
                newEnd = wireSegment.getStartPoint();
                removeSegment( wireSegment );
            }
        }
        assert newStart != null;
        assert newEnd   != null;
        addSegment( newStart, newEnd );
    }

    private void removeSegment( final WireSegment wireSegment ) {
        WireSegment currentSegment = headWireSegment;
        WireSegment previousSegment = null;
        while ( currentSegment != null ) {
            if ( currentSegment == wireSegment ) {
                if ( previousSegment == null ) {
                    headWireSegment = currentSegment.getNext();
                }
                else {
                    previousSegment.setNext( currentSegment.getNext() );
                }
                return;
            }
            previousSegment = currentSegment;
            currentSegment = currentSegment.getNext();
        }
//        assert false : "Wire segment " + wireSegment + " not in wire segment linked list.";
    }

    private WireSegment addSegment( final Point2D start, final Point2D end ) {
        assert start != null;
        assert end != null;
        assert start.distance( end ) != 0.0;
        boolean isAttachedToGate = false;

        Point2D node0 = start;
        Point2D node1 = end;

        for ( Node node : attachedNodes ) {
            if ( node == null ) {
                break;
            }
            Point2D nodeLocation = node.getTrueLocation();
            if ( nodeLocation.distance(start) == 0 ) {
                isAttachedToGate = true;
                break;
            }
            if ( nodeLocation.distance( end ) == 0 ) {
                node0 = end;
                node1 = start;
                isAttachedToGate = true;
                break;
            }
        }

        WireSegment newSegment = new WireSegment(this, node0, node1, isAttachedToGate);
        newSegment.setNext(headWireSegment);
        headWireSegment = newSegment;

        if ( maxBound == null ) {
            maxBound = new Point2D.Double();
            maxBound.setLocation( node0 );
        }
        if ( minBound == null ) {
            minBound = new Point2D.Double();
            minBound.setLocation( node0 );
        }

        updateBounds( headWireSegment );
        return headWireSegment;
    }

    // @TODO: BUG  updateBounds can never shrink the bounds
    private void updateBounds( final WireSegment wireSegment ) {
        Point2D start = wireSegment.getStartClone();
        Point2D end   = wireSegment.getEndClone();
        double minX = Math.min( minBound.getX(), Math.min( start.getX(), end.getX() ) );
        double minY = Math.min( minBound.getY(), Math.min( start.getY(), end.getY() ) );
        minBound.setLocation( minX, minY );

        double maxX = Math.max( maxBound.getX(), Math.max( start.getX(), end.getX() ) );
        double maxY = Math.max( maxBound.getY(), Math.max( start.getY(), end.getY() ) );
        maxBound.setLocation( maxX, maxY );
    }

    public void repaint( final Graphics2D graphics2D ) {
        switch ( wireType ) {
            case UNCONNECTED  -> repaintUNCONNECTED( graphics2D );
            case HELD_GATE    -> repaintHELD_GATE( graphics2D );
            case HELD_IN_HAND -> repaintHELD_IN_HAND( graphics2D );
            case CONNECTED    -> repaintCONNECTED( graphics2D );
        }
    }

    private void repaintUNCONNECTED( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        Node node = attachedNodes[0];
        assert node != null;
        int offset;
        switch ( node.getNodeType() ) {
            case INPUT  -> offset = -40;
            case OUTPUT -> offset = 40;
            default     -> offset = 0;
        }

        Point2D location = node.getTrueLocation();
        graphics2D.drawLine(
                (int) location.getX() + offset, (int) location.getY(), (int) location.getX(), (int) location.getY()
        );
    }

    private void repaintHELD_GATE( final Graphics2D graphics2D ) {
        Point2D location = attachedGates[0].getLocation();
        graphics2D.translate( location.getX(), location.getY() );
        repaintUNCONNECTED( graphics2D );
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    private void repaintHELD_IN_HAND( final Graphics2D graphics2D ) {
        repaintCONNECTED( graphics2D );
        graphics2D.setColor( Colors.BLACK );
        Point2D player = attachedNodes[numNodes].getLocation();
        Point2D node0 = attachedNodes[numNodes-1].getTrueLocation();
        int middleX = (int) ( node0.getX() + player.getX() ) / 2;
        graphics2D.drawLine((int) node0.getX(), (int) node0.getY(), middleX, (int) node0.getY());
        graphics2D.drawLine((int) player.getX(), (int) player.getY(), middleX, (int) player.getY());
        graphics2D.drawLine(middleX, (int) node0.getY(), middleX, (int) player.getY());

    }

    private void repaintCONNECTED( final Graphics2D graphics2D ) {
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        for ( WireSegment wireSegment = headWireSegment; wireSegment != null; wireSegment = wireSegment.getNext() ) {
            wireSegment.repaint( graphics2D );
        }
    }
}

/*

for ( Node node : attachedNodes ) {
                assert node != null : "Wire segment boolean [isAttachedToGate] shouldn't be true";

                Point2D nodeLocation = node.getTrueLocation();
                if ( nodeLocation.distance( wireSegment.getStartClone() ) == 0 ) {
                    double endX = nodeLocation.getX() + Constants.MIN_LINE_LENGTH;
                    if ( endX == wireSegment.getEndClone().getX() ) {
                        return;
                    }
                    Point2D segmentLocation = wireSegment.getStartPoint();
                    wireSegment.setStartX( endX );
                    Point2D middle = new Point2D.Double( endX, nodeLocation.getY() );
                    addSegment( nodeLocation, middle );
                    addSegment( middle, segmentLocation );
                    break;
                }

                if ( nodeLocation.distance( wireSegment.getEndClone() ) == 0 ) {
                    double startX = nodeLocation.getX() - Constants.MIN_LINE_LENGTH;
                    if ( startX == wireSegment.getStartClone().getX() ) {
                        return;
                    }
                    Point2D segmentLocation = wireSegment.getEndPoint();
                    wireSegment.setEndX( startX );
                    Point2D middle = new Point2D.Double( startX, nodeLocation.getY() );
                    addSegment( segmentLocation, middle );
                    addSegment( middle, nodeLocation );
                    break;
                }
            }
            wireSegment.detachFromGate();
 */