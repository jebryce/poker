package Gates;

import Main.Colors;
import Main.Constants;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Gate {
    protected final Path2D    body       = new Path2D.Float();
    protected final Point2D[] input_pts  = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final boolean[] inputs     = new boolean[Constants.MAX_NUM_IO];
    protected final Point2D[] output_pts = new Point2D.Float[Constants.MAX_NUM_IO];
    protected final boolean[] outputs    = new boolean[Constants.MAX_NUM_IO];


    public void repaint( final Graphics2D graphics2D ) {
        graphics2D.setColor( Colors.BLACK );
        graphics2D.setStroke( new BasicStroke( 6 ) );
        graphics2D.draw(body);

        repaintInputs( graphics2D );
        repaintOutputs( graphics2D );
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
