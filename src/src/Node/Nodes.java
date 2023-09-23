package Node;

import Gate.Gate;
import Main.Constants;

import java.awt.geom.Point2D;

public class Nodes {
    private final Node[] nodes     = new Node[Constants.MAX_NUM_GATES*Constants.MAX_NUM_IO];
    private       int    num_nodes = 0;

    public void addNodesFromGate( final Gate newGate) {
        for ( Node node : newGate.getNodes() ) {
            if ( node == null ) {
                break;
            }
            node.setTrueLocation();
            add_node( node );
        }

    }

    public void add_node( final Node newNode ) {
        if ( newNode == null ) {
            return;
        }
        nodes[num_nodes++] = newNode;
    }

    public Node findClosestNode( final Point2D location ) {
        Node closestNode       = null;
        double closestDistance = 0;
        for ( Node node : nodes ) {
            if ( node == null ) {
                break;
            }
            double nodeDistance = node.getTrueLocation().distance( location );
            if ( closestNode == null ) {
                closestDistance = nodeDistance;
                closestNode = node;
            }
            if ( nodeDistance < closestDistance ) {
                closestDistance = nodeDistance;
                closestNode = node;
            }
        }
        return closestNode;
    }

}
