package Wire;

import Gate.Gate;
import Main.Colors;
import Main.Constants;
import Node.Node;
import Node.NodeType;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

public class Wire {
    private       boolean       state         = false;
    private final Node[]        attachedNodes = new Node[Constants.MAX_NUM_WIRE_NODES];
    private       int           numNodes      = 0;
    private final Gate[]        attachedGates = new Gate[Constants.MAX_NUM_WIRE_NODES];
    private final WireSegment[] wireSegments  = new WireSegment[Constants.MAX_NUM_WIRE_SEGMENTS];
    private       int           numSegments   = 0;
    private       WireType      wireType      = WireType.UNCONNECTED;
    private final Point2D[]     bounds        = new Point2D[2];

    public Wire() {}

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
        wireSegments[numSegments++] = new WireSegment( node0, middle0 );
        wireSegments[numSegments++] = new WireSegment( node1, middle1 );
        wireSegments[numSegments++] = new WireSegment( middle0, middle1 );
        bounds[0] = new Point2D.Double( Math.min( node0.getX(), node1.getX() ), Math.min( node0.getY(), node1.getY() ) );
        bounds[1] = new Point2D.Double( Math.max( node0.getX(), node1.getX() ), Math.max( node0.getY(), node1.getY() ) );
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
        if ( point.getX() < bounds[0].getX() ) {
            return false;
        }
        if ( point.getX() > bounds[1].getX() ) {
            return false;
        }
        if ( point.getY() < bounds[0].getY() ) {
            return false;
        }
        if ( point.getY() > bounds[1].getY() ) {
            return false;
        }
        return true;
    }

    protected WireSegment isPointNear( final Point2D point ) {

        WireSegment nearestSegment = null;
        for ( WireSegment wireSegment : wireSegments ) {
            if ( wireSegment == null ) {
                break;
            }
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
        for ( WireSegment wireSegment : wireSegments ) {
            if ( wireSegment == null ) {
                break;
            }
            wireSegment.repaint( graphics2D );
        }
    }
}
