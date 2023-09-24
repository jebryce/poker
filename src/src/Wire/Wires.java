package Wire;

import Gate.Gate;
import Main.Constants;

import java.awt.*;

public class Wires {
    private final Wire[] wires     = new Wire[Constants.MAX_NUM_GATES*Constants.MAX_NUM_IO];
    private       int    num_wires = 0;

    public void addWiresFromGate( final Gate newGate) {
        for ( Wire wire : newGate.getWires() ) {
            if ( wire == null ) {
                break;
            }
            add_wire( wire );
        }
    }

    public void remove_wire( final Wire wireToRemove ) {
        if ( wireToRemove == null ) {
            return;
        }
        int index;
        for ( index = 0; wires[index] != wireToRemove; index++ ) {
            if ( wires[index] == null ) {
                return;
            }
        }
        num_wires--;
        while ( wires[index] != null ) {
            wires[index] = wires[++index];
        }
    }

    public void add_wire( final Wire newWire ) {
        if ( newWire == null ) {
            return;
        }
        if ( num_wires >= Constants.MAX_NUM_GATES*Constants.MAX_NUM_IO ) {
            return;
        }
        wires[num_wires++] = newWire;
    }

    public void repaint( final Graphics2D graphics2D ) {
        for ( Wire wire : wires ) {
            if ( wire == null ) {
                break;
            }
            wire.repaint( graphics2D );
        }
    }
}
