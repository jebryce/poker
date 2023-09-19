package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Gate {
    protected final Path2D    body       = new Path2D.Float();
    protected final Point2D   location   = new Point2D.Float( 0, 0 );
    protected final Point2D[] input_pts  = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final Wire[]    inputs     = new Wire[Constants.MAX_NUM_IO];
    protected final Point2D[] output_pts = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final Wire[]    outputs    = new Wire[Constants.MAX_NUM_IO];

    public Gate( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public void setLocation( final int x, final int y ) {
        location.setLocation( x, y );
    }

    public Point2D getCenterOffset() {
        return new Point2D.Double( body.getBounds().getCenterX() , body.getBounds().getCenterY() );
    }

    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( 6 ) );
        graphics2D.translate( location.getX(), location.getY() );
        graphics2D.draw(body);



        repaintInputs( graphics2D );
        repaintOutputs( graphics2D );
        graphics2D.translate( -location.getX(), -location.getY() );
    }

    public void denoteOpenNodes( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.GREEN );
        for ( int index = 0; index < Constants.MAX_NUM_IO; index++ ) {
            if ( input_pts[index] != null ) {
                if ( inputs[index] == null ) {
                    graphics2D.fillOval(
                            (int) ( location.getX() + input_pts[index].getX() - 11),
                            (int) ( location.getY() + input_pts[index].getY() - 11), 22, 22
                    );
                }
            }
            if ( output_pts[index] != null ) {
                if ( outputs[index] == null ) {
                    graphics2D.fillOval(
                            (int) ( location.getX() + output_pts[index].getX() - 11),
                            (int) ( location.getY() + output_pts[index].getY() - 11), 22, 22
                    );
                }
            }
        }
    }

    private void repaintInputs( final Graphics2D graphics2D ) {
        for ( Point2D input : input_pts ) {
            if ( input == null ) {
                return;
            }
            graphics2D.drawLine( (int) input.getX(), (int) input.getY(),
                    (int) input.getX()-50, (int) input.getY() );
        }
    }

    private void repaintOutputs( final Graphics2D graphics2D ) {
        for ( Point2D output : output_pts ) {
            if ( output == null ) {
                return;
            }
            graphics2D.drawLine( (int) output.getX(), (int) output.getY(),
                    (int) output.getX()+50, (int) output.getY() );
        }
    }
}
