package Gate.Chip;

import Gate.Gate;
import Main.Colors;
import Node.NodeType;
import Wire.Wire;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class Chip extends Gate {
    private final Arc2D notch = new Arc2D.Double( 40, -20, 40, 40, 180, 180, Arc2D.OPEN );


    public Chip( int x, int y ) {
        super(x, y);
        body.append( new Rectangle2D.Double(0, 0, 120, 180), false );

        addNode( NodeType.INPUT, 0, 30 );
        addNode( NodeType.INPUT, 0, 70 );
        addNode( NodeType.INPUT, 0, 110 );
        addNode( NodeType.INPUT, 0, 150 );

        addNode( NodeType.OUTPUT, 120, 30 );
        addNode( NodeType.OUTPUT, 120, 70 );
        addNode( NodeType.OUTPUT, 120, 110 );
        addNode( NodeType.OUTPUT, 120, 150 );
    }

    @Override
    public void select() {

    }

    @Override
    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.setColor( Colors.GRAY );
        graphics2D.fill( body );
        graphics2D.setColor( Colors.BLACK );
        graphics2D.fill( notch );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaint( graphics2D );
    }
}
