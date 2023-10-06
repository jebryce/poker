package Gate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class gsonGatesAdapter extends TypeAdapter<Gates> {
    @Override
    public void write( final JsonWriter jsonWriter, final Gates gates ) throws IOException {
        jsonWriter.beginArray();
        for ( Gate gate : gates ) {
            jsonWriter.beginObject();
            jsonWriter.name( "type" );
            jsonWriter.value(  );
        }
        jsonWriter.name( "name" );
        jsonWriter.value( student.getName() );
        jsonWriter.name("rollNo");
        jsonWriter.value(student.getRollNo());
        jsonWriter.endObject();
    }

    @Override
    public Gates read( final JsonReader jsonReader ) throws IOException {
        return null;
    }
}
