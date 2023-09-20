package Gates;

import Main.Constants;

import java.awt.geom.Point2D;

public class Nodes {
    private final Node[] nodes     = new Node[Constants.MAX_NUM_GATES*Constants.MAX_NUM_IO];
    private       int    num_nodes = 0;

    public void add_node( final Node newNode ) {
        nodes[num_nodes++] = newNode;
    }

    public Node findClosestNode( final Point2D location ) {
        Node closestNode       = null;
        double closestDistance = 0;
        for ( Node node : nodes ) {
            if ( node == null ) {
                return null;
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
