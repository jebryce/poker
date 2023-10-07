package Player;

import Gate.*;
import Gate.BaseGates.*;
import Gate.IOGates.Input;
import Gate.IOGates.Output;
import GsonAdapters.GsonGatesAdapter;
import Main.KeyBinds;
import Main.KeyHandler;
import Main.MouseHandler;
import Wire.Node.Node;
import Wire.Wire;
import Wire.Wires;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private final MouseHandler  mouseHandler;
    private final KeyHandler    keyHandler;
    private       Gate          heldGate = null;
    private       Wire          heldWire = null;
    private       Node          heldWireNode = null;
    private final Point2D       playerLocation = new Point2D.Double();
    private       Gates         gates          = new Gates();
    private       PlayerMode    playerMode     = PlayerMode.NORMAL;
    private final Gson          gson;

    public Player( final MouseHandler mouseH, final KeyHandler keyH ) {
        mouseHandler = mouseH;
        keyHandler = keyH;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter( Gates.class, new GsonGatesAdapter() );
        gson = builder.create();
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
                                 heldGate = new gateNOT(  playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newANDGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNAND( playerLocation ); }
            else               { heldGate = new gateAND(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateNOR(  playerLocation ); }
            else               { heldGate = new gateOR(   playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newXORGate ) ) {
            clearHand();
            if ( makeGateNot ) { heldGate = new gateXNOR( playerLocation ); }
            else               { heldGate = new gateXOR(  playerLocation ); }
            playerMode = PlayerMode.PLACE_GATE;
        }
        else if ( keyHandler.isKeyPressed( KeyBinds.newChip ) ) {
            clearHand();
                                 heldGate = new Chip(     playerLocation );
            playerMode = PlayerMode.PLACE_GATE;
        }

        if ( keyHandler.isKeyPressed( KeyBinds.clearHand ) ) {
            clearHand();
        }

        if ( keyHandler.isKeyPressed( KeyBinds.save ) ) {
            String jsonString = gson.toJson( gates );
            System.out.println( jsonString );
        }

        if ( keyHandler.isKeyPressed( KeyBinds.load ) ) {
            String jsonString = "[[{\"gateType\":9,\"point\":[960.0,290.0]}],[{\"gateType\":8,\"point\":[212.0,287.0]},[{\"nodeType\":0,\"index\":0,\"point\":[590.0,312.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[550.0,312.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[302.0,312.0],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[262.0,312.0],\"connectedNodeIndexes\":[2]}]],[{\"gateType\":10,\"point\":[590.0,282.0]},[{\"nodeType\":0,\"index\":0,\"point\":[960.0,315.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":2,\"index\":1,\"point\":[920.0,315.0],\"connectedNodeIndexes\":[0,2]},{\"nodeType\":2,\"index\":2,\"point\":[750.0,312.0],\"connectedNodeIndexes\":[1,3]},{\"nodeType\":1,\"index\":3,\"point\":[710.0,312.0],\"connectedNodeIndexes\":[2]}],[{\"nodeType\":2,\"index\":0,\"point\":[750.0,352.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[710.0,352.0],\"connectedNodeIndexes\":[0]}],[{\"nodeType\":2,\"index\":0,\"point\":[750.0,392.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[710.0,392.0],\"connectedNodeIndexes\":[0]}],[{\"nodeType\":2,\"index\":0,\"point\":[750.0,432.0],\"connectedNodeIndexes\":[1]},{\"nodeType\":1,\"index\":1,\"point\":[710.0,432.0],\"connectedNodeIndexes\":[0]}]]]";
            gates = gson.fromJson( jsonString, Gates.class );
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
        if ( mouseHandler.isMouseLeftClicked() ) {
            Gate gate = gates.findContainingGate( playerLocation );
            if ( gate instanceof Input ) {
                ((Input) gate).flipState();
            }
            if ( gate instanceof Chip ) {
                gates = ((Chip) gate).getContents().setPrevious( gates );
            }
        }
        if ( mouseHandler.isMouseRightClicked() ) {
            gates = gates.getPrevious();
        }
    }

    private void updatePLACE_GATE() {
        if ( mouseHandler.isMouseLeftClicked() ) {
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
        if ( !mouseHandler.isMouseLeftClicked() ) {
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
