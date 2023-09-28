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

        Gate in1 = placeGate( new Input( 100, 100 ) );
        Gate in2 = placeGate( new Input( 100, 500 ) );
        Gate and = placeGate( new AndGate( 400, 400 ) );
        Gate out = placeGate( new Output( 800, 600 ) );
        attachWireToNode( in1.getWires()[0], and.getNodes()[0] );
        attachWireToNode( in2.getWires()[0], and.getNodes()[1] );
        attachWireToNode( and.getWires()[2], out.getNodes()[0] );
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

        checkKeyPresses();

        switch ( playerMode ) {
            case NORMAL     -> updateNORMAL();
            case PLACE_GATE -> updatePLACE_GATE();
            case PLACE_WIRE -> updatePLACE_WIRE();
        }

    }

    private void checkKeyPresses() {
        if ( keyHandler.isPlaceWirePressed() ) {
            if ( heldWire != null ) {
                heldWire.detachFromPlayer();
                heldWire = null;
                playerMode = PlayerMode.NORMAL;
            }
            else {
                playerMode = PlayerMode.PLACE_WIRE;
            }
            return;
        }

        int x = (int) playerLocation.getX();
        int y = (int) playerLocation.getY();
        int numberPressed = keyHandler.getNumberPressed();
        switch ( numberPressed ) {
            case -1 -> {}
            case 1  -> { heldGate = new Input( x, y );    handleNewHeldGate(); }
            case 2  -> { heldGate = new Output( x, y );   handleNewHeldGate(); }
            case 3  -> { heldGate = new NotGate( x, y );  handleNewHeldGate(); }
            case 4  -> { heldGate = new AndGate( x, y );  handleNewHeldGate(); }
            case 5  -> { heldGate = new NandGate( x, y ); handleNewHeldGate(); }
            case 6  -> { heldGate = new OrGate( x, y );   handleNewHeldGate(); }
            case 7  -> { heldGate = new NorGate( x, y );  handleNewHeldGate(); }
            case 8  -> { heldGate = new XorGate( x, y );  handleNewHeldGate(); }
            case 9  -> { heldGate = new XNorGate( x, y ); handleNewHeldGate(); }
            default -> { heldGate = null;                 playerMode = PlayerMode.NORMAL; }
        }
    }

    private void handleNewHeldGate() {
        heldWire = null;
        playerMode = PlayerMode.PLACE_GATE;
        heldGate.setWireTypes( WireType.HELD_GATE);
    }

    private void updateNORMAL() {
        if ( mouseHandler.isMouseClicked() ) {
            for ( Gate gate : placedGates ) {
                if ( gate == null ) {
                    break;
                }
                if ( gate.isPointWithin(playerLocation) ) {
                    gate.flipState();
                    break;
                }
            }
        }
        if ( mouseHandler.isMouseHeld() ) {

        }
    }

    private void updatePLACE_GATE() {
        if ( mouseHandler.isMouseClicked() ) {
            playerMode = PlayerMode.NORMAL;
            placeGate( heldGate );
            heldGate = null;
        }
    }

    private Gate placeGate( final Gate gate) {
        nodes.addNodesFromGate( gate );
        wires.addWiresFromGate( gate );
        gate.setWireTypes( WireType.UNCONNECTED );
        placedGates[numPlacedGates++] = gate;
        return gate;
    }

    private void updatePLACE_WIRE() {
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
                attachWireToNode( heldWire, closestNode );
                heldWire = null;
            }
        }
    }

    private void attachWireToNode( final Wire wire, final Node node ) {
        wires.remove_wire( wire.attachToNode( node ) );
    }
}
