package Wire;

import Container.LinkedList;
import Gate.Gate;

public class WiresLL extends LinkedList<Wire> {

    public void addFromGate( final Gate newGate ) {
        for ( Wire wire : newGate.getInputWires() ) {
            add( wire );
        }
        for ( Wire wire : newGate.getOutputWires() ) {
            add( wire );
        }
    }

}
