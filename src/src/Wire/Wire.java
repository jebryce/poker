package Wire;

import Gate.Gate;
import Main.Colors;
import Main.Constants;
import Node.Node;

import java.awt.*;
import java.awt.geom.Point2D;

import Container.LinkedList;


public class Wire extends LinkedList<WireSegment> {
    private       boolean       state         = false;
    private final Node[]        attachedNodes = new Node[Constants.MAX_NUM_WIRE_NODES];
    private       int           numNodes      = 0;
    private final Gate[]        attachedGates = new Gate[Constants.MAX_NUM_WIRE_NODES];
    private       WireType      wireType      = WireType.UNCONNECTED;
    private       Point2D       minBound;
    private       Point2D       maxBound;

    public Wire() {
    }

    public Wire( final Node node ) {
        attachToNode( node );
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
        else if ( numNodes == 2 ) {
            WireSegmentNode node0   = new WireSegmentNode( attachedNodes[0].getTrueLocation() );
            WireSegmentNode node1   = new WireSegmentNode( attachedNodes[1].getTrueLocation() );
            double middleX  = ( node0.getX() + node1.getX() ) / 2;
            WireSegmentNode middle0 = new WireSegmentNode( middleX, node0.getY() );
            WireSegmentNode middle1 = new WireSegmentNode( middleX, node1.getY() );
            addSegment( node0, middle0 );
            addSegment( middle1, node1 );
            addSegment( middle0, middle1 );
            return;
        }
        WireSegment segmentToBranchFrom = null;
        for ( WireSegment wireSegment : this ) {
            if ( wireSegment.isAttachedToGate() ) {
                continue;
            }
            segmentToBranchFrom = wireSegment;
        }
        assert segmentToBranchFrom != null;
        WireSegmentNode start = segmentToBranchFrom.getStartPoint();
        WireSegmentNode end   = segmentToBranchFrom.getEndPoint();
        WireSegmentNode min, max;
        if ( start.getY() < end.getY() ) {
            min = start;
            max = end;
        }
        else {
            min = end;
            max = start;
        }
        WireSegmentNode newNode = new WireSegmentNode( attachedNodes[numNodes-1].getTrueLocation() );
        WireSegmentNode middle  = new WireSegmentNode( start.getX(), newNode.getY() );
        if ( newNode.getY() < min.getY() ) {
            addSegment( middle, min );
        }
        else if ( newNode.getY() > max.getY() ) {
            addSegment( middle, max );
        }
        else {
            remove( segmentToBranchFrom );
            addSegment( min, middle );
            addSegment( middle, max);
        }
        addSegment( newNode, middle );
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
        for ( WireSegment wireSegment : this ) {
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
        assert location != null;
        WireSegmentNode gatePoint = null;
        WireSegmentNode wirePoint = null;
        Gate overlappedGate = null;
        WireSegment returnSegment = wireSegment;

        if ( wireSegment.isAttachedToGate() ) {
            gatePoint = wireSegment.getStartClone();
            wirePoint = wireSegment.getStartPoint();
        }

        // if the wireSegment we are trying has a node shared with (at least) 2 other wire segments
        if ( wireSegment.getEndPoint().getNumSegments() > 2 ) {
            returnSegment = detachSharedSegmentFromPoint( returnSegment, returnSegment.getEndPoint(), location );
        }
        if ( wireSegment.getStartPoint().getNumSegments() > 2 ) {
            returnSegment = detachSharedSegmentFromPoint( returnSegment, returnSegment.getStartPoint(), location );
        }

        returnSegment.moveSegment(location);
        snapSegment(returnSegment);
        updateBounds(returnSegment);

        // if the wireSegment we are trying to move is attached to a gate, split the segment in two and leave a little bit attached
        if ( gatePoint != null && gatePoint.getY() != location.getY() ) {
            double offset = Constants.MIN_LINE_LENGTH;
            if ( wirePoint.getX() > returnSegment.getEndClone().getX() ) {
                offset *= -1;
            }
            returnSegment.moveStartX(offset);
            returnSegment.detachFromGate();
            WireSegmentNode midPoint = new WireSegmentNode(wirePoint.getX(), gatePoint.getY());
            addSegment(gatePoint, midPoint);
            addSegment(midPoint, wirePoint);
            return returnSegment;
        }

        // if we try to move a wire segment, don't allow it to overlap an attached gate
        for ( Gate gate : attachedGates ) {
            if ( gate == null ) {
                break;
            }
            WireSegmentNode start = wireSegment.getStartPoint();
            WireSegmentNode end = wireSegment.getEndPoint();
            if ( gate.isPointNear(start) ) {
                gatePoint = start;
                wirePoint = end;
            } else if ( gate.isPointNear(end) ) {
                gatePoint = end;
                wirePoint = start;
            } else {
                continue;
            }
            overlappedGate = gate;
            break;
        }

        if ( overlappedGate != null && wireSegment.getSegmentType() == SegmentType.VERTICAL ) {
            assert gatePoint != null;
            double edge = overlappedGate.getNearestEdge(gatePoint.getX(), SegmentType.VERTICAL);
            double y = overlappedGate.getNearestEdge(location.getY(), SegmentType.HORIZONTAL);

            if ( edge != location.getX() && y != gatePoint.getY() && y != wirePoint.getY() ) {
                WireSegmentNode cornerPoint = new WireSegmentNode(edge, y);
                WireSegmentNode edgePoint = new WireSegmentNode(location.getX(), y);

                gatePoint.setLocation(edge, gatePoint.getY());
                addSegment(gatePoint, cornerPoint);
                addSegment(cornerPoint, edgePoint);
                remove(wireSegment);
                returnSegment = addSegment(edgePoint, wirePoint);
            }
        }
        return returnSegment;
    }

    private WireSegment detachSharedSegmentFromPoint( final WireSegment wireSegment, final WireSegmentNode segmentNode, final Point2D offset ) {
        WireSegment returnSegment = wireSegment;
        for ( WireSegment attachedSegment : segmentNode ) {
            if ( attachedSegment.getSegmentType() != wireSegment.getSegmentType() ) {
                continue;
            }
            if ( attachedSegment == wireSegment ) {
                continue;
            }
            if ( wireSegment.getSegmentType() == SegmentType.VERTICAL ) {
                WireSegmentNode newNode = new WireSegmentNode( offset.getX(), segmentNode.getY() );
                WireSegmentNode otherNode = wireSegment.getOtherNode( segmentNode );
                otherNode.setLocation( offset.getX(), otherNode.getY() );
                returnSegment = addSegment( otherNode, newNode );
                addSegment( segmentNode, newNode );
                remove( wireSegment );
                break;
            }
        }
        return returnSegment;
    }

    private void snapSegment( final WireSegment heldSegment ) {
        for ( WireSegment wireSegment : this ) {
            if ( heldSegment == wireSegment ) {
                continue;
            }
            Point2D pointToMoveTo = wireSegment.getNonConnectedPoint( heldSegment );
            if ( pointToMoveTo == null ) {
                continue;
            }
            if ( wireSegment.getLength() > Constants.LINE_THICKNESS ) {
                continue;
            }
            heldSegment.moveSegment( pointToMoveTo );
        }
    }

    public void remove0LengthSegments() {
        for ( WireSegment wireSegment : this ) {
            if ( wireSegment.getLength() == 0.0 ) {
                remove( wireSegment );
                reconnectSegmentsFrom0LengthSegment( wireSegment );
            }
        }
    }

    private void reconnectSegmentsFrom0LengthSegment( final WireSegment wireSegment0Length ) {
        WireSegmentNode newStart = null;
        WireSegmentNode newEnd   = null;
        for ( WireSegment wireSegment : this ) {
            assert wireSegment != wireSegment0Length;
            WireSegmentNode nonConnectedPoint = wireSegment.getNonConnectedPoint( wireSegment0Length );
            if ( nonConnectedPoint == null ) {
                continue;
            }
            remove( wireSegment );
            if ( newStart == null ) {
                newStart = nonConnectedPoint;
                continue;
            }
            newEnd = nonConnectedPoint;
            break;
        }
        assert newStart != null;
        assert newEnd   != null;
        addSegment( newStart, newEnd );
    }

    private WireSegment addSegment( final WireSegmentNode start, final WireSegmentNode end ) {
        assert start != null;
        assert end != null;
//        assert start.distance( end ) != 0.0;
        boolean isAttachedToGate = false;

        WireSegmentNode node0 = start;
        WireSegmentNode node1 = end;

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
        WireSegment newSegment = new WireSegment( this, node0, node1, isAttachedToGate );
        add( newSegment );
        start.addSegment( newSegment );
        end.addSegment( newSegment );

        if ( maxBound == null ) {
            maxBound = new Point2D.Double();
            maxBound.setLocation( node0 );
        }
        if ( minBound == null ) {
            minBound = new Point2D.Double();
            minBound.setLocation( node0 );
        }

        updateBounds( newSegment );
        return newSegment;
    }

    @Override
    public void remove( final WireSegment wireSegment ) {
        wireSegment.detachFromSegmentNodes();
        super.remove( wireSegment );
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
        for ( WireSegment wireSegment : this ) {
            wireSegment.repaint( graphics2D );
        }
    }
}