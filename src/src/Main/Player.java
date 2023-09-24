package Main;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Node.Node;
import Node.Nodes;
import Wire.Wire;
import Wire.WireType;
import Wire.Wires;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Player {
    private final MouseHandler mouseHandler;
    private final KeyHandler   keyHandler;
    private       Gate         heldGate;
    private final Gate[]       placedGates    = new Gate[Constants.MAX_NUM_GATES];
    private final Wires        wires          = new Wires();
    private       int          numPlacedGates = 0;
    private       int          numPlacedWires = 0;
    private       PlayerMode   playerMode     = PlayerMode.NORMAL;
    private final Nodes        nodes          = new Nodes();
    private       Wire         heldWire       = null;
    private final Point2D      playerLocation = new Point2D.Double();

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;
    }

    public void repaint( final Graphics2D graphics2D  ) {
        wires.repaint( graphics2D );
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.repaint( graphics2D );
        }
        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            heldGate.setLocation(
                    (int) (playerLocation.getX() - centerOffset.getX()),
                    (int) (playerLocation.getY()- centerOffset.getY())
            );
            heldGate.repaint( graphics2D );
            for ( Wire wire : heldGate.getWires() ) {
                if ( wire == null ) {
                    break;
                }
                wire.repaint( graphics2D );
            }
        }

    }

    public void update() {
        playerLocation.setLocation( mouseHandler.xPos, mouseHandler.yPos );
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.update();
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
        if ( mouseHandler.isMouseClicked() ) {
            Gate    closestGate     = null;
            double  closestDistance = 0;
            for ( Gate gate : placedGates ) {
                if ( gate == null ) {
                    break;
                }
                double gateDistance = gate.getCenter().distance( playerLocation );
                if ( closestGate == null ) {
                    closestDistance = gateDistance;
                    closestGate = gate;
                }
                if ( gateDistance < closestDistance ) {
                    closestDistance = gateDistance;
                    closestGate = gate;
                }
            }
            if ( closestGate != null ) {
                closestGate.flipState();
            }
        }


        checkHeldGate();
    }

    private void checkHeldGate() {
        int x = (int) playerLocation.getX();
        int y = (int) playerLocation.getY();
        int numberPressed = keyHandler.getNumberPressed();
        switch ( numberPressed ) {
            case -1 -> {}
            case 1  -> { heldGate = new Input( x, y );    updateHeldGate(); }
            case 2  -> { heldGate = new Output( x, y );   updateHeldGate(); }
            case 3  -> { heldGate = new NotGate( x, y );  updateHeldGate(); }
            case 4  -> { heldGate = new AndGate( x, y );  updateHeldGate(); }
            case 5  -> { heldGate = new NandGate( x, y ); updateHeldGate(); }
            case 6  -> { heldGate = new OrGate( x, y );   updateHeldGate(); }
            case 7  -> { heldGate = new NorGate( x, y );  updateHeldGate(); }
            case 8  -> { heldGate = new XorGate( x, y );  updateHeldGate(); }
            case 9  -> { heldGate = new XNorGate( x, y ); updateHeldGate(); }
            default -> { heldGate = null;                 playerMode = PlayerMode.NORMAL; }
        }
    }

    private void updateHeldGate() {
        playerMode = PlayerMode.PLACE_GATE;
        heldGate.setWireTypes( WireType.HELD_GATE);
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
            wires.addWiresFromGate( heldGate );
            heldGate.setWireTypes( WireType.UNCONNECTED );
            placedGates[numPlacedGates++] = heldGate;
            heldGate = null;
        }
    }

    private void updatePLACE_WIRE() {
        if ( keyHandler.isPlaceWirePressed() ) {
            if ( heldWire != null ) {
                heldWire.detachFromPlayer();
            }
            heldWire = null;
            playerMode = PlayerMode.NORMAL;
            return;
        }

        if ( mouseHandler.isMouseClicked() ) {
            Node closestNode = nodes.findClosestNode( playerLocation );
            if ( closestNode == null ) {
                playerMode = PlayerMode.NORMAL;
                return;
            }
            if ( heldWire == null ) {
                heldWire = closestNode.getAttachedWire();
                heldWire.attachToPlayer( playerLocation );
                heldWire.setWireType( WireType.HELD_IN_HAND );
            }
            else if ( !heldWire.hasAttachedNode(closestNode) ) {
                playerMode = PlayerMode.NORMAL;
                Wire disconnectedWire = heldWire.attachToNode( closestNode );
                wires.remove_wire( disconnectedWire );
                heldWire = null;
            }

        }
    }
}
