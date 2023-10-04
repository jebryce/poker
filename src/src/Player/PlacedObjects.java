package Player;

import Gate.Gate;
import Gate.Gates;
import Node.Node;
import Node.Nodes;
import Wire.Wire;
import Wire.WireSegment;
import Wire.WireType;
import Wire.Wires;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlacedObjects {
    private final Gates         placedGates    = new Gates();
    private final Wires         wires          = new Wires();
    private final Nodes         nodes          = new Nodes();

    private       PlacedObjects previous       = this;

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

    protected PlacedObjects selectGate( final Point2D playerLocation ) {
        for ( Gate gate : placedGates ) {
            if ( gate == null ) {
                break;
            }
            if ( gate.isPointWithin( playerLocation ) ) {
                PlacedObjects selectedObjects = gate.select();
                if ( selectedObjects != null ) {
                    selectedObjects.previous = this;
                    return selectedObjects;
                }
            }
        }
        return this;
    }

    protected WireSegment findContainingWireSegment( final Point2D playerLocation ) {
        return wires.findContainingWireSegment( playerLocation );
    }

    protected Node findClosestNode( final Point2D playerLocation ) {
        return nodes.findClosestNode( playerLocation );
    }

    protected PlacedObjects getPrevious() {
        return previous;
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
