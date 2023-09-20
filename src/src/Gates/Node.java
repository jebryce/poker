package Gates;

import java.awt.geom.Point2D;

public class Node {
    private final Gate attachedGate;
    private final Point2D location     = new Point2D.Double();
    private final Point2D trueLocation = new Point2D.Double();

    public Node( final Gate attachedGate, final int x, final int y ) {
        location.setLocation( x, y );
        this.attachedGate = attachedGate;
        trueLocation.setLocation( location.getX() + attachedGate.location.getX(),
                location.getY() + attachedGate.location.getY() );
    }

    public Point2D getLocation() {
        return (Point2D) location.clone();
    }

    public Point2D getTrueLocation() {
        return (Point2D) trueLocation.clone();
    }

}
