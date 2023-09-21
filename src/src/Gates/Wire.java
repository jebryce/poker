package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wire {
    private       boolean state         = false;
    private final Node[]  attachedNodes = new Node[Constants.MAX_NUM_WIRE_NODES];
    private       int     numNodes      = 0;

    public void attachToNode( final Node node ) {
        if ( node == null ) {
            return;
        }
        node.getAttachedGate().attachWire( node, this );
        this.attachedNodes[numNodes++] = node;
    }

    public void detach() {
        for ( int index = 0; index < Constants.MAX_NUM_WIRE_NODES; index++ ) {
            Node node = attachedNodes[index];
            attachedNodes[index] = null;
            if ( node == null ) {
                break;
            }
            node.getAttachedGate().detachWire( this );
        }
        numNodes = 0;
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

    protected boolean getState() {
        return state;
    }

    protected void setState( final boolean newState ) {
        state = newState;
    }

    protected void flipState() {
        state = !state;
    }

    public void repaintToHand( final Graphics2D graphics2D, final Point2D player ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( Constants.LINE_THICKNESS ) );
        Point2D node = attachedNodes[numNodes - 1].getTrueLocation();

        int middleX = (int) ( (player.getX() + node.getX()) / 2 );

        graphics2D.drawLine( (int) node.getX(), (int) node.getY(), middleX, (int) node.getY() );
        graphics2D.drawLine( middleX, (int) node.getY(), middleX, (int) player.getY() );
        graphics2D.drawLine( middleX, (int) player.getY(), (int) player.getX(), (int) player.getY() );
    }

    public void repaint( final Graphics2D graphics2D ) {
        System.out.println( numNodes );
        if ( numNodes != 2 ) {
            return;
        }
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }

        graphics2D.setStroke( new BasicStroke( Constants.LINE_THICKNESS ) );



        Point2D node0 = attachedNodes[0].getTrueLocation();
        Point2D node1 = attachedNodes[1].getTrueLocation();
        int middleX = (int) ( ( node0.getX() + node1.getX() ) / 2 );
        graphics2D.drawLine( (int) node0.getX(), (int) node0.getY(), middleX, (int) node0.getY() );
        graphics2D.drawLine( middleX, (int) node0.getY(), middleX, (int) node1.getY() );
        graphics2D.drawLine( middleX, (int) node1.getY(), (int) node1.getX(), (int) node1.getY() );
    }

}
