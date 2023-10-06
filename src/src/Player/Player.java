package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import Main.KeyBinds;
import Main.KeyHandler;
import Main.MouseHandler;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Wires;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private final KeyHandler    keyHandler;
    private       Gate          heldGate = null;
    private       Wire          heldWire = null;
    private       Node          heldWireNode = null;
    private final Point2D       playerLocation = new Point2D.Double();
    private final Gates         gates          = new Gates();
    private       PlayerMode    playerMode     = PlayerMode.NORMAL;

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;

        Gate in0 = gates.add( new Input(   new Point2D.Double( 100, 100 ) ) ).place();
        Gate in1 = gates.add( new Input(   new Point2D.Double( 100, 500 ) ) ).place();
        Gate and = gates.add( new AndGate( new Point2D.Double( 400, 400 ) ) ).place();
        Gate out = gates.add( new Output(  new Point2D.Double( 800, 600 ) ) ).place();

        gates.connectGates( in0, and );
        gates.connectGates( in1, and );
        gates.connectGates( and, out );

    }

    public void repaint( final Graphics2D graphics2D  ) {
        gates.repaint( graphics2D );

        if ( heldGate != null ) {
            Point2D centerOffset = heldGate.getCenterOffset();
            heldGate.setLocation( playerLocation.getX() - centerOffset.getX(), playerLocation.getY() - centerOffset.getY() );
            heldGate.repaintInHand( graphics2D );
        }
    }

    public void update() {
        gates.update();

        playerLocation.setLocation( mouseHandler.xPos, mouseHandler.yPos );

        checkKeyPresses();

        switch ( playerMode ) {
            case NORMAL        -> updateNORMAL();
            case PLACE_GATE    -> updatePLACE_GATE();
            case PLACE_WIRE    -> updatePLACE_WIRE();
        }
    }

    private void checkKeyPresses() {
        if ( keyHandler.isKeyPressed( KeyBinds.placeWire ) ) {
            clearHand();
            playerMode = PlayerMode.PLACE_WIRE;
        }

        boolean makeGateNot = keyHandler.isKeyHeld( KeyBinds.makeGateNOT );
        if ( keyHandler.isKeyPressed( KeyBinds.newIOGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new Output(   playerLocation ); }
            else               { heldGate = new Input(    playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newNOTGate ) ) {
            clearHand();
                                 heldGate = new NotGate(  playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newANDGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new NandGate( playerLocation ); }
            else               { heldGate = new AndGate(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new NorGate(  playerLocation ); }
            else               { heldGate = new OrGate(   playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newXORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new XNorGate( playerLocation ); }
            else               { heldGate = new XorGate(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }

        if ( keyHandler.isKeyPressed( KeyBinds.clearHand ) ) {
            clearHand();
        }
    }

    private void clearHand() {
        heldGate = null;
        heldWire = null;
        if ( heldWireNode != null ) {
            heldWireNode.clearPlayerNode();
        }
        heldWireNode = null;
        playerMode = PlayerMode.NORMAL;
    }



    private void updateNORMAL() {
        if ( mouseHandler.isMouseClicked() ) {
            Gate gate = gates.findContainingGate( playerLocation );
            if ( gate instanceof Input ) {
                ((Input) gate).flipState();
            }
        }
    }

    private void updatePLACE_GATE() {
        if ( mouseHandler.isMouseClicked() ) {
            if ( heldGate != null ) {
                heldGate.place();
                gates.add( heldGate );
                heldGate = null;
                playerMode = PlayerMode.NORMAL;
            }
        }
    }

    private void updatePLACE_WIRE() {
        if ( heldWireNode != null ) {
            heldWireNode.setPlayerNode( playerLocation );
        }
        if ( !mouseHandler.isMouseClicked() ) {
            return;
        }


        Wires containingWires = gates.findContainingWires( playerLocation );
        Node containingNode;

        for ( Wire wire : containingWires ) {
            if ( wire == heldWire ) {
                continue;
            }
            containingNode = wire.findContainingNode( playerLocation );
            if ( heldWireNode == null ) {
                if ( containingNode == null ) {
                    continue;
                }
                heldWireNode = containingNode;
                heldWire = wire;
                return;
            }
            if ( containingNode != null ) {
                gates.connectWires( heldWire, wire, heldWireNode, containingNode );
                clearHand();
                return;
            }
        }
        if ( heldWireNode != null ) {
            heldWireNode = heldWire.placePlayerNode( heldWireNode );
        }
    }
}
