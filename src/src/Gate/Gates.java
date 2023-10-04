package Gate;

import java.awt.*;
import Container.LinkedList;

public class Gates extends LinkedList<Gate> {

    public void repaint( final Graphics2D graphics2D ) {
        for ( Gate gate : this ) {
            gate.repaint( graphics2D );
        }
    }

}
