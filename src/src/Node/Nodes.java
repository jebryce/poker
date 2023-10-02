package Node;

import Container.LinkedList;
import Gate.Gate;

import java.awt.geom.Point2D;

public class Nodes extends LinkedList<Node> {

    public void addNodesFromGate( final Gate newGate) {
        for ( Node node : newGate.getNodes() ) {
            if ( node == null ) {
                break;
            }
            node.setTrueLocation();
            add( node );
        }
    }

    public Node findClosestNode( final Point2D location ) {
        Node closestNode       = null;
        double closestDistance = 0;
        for ( Node node : this ) {
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
