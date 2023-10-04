package Node;

import Container.ListItem;
import Gate.Gate;
import Wire.Wire;

import java.awt.geom.Point2D;

public class Node extends ListItem {
    private final Gate     attachedGate;
    private final Point2D  location;
    private final Point2D  trueLocation = new Point2D.Double();
    private final NodeType nodeType;

    public Node( final Point2D playerLocation ) {
        location     = playerLocation;
        attachedGate = null;
        nodeType     = NodeType.PLAYER;
    }

    public Node( final Gate attachedGate, final NodeType nodeType, final int x, final int y ) {
        location          = new Point2D.Double( x, y );
        this.attachedGate = attachedGate;
        this.nodeType     = nodeType;
        trueLocation.setLocation( location );
    }

    public void setTrueLocation() {
        assert attachedGate != null;
        trueLocation.setLocation( location.getX() + attachedGate.getLocation().getX(),
                location.getY() + attachedGate.getLocation().getY() );
    }

    public Point2D getLocation() {
        return (Point2D) location.clone();
    }

    public Point2D getTrueLocation() {
        return (Point2D) trueLocation.clone();
    }

    public Wire getAttachedWire() {
        assert attachedGate != null;
        return attachedGate.getWireAtNode( this );
    }

    public Gate getAttachedGate() {
        return attachedGate;
    }

    public NodeType getNodeType() { return nodeType; }
}
