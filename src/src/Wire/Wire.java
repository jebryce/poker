package Wire;

import Main.Colors;
import Main.Constants;
import Wire.Node.Node;

import Container.LinkedList;
import Wire.Node.NodeType;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Wire extends LinkedList<Node> {
    private       boolean     state  = false;
    private final Rectangle2D bounds = new Rectangle2D.Double();

    public Wire( final NodeType nodeType, final double x, final double y ) {
        initialize( nodeType, x, y );
    }

    public Wire( final NodeType nodeType, final Point2D location ) {
        initialize( nodeType, location.getX(), location.getY() );
    }

    private void initialize( final NodeType nodeType, final double x, final double y ) {
        Node start = new Node( nodeType, x, y );
        Node end;
        bounds.setRect( x, y, 0, 0 );
        if ( nodeType == NodeType.INPUT ) {
            end = new Node( x - Constants.MIN_LINE_LENGTH, y );
        } else if ( nodeType == NodeType.OUTPUT ) {
            end = new Node( x + Constants.MIN_LINE_LENGTH, y );
        } else {
            return;
        }
        add( start );
        add( end );
        start.connectNode( end );
    }

    @Override
    public Node add( final Node newNode ) {
        super.add( newNode );
        updateBounds( newNode.getLocation() );
        return newNode;
    }

    private void updateBounds( final Point2D location ) {
        final double x = location.getX();
        final double y = location.getY();
        bounds.add( x + Constants.LINE_GRAB_RADIUS, y + Constants.LINE_GRAB_RADIUS );
        bounds.add( x - Constants.LINE_GRAB_RADIUS, y - Constants.LINE_GRAB_RADIUS );
    }

    private void recalculateBounds() {
        bounds.setFrame( getHead().getLocation(), new Dimension(0,0) );
        for ( Node node : this ) {
            updateBounds( node.getLocation() );
        }
    }

    public void setState( final boolean newState ) {
        state = newState;
    }

    public boolean getState() {
        return state;
    }

    public boolean isPointWithinBounds( final Point2D point ) {
        return bounds.contains( point );
    }

    public void repaint( final Graphics2D graphics2D ) {
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }  else {
            graphics2D.setColor( Colors.RED );
        }
        for ( Node node : this ) {
            node.repaint( graphics2D );
        }
        graphics2D.draw( bounds );
    }

    public Node findContainingNode( final Point2D location ) {
        for ( Node node : this ) {
            if ( node.isPointWithinBounds( location ) ) {
                if ( node.getNodeType() != NodeType.NORMAL ) {
                    continue;
                }
                return node;
            }
        }
        return null;
    }

    public Node placePlayerNode( final Node heldWireNode ) {
        Node newHeldWireNode = heldWireNode.placePlayerNode();
        add( newHeldWireNode );
        return newHeldWireNode;
    }

    public void flipState() {
        state = !state;
    }

    public void replaceWire( final Wire wire, final Node thisNode, final Node wireNode ) {
        for ( Node node : wire ) {
            add( node );
        }
        thisNode.connectNode( wireNode );
        setState( state || wire.state );
    }

    public void move( final Point2D location ) {
        for ( Node node : this ) {
            node.move( location );
        }
        recalculateBounds();
    }
}
