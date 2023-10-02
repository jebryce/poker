package Container;

public class ListItem {
    private ListItem next;

    protected void setNext( final ListItem nextItem) {
        next = nextItem;
    }

    protected ListItem getNext() {
        return next;
    }

    protected boolean hasNext() {
        return next != null;
    }
}