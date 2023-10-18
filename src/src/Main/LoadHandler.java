package Main;

import Gate.Gates;
import GsonAdapters.GsonGatesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.File;

public class LoadHandler {
    private static LoadHandler   instance = null;
    private final  Gson          gson;

    private LoadHandler() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter( Gates.class, new GsonGatesAdapter() );
        gson = builder.create();
    }

    public static LoadHandler get() {
        if ( instance == null ) {
            instance = new LoadHandler();
        }
        return instance;
    }

    public void repaint( final Graphics2D graphics2D ) {
        File file = new File ( Constants.SAVE_PATH );
    }
}
