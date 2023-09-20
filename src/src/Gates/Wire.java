package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wire {
    private boolean state = false;
    private Node    attachedNode = null;

    public Wire( final Node attachedNode ) {
        this.attachedNode = attachedNode;
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
        Point2D node = attachedNode.getTrueLocation();

        int middleX = (int) ( (player.getX() + node.getX()) / 2 );

        graphics2D.drawLine( (int) node.getX(), (int) node.getY(), middleX, (int) node.getY() );
        graphics2D.drawLine( middleX, (int) node.getY(), middleX, (int) player.getY() );
        graphics2D.drawLine( middleX, (int) player.getY(), (int) player.getX(), (int) player.getY() );

    }
}
