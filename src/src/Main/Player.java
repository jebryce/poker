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
    private final Wire[]       placedWires    = new Wire[Constants.MAX_NUM_GATES*Constants.MAX_NUM_IO/2];
    private       int          numPlacedGates = 0;
    private       int          numPlacedWires = 0;
    private       PlayerMode   playerMode     = PlayerMode.NORMAL;
    private       PlayerMode   lastPlayerMode = PlayerMode.NORMAL;
    private final Nodes        nodes          = new Nodes();
    private       Node         closestNode    = null;
    private       Wire         heldWire       = null;

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;
    }

    public void repaint( final Graphics2D graphics2D  ) {
        for ( Wire wire : placedWires ) {
            if ( wire == null ) {
                break;
            }
            wire.repaint( graphics2D );
        }
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.repaint( graphics2D );
        }
        if ( heldWire != null ) {
            heldWire.repaintToHand( graphics2D, new Point2D.Double( mouseHandler.xPos, mouseHandler.yPos ) );
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
        if ( lastPlayerMode != playerMode ) {
            lastPlayerMode = playerMode;
        }

        switch ( playerMode ) {
            case NORMAL     -> updateNORMAL();
            case PLACE_GATE -> updatePLACE_GATE();
            case PLACE_WIRE -> updatePLACE_WIRE();
        }
    }

    private void updateNORMAL() {
        if ( keyHandler.isPlaceWirePressed() ) {
            playerMode = PlayerMode.PLACE_WIRE;
            return;
        }
        checkHeldGate();
    }

    private void checkHeldGate() {
        int x = mouseHandler.xPos;
        int y = mouseHandler.yPos;
        int numberPressed = keyHandler.getNumberPressed();
        switch ( numberPressed ) {
            case -1 -> {}
            case 1  -> { heldGate = new Input( x, y );   playerMode = PlayerMode.PLACE_GATE; }
            case 2  -> { heldGate = new Output( x, y );  playerMode = PlayerMode.PLACE_GATE; }
            case 3  -> { heldGate = new AndGate( x, y ); playerMode = PlayerMode.PLACE_GATE; }
            case 4  -> { heldGate = new OrGate( x, y );  playerMode = PlayerMode.PLACE_GATE; }
            case 5  -> { heldGate = new NotGate( x, y ); playerMode = PlayerMode.PLACE_GATE; }
            default -> { heldGate = null;                playerMode = PlayerMode.NORMAL; }
        }
    }

    private void updatePLACE_GATE() {
        checkHeldGate();
        if ( heldGate == null ) {
            playerMode = PlayerMode.NORMAL;
            return;
        }
        if ( mouseHandler.isMouseClicked() ) {
            playerMode = PlayerMode.NORMAL;
            nodes.addNodesFromGate( heldGate );
            placedGates[numPlacedGates++] = heldGate;
            heldGate = null;
        }
    }

    private void updatePLACE_WIRE() {
        if ( keyHandler.isPlaceWirePressed() ) {
            if ( heldWire != null ) {
                heldWire.detach();
            }
            heldWire = null;
            playerMode = PlayerMode.NORMAL;
            return;
        }

        if ( mouseHandler.isMouseClicked() ) {
            closestNode = nodes.findClosestNode( new Point2D.Double( mouseHandler.xPos, mouseHandler.yPos ) );
            if ( closestNode == null ) {
                playerMode = PlayerMode.NORMAL;
                return;
            }
            if ( heldWire == null ) {
                heldWire = new Wire();
                heldWire.attachToNode( closestNode );
            }
            else if ( !heldWire.hasAttachedNode( closestNode ) ) {
                playerMode = PlayerMode.NORMAL;
                heldWire.attachToNode( closestNode );
                placedWires[numPlacedWires++] = heldWire;
                heldWire = null;
            }

        }
    }
}
