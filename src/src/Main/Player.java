package Main;

import Gates.*;

import java.awt.*;
import java.awt.geom.Point2D;

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
        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            heldGate.setLocation(
                    (int) (mouseHandler.xPos - centerOffset.getX()),
                    (int) (mouseHandler.yPos - centerOffset.getY())
            );
            heldGate.repaint( graphics2D );
        }
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.repaint( graphics2D );
        }

        if ( playerMode == PlayerMode.PLACE_WIRE ) {
            denoteWireNodes( graphics2D );
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
            placedGates[numPlacedGates++] = heldGate;
            heldGate = null;
        }
    }

    private void denoteWireNodes( final Graphics2D graphics2D ) {
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.denoteOpenNodes( graphics2D );
        }
    }

}
