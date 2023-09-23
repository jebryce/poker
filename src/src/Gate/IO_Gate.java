package Gate;

import Gate.Gate;
import Main.Colors;

import java.awt.*;

public class IO_Gate extends Gate {
    public IO_Gate( int x, int y ) {
        super(x, y);
    }

    @Override
    public void repaint( Graphics2D graphics2D ) {
        graphics2D.translate( location.getX(), location.getY() );
        if ( wires[0] != null && wires[0].getState() ) {
            graphics2D.setColor( Colors.GREEN );
        }
        else {
            graphics2D.setColor( Colors.RED );
        }
        graphics2D.fill( body );
        graphics2D.translate( -location.getX(), -location.getY() );
        super.repaint(graphics2D);
    }
}
