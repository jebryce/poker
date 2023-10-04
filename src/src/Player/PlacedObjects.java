package Player;

import Gate.Gate;
import Gate.Gates;
import Node.Node;
import Node.Nodes;
import Wire.Wire;
import Wire.WireType;
import Wire.Wires;

import java.awt.*;

public class PlacedObjects {
    protected final Gates placedGates    = new Gates();
    protected final Wires wires          = new Wires();
    protected final Nodes nodes          = new Nodes();

    protected Gate placeGate( final Gate gate) {
        nodes.addNodesFromGate( gate );
        wires.addWiresFromGate( gate );
        gate.setWireTypes( WireType.UNCONNECTED );
        placedGates.add( gate );
        return gate;
    }

    protected void attachWireToNode( final Wire wire, final Node node ) {
        wires.remove( wire.attachToNode( node ) );
    }

    public void repaint( final Graphics2D graphics2D ) {
        wires.repaint( graphics2D );
        placedGates.repaint( graphics2D );
    }

    public void update() {
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.update();
        }
    }
}
