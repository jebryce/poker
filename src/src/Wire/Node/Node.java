package Wire.Node;

import Container.ListItem;

import java.awt.*;
import java.awt.geom.Point2D;

public class Node extends ListItem {
    private final Nodes   previousNodes = new Nodes( 3 );
    private final Nodes   nextNodes     = new Nodes( 3 );

    private final Point2D location      = new Point2D.Double();

    public Node( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public void connectNode( final Node connectedNode ) {
        this.connectToNode( connectedNode );
        connectedNode.connectToNode( this );
    }

    private void connectToNode( final Node connectedNode) {
        assert connectedNode != this : "Cannot connect node to itself.";
        if ( connectedNode.location.getX() > location.getX() ) {
            nextNodes.add( connectedNode );
        } else if ( connectedNode.location.getX() < location.getX() ) {
            previousNodes.add( connectedNode );
        } else if ( connectedNode.location.getY() > location.getY() ) {
            nextNodes.add( connectedNode );
        } else if ( connectedNode.location.getY() < location.getY() ) {
            previousNodes.add( connectedNode );
        } else {
            assert false : "Cannot connect node to a node at the same location.";
        }
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Node node : nextNodes ) {
            graphics2D.drawLine(
                    (int) location.getX(), (int) location.getY(), (int) node.location.getX(), (int) node.location.getY()
            );
        }
    }

    public Point2D getLocation() {
        return (Point2D) location.clone();
    }
}
