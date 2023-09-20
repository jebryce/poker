package Main;

import Gates.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Player {
    private final MouseHandler mouseHandler;
    private final KeyHandler   keyHandler;
    private       Gate         heldGate;
    private final Gate[]       placedGates    = new Gate[Constants.MAX_NUM_GATES];
    private       int          numPlacedGates = 0;
    private       PlayerMode   playerMode     = PlayerMode.NORMAL;

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;
    }

    public void repaint( final Graphics2D graphics2D  ) {
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.repaint( graphics2D );
        }
        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            heldGate.setLocation(
                    (int) (mouseHandler.xPos - centerOffset.getX()),
                    (int) (mouseHandler.yPos - centerOffset.getY())
            );
            heldGate.repaint( graphics2D );
        }

    }

    public void update() {
        int x = mouseHandler.xPos;
        int y = mouseHandler.yPos;
        switch ( keyHandler.getNumberPressed() ) {
            case -1 -> {}
            case 1  -> heldGate = new Input( x, y );
            case 2  -> heldGate = new Output( x, y );
            case 3  -> heldGate = new AndGate( x, y );
            case 4  -> heldGate = new OrGate( x, y );
            case 5  -> heldGate = new NotGate( x, y );
            default -> heldGate = null;
        }
        if ( keyHandler.isPlaceWirePressed() ) {
            playerMode = PlayerMode.PLACE_WIRE;
        }
        if ( mouseHandler.isMouseClicked() ) {
            if ( heldGate != null ) {
                placedGates[numPlacedGates++] = heldGate;
                heldGate = null;
            }
            if ( playerMode == PlayerMode.PLACE_WIRE ) {
                findNearestNode( x, y );
            }
        }
    }

    private void findNearestNode( final int x, final int y ) {
        Gate closestGate       = null;
        double closestDistance = 0;
        Point2D player         = new Point2D.Double( x, y );
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            double gateDistance = gate.getCenter().distance( player );
            if ( closestGate == null ) {
                closestDistance = gateDistance;
                closestGate = gate;
                continue;
            }
            if ( gateDistance < closestDistance ) {
                closestDistance = gateDistance;
                closestGate = gate;
            }
        }
        if ( closestGate == null ) {
            return;
        }

    }
}
