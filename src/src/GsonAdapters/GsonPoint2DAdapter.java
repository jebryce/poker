package GsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GsonPoint2DAdapter extends GsonAdapter< Point2D > {
    @Override
    public void write( final JsonWriter jsonWriter, final Point2D point2D ) throws IOException {
        jsonWriter.name( "point" );
        jsonWriter.beginArray();
        jsonWriter.value( point2D.getX() ).value( point2D.getY() );
        jsonWriter.endArray();
    }

    @Override
    public Point2D read( JsonReader jsonReader ) throws IOException {
        Point2D point = null;
        String fieldName = getNextField( jsonReader );
        if ( fieldName.equals( "point" ) ) {
            jsonReader.beginArray();
            point = new Point2D.Double( jsonReader.nextDouble(), jsonReader.nextDouble() );
            jsonReader.endArray();
        }
        return point;
    }
}
