package Wire;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class WireSegmentNode extends Point2D.Double implements Iterable<WireSegment> {
    private final WireSegment[] segments    = new WireSegment[4];
    private       int           numSegments = 0;

    public WireSegmentNode( final double x, final double y ) {
        super( x, y );
    }

    public WireSegmentNode( final Point2D point2D ) {
        x = point2D.getX();
        y = point2D.getY();
    }

    protected int getNumSegments() {
        return numSegments;
    }

    protected void addSegment( final WireSegment newSegment ) {
        for ( int index = 0; index < segments.length; index++ ) {
            if ( segments[index] == null ) {
                segments[index] = newSegment;
                numSegments++;
                break;
            }
        }
    }

    protected void removeSegment( final WireSegment oldSegment ) {
        for ( int index = 0; index < segments.length; index++ ) {
            if ( segments[index] == oldSegment ) {
                segments[index] = null;
                numSegments--;
                break;
            }
        }
    }

    @Override
    public Iterator< WireSegment > iterator() {
        return new Iterator<>() {
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return segments[nextIndex] != null;
            }

            @Override
            public WireSegment next() {
                return segments[nextIndex++];
            }
        };
    }

    @Override
    public Spliterator< WireSegment > spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    public void forEach( Consumer< ? super WireSegment > action ) {
        Iterable.super.forEach(action);
    }
}
