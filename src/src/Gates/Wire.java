package Gates;

public class Wire {
    private boolean state = false;

    protected boolean getState() {
        return state;
    }

    protected void setState( final boolean newState ) {
        state = newState;
    }

    protected void flipState() {
        state = !state;
    }
}
