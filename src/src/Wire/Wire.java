package Wire;

import Main.Colors;
import Main.Constants;
import Wire.Node.Node;

import Container.LinkedList;
import Wire.Node.NodeType;

import java.awt.*;

public class Wire extends LinkedList<Node> {
    private boolean state = false;

    public Wire( final NodeType nodeType, final int x, final int y ) {
        Node start = new Node( x, y );
        Node end = null;
        if ( nodeType == NodeType.INPUT ) {
            end = new Node( x - Constants.MIN_LINE_LENGTH, y );
        } else if ( nodeType == NodeType.OUTPUT ) {
            end = new Node( x + Constants.MIN_LINE_LENGTH, y );
        }
        assert end != null;
        add( start );
        add( end );
        start.connectNode( end );
    }

    public void setState( final boolean newState ) {
        state = newState;
    }

    public boolean getState() {
        return state;
    }

    public void repaint( final Graphics2D graphics2D ) {
        if ( state ) {
            graphics2D.setColor( Colors.GREEN );
        }  else {
            graphics2D.setColor( Colors.RED );
        }
        for ( Node node : this ) {
            node.repaint( graphics2D );
        }
    }

}
