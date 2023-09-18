package Gates;


import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class OrGate extends Gate {

    public OrGate() {
        body.append( new Line2D.Float( 0, 0, 75, 0 ), false );
        body.append( new Arc2D.Float( 25, 0, 100, 100, 270, 180, Arc2D.OPEN ), false );
        body.append( new Line2D.Float( 0, 100, 75, 100 ), false );
        body.append( new Arc2D.Float( -50, 0, 100, 100, 270, 180, Arc2D.OPEN ), false );

        inputs[0] = new Point2D.Float( 45, 30 );
        inputs[1] = new Point2D.Float( 45, 70 );
    }
}
