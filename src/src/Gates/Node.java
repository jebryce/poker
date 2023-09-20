package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Point2D;

public class Node {
    private final Gate attachedGate;
    private final Point2D location = new Point2D.Double();

    public Node( final Gate attachedGate, final int x, final int y ) {
        location.setLocation( x, y );
        this.attachedGate = attachedGate;
    }

    public void highlight( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.GREEN );
        graphics2D.fillOval( (int) ( attachedGate.location.getX() + location.getX() - 11),
                (int) ( attachedGate.location.getY() + location.getY() - 11), 22, 22 );

    }
}
