package Gate;

import java.awt.*;
import java.awt.geom.Point2D;

import Container.LinkedList;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wires.WiresLL;
import Wire.Wires.Wires;
import Wire.Wire;

public class Gates extends LinkedList<Gate> {
    private final WiresLL wires    = new WiresLL();
    private       Gates   previous = null;

    @Override
    public Gate add( final Gate newGate ) {
        super.add( newGate );
        wires.addFromGate( newGate );
        return newGate;
    }

    public Gates getPrevious() {
        if ( previous == null ) {
            return this;
        }
        return previous;
    }

    public Gates setPrevious( final Gates newPrevious ) {
        previous = newPrevious;
        return this;
    }

    public Wires findContainingWires( final Point2D location ) {
        Wires returnWires = new Wires( wires.getLength() );
        for ( Wires gateIO : wires ) {
            for ( Wire wire : gateIO ) {
                if ( wire.isPointWithinBounds( location ) ) {
                    returnWires.add( wire );
                }
            }
        }
        return returnWires;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Wires gateIO : wires ) {
            for ( Wire wire : gateIO ) {
                wire.repaint( graphics2D );
            }
        }
        for ( Gate gate : this ) {
            gate.repaint( graphics2D );
        }
    }

    public void update() {
        for ( Gate gate : this ) {
            gate.update();
        }
    }

    public Gate findContainingGate( final Point2D point ) {
        for ( Gate gate : this ) {
            if ( gate.isPointWithin( point ) ) {
                return gate;
            }
        }
        return null;
    }


    public void connectWires( final Wire wire0, final Wire wire1, final Node wire0Node, final Node wire1Node ) {
        Gate wire1AttachedGate = getAttachedGate( wire1 );
        assert wire1AttachedGate != null : "No gate contains the wire " + wire1 + ".";
        wire1AttachedGate.replaceWire( wire1, wire0 );
        wire0.replaceWire( wire1, wire0Node, wire1Node );
    }

    private Gate getAttachedGate( final Wire wire ) {
        for ( Gate gate : this ) {
            if ( gate.contains( wire ) ) {
                return gate;
            }
        }
        assert false : "No gate contains the wire " + wire + ".";
        return null;
    }

    public void connectOutputWiresToGates() {
        for ( Wires gateIO : wires ) {
            for ( Wire wire : gateIO ) {
                for ( Node node : wire ) {
                    if ( node.getNodeType() != NodeType.INPUT ) {
                        continue;
                    }
                    for ( Gate gate : this ) {
                        if ( gate.isPointNear(node.getLocation(), 1) ) {
                            gate.getInputWires().replaceWireAtNode(wire, node);
                        }
                    }
                }
            }
        }
    }
}
