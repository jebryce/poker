package Wire;

import Container.IterableArray;

import java.awt.*;

public class Wires extends IterableArray<Wire> {

    public Wires( int maxLength ) {
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
}
