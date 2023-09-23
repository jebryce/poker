package Wire;

import Gate.Gate;
import Main.Constants;
import Node.Node;
import Node.NodeType;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wire {
    private       boolean  state         = false;
    private final Node[]   attachedNodes = new Node[Constants.MAX_NUM_WIRE_NODES];
    private final Gate[]   attachedGates = new Gate[Constants.MAX_NUM_WIRE_NODES];
    private       int      numNodes      = 0;
    private       WireType wireType      = WireType.UNCONNECTED;

    public Wire() {}

    public Wire( final Node node ) {
        attachToNode( node );
    }

    public WireType getWireType() {
        return wireType;
    }

    public void attachToNode( final Node node ) {
        if ( node == null ) {
            return;
        }
        Gate attachedGate = node.getAttachedGate();
        attachedGate.attachWire( node, this );
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
        }
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

    public void repaint( final Graphics2D graphics2D ) {
        switch ( wireType ) {
            case UNCONNECTED -> repaintUNCONNECTED( graphics2D );
        }
    }

    private void repaintUNCONNECTED( final Graphics2D graphics2D ) {
        for ( Node node : attachedNodes ) {
            if ( node == null ) {
                return;
            }
            int offset;
            if ( node.getNodeType() == NodeType.INPUT ) {
                offset = -40;
            } else {
                offset = 40;
            }

            Point2D location = node.getTrueLocation();
            graphics2D.drawLine(
                    (int) location.getX() + offset, (int) location.getY(), (int) location.getX(), (int) location.getY()
            );
        }
    }

    /*
    public void repaintToHand( final Graphics2D graphics2D, final Point2D player ) {
        Point2D node = attachedNodes[numNodes - 1].getTrueLocation();

        int middleX = (int) ( (player.getX() + node.getX()) / 2 );

        graphics2D.drawLine( (int) node.getX(), (int) node.getY(), middleX, (int) node.getY() );
        graphics2D.drawLine( middleX, (int) node.getY(), middleX, (int) player.getY() );
        graphics2D.drawLine( middleX, (int) player.getY(), (int) player.getX(), (int) player.getY() );
    }

    public void repaint( final Graphics2D graphics2D ) {
        if ( numNodes != 2 ) {
            return;
        }
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }



        Point2D node0 = attachedNodes[0].getTrueLocation();
        Point2D node1 = attachedNodes[1].getTrueLocation();
        int middleX = (int) ( ( node0.getX() + node1.getX() ) / 2 );
        graphics2D.drawLine( (int) node0.getX(), (int) node0.getY(), middleX, (int) node0.getY() );
        graphics2D.drawLine( middleX, (int) node0.getY(), middleX, (int) node1.getY() );
        graphics2D.drawLine( middleX, (int) node1.getY(), (int) node1.getX(), (int) node1.getY() );
    }
    */
}
