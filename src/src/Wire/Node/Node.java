package Wire.Node;

import Container.ListItem;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Point2D;

public class Node extends ListItem {
    private final Nodes       previousNodes = new Nodes( Constants.NUM_NEXT_NODES );
    private final Nodes       nextNodes     = new Nodes( Constants.NUM_NEXT_NODES );
    private       Node        playerNode    = null;
    private final NodeType    nodeType;

    private final Point2D     location      = new Point2D.Double();

    public Node( final double x, final double y ) {
        this.nodeType = NodeType.NORMAL;
        location.setLocation( x, y );
    }

    public Node( final NodeType nodeType, final double x, final double y ) {
        this.nodeType = nodeType;
        location.setLocation( x, y );
    }

    public Node( final NodeType nodeType, final Point2D point2D ) {
        this.nodeType = nodeType;
        location.setLocation( point2D );
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

    public void setPlayerNode( final Point2D playerLocation ) {
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        if ( Math.abs( playerX - location.getX() )  < Math.abs( playerY - location.getY() ) ) {
            playerNode = new Node( location.getX(), playerLocation.getY() );
        }
        else {
            playerNode = new Node( playerLocation.getX(), location.getY() );
        }
    }

    public void clearPlayerNode() {
        playerNode = null;
    }

    public Node placePlayerNode() {
        assert playerNode != null;
        Node returnNode = playerNode;
        connectNode( returnNode );
        clearPlayerNode();
        return returnNode;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Node node : nextNodes ) {
            graphics2D.drawLine(
                    (int) location.getX(), (int) location.getY(), (int) node.location.getX(), (int) node.location.getY()
            );
        }
        if ( playerNode != null ) {
            graphics2D.drawLine(
                    (int) location.getX(), (int) location.getY(),
                    (int) playerNode.location.getX(), (int) playerNode.location.getY()
            );
        }
    }

    public Point2D getLocation() {
        return (Point2D) location.clone();
    }

    public boolean isPointWithinBounds( final Point2D point ) {
        if ( point.getX() < location.getX() - Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getX() > location.getX() + Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getY() < location.getY() - Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        if ( point.getY() > location.getY() + Constants.LINE_GRAB_RADIUS ) {
            return false;
        }
        return true;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void move( final Point2D newLocation ) {
        this.location.setLocation( newLocation.getX() + location.getX(), newLocation.getY() + location.getY() );
    }

    public Nodes getNextNodes() {
        return nextNodes;
    }

    public Nodes getPreviousNodes() {
        return previousNodes;
    }
}
