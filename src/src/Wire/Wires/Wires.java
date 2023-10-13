package Wire.Wires;

import Container.IterableArray;
import Wire.Node.Node;
import Wire.Node.NodeType;
import Wire.Wire;

import java.awt.*;

public class Wires extends IterableArray< Wire > {

    public Wires( final int maxLength ) {
        super( maxLength );
    }

    public boolean and() {
        boolean result = true;
        for ( Wire wire : this ) {
            result &= wire.getState();
        }
        return result;
    }

    public boolean or() {
        boolean result = false;
        for ( Wire wire : this ) {
            result |= wire.getState();
        }
        return result;
    }

    public boolean xor() {
        boolean result = false;
        for ( Wire wire : this ) {
            result ^= wire.getState();
        }
        return result;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Wire wire : this ) {
            wire.repaint( graphics2D );
        }
    }

    public void replaceAndShiftWireAtNode( final Wire newWire, final Node inputNode ) {
        replaceWireAtNode( newWire, inputNode, true );
    }

    public void replaceWireAtNode( final Wire newWire, final Node inputNode ) {
        replaceWireAtNode( newWire, inputNode, false );
    }

    private void replaceWireAtNode( final Wire newWire, final Node inputNode, final boolean shift ) {
        for ( Wire wire : this ) {
            if ( wire == newWire ) {
                return;
            }
            for ( Node node : wire ) {
                if ( node.getNodeType() != NodeType.INPUT ) {
                    continue;
                }
                if ( node.getLocation().equals( inputNode.getLocation() ) ) {
                    if ( shift ) {
                        remove( wire );
                    } else {
                        removeFirst( wire );
                    }
                }
            }
        }
        if ( shift)  {
            super.add( newWire );
        } else {
            super.addFirst( newWire );
        }
    }
}
