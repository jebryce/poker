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
        Gate[] wire1AttachedGates = getAttachedGates( wire1 );
        for ( Gate gate : wire1AttachedGates ) {
            if ( gate == null ) {
                break;
            }
            gate.replaceWire( wire1, wire0 );
        }
        wire0.replaceWire( wire1, wire0Node, wire1Node );
    }

    private Gate[] getAttachedGates( final Wire wire ) {
        Gate[] attachedGates = new Gate[this.getLength()];
        int ct = 0;
        for ( Gate gate : this ) {
            if ( gate.contains( wire ) ) {
                attachedGates[ct++] = gate;
            }
        }
        assert ct > 0 : "No gate contains the wire " + wire + ".";
        return attachedGates;
    }

    private Gate findGateNear( final Point2D point2D ) {
        for ( Gate gate : this ) {
            if ( gate.isGateNear( point2D, 1 ) ) {
                return gate;
            }
        }
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
                        if ( gate.isGateNear( node.getLocation(), 1 ) ) {
                            gate.getInputWires().replaceWireAtNode(wire, node);
                        }
                    }
                }
            }
        }
    }

    public void deleteAtLocation( final Point2D location ) {
        deleteWireAtLocation( location );

        Gate gateToDelete = findContainingGate( location );
        if ( gateToDelete == null ) {
            return;
        }
        for ( Wire wire : gateToDelete.getInputWires() ) {
            deleteWire( wire );
        }
        for ( Wire wire : gateToDelete.getOutputWires() ) {
            deleteWire( wire );
        }
        wires.remove( gateToDelete.inputWires );
        wires.remove( gateToDelete.outputWires );
        this.remove( gateToDelete );
    }

    private void deleteWireAtLocation( final Point2D location ) {
        Wires     containingWires = findContainingWires( location );
        Wire      wire            = containingWires.getFirst();
        Node      containingNode  = null;
        if ( wire != null ) {
            containingNode = wire.findContainingNode( location );
        }
        if ( containingNode != null ) {
            deleteWire( wire );
        }
    }

    private void deleteWire( final Wire wire ) {
        for ( Node node : wire.ioNodes() ) {
            if ( node == null ) {
                break;
            }
            Gate nearGate = findGateNear ( node.getLocation() );
            assert nearGate != null : "ioNodes must have only nodes attached to gates";
            nearGate.replaceWire( wire, new Wire ( node.getNodeType(), node.getLocation() ) );
        }
    }

    public Point2D snapToNode( final Point2D playerLocation ) {
        Point2D snappedLocation;
        for ( Wires ioWire : wires ) {
            for ( Wire wire : ioWire ) {
                snappedLocation = wire.snapNode( playerLocation );
                if ( snappedLocation != null ) {
                    return snappedLocation;
                }
            }
        }
        return playerLocation;
    }
}
