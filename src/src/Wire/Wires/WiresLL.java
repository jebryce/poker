package Wire.Wires;

import Container.LinkedList;
import Gate.Gate;
import Gate.Chip;

public class WiresLL extends LinkedList< Wires > {
    private int length = 0;

    public void addFromGate( final Gate newGate ) {
        if ( newGate instanceof Chip ) {
            length += add( ( (Chip) newGate ).getGateIO() ).getMaxLength();
        }
        else {
            length += add( newGate.getInputWires() ).getMaxLength();
            length += add( newGate.getOutputWires() ).getMaxLength();
        }
    }

    public int getLength() {
        return length;
    }
}
