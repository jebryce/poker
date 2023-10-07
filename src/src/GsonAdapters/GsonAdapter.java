package GsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.awt.geom.Point2D;
import java.io.IOException;

public abstract class GsonAdapter< W > extends TypeAdapter< W > {

    protected String getNextField( final JsonReader jsonReader ) throws IOException {
        String fieldName = null;
        JsonToken token = jsonReader.peek();
        if ( token.equals( JsonToken.NAME ) ) {
            fieldName = jsonReader.nextName();
        }
        assert fieldName != null;
        return fieldName;
    }
}
