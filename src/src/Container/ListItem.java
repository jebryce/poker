package Container;

public class ListItem {
    private ListItem next = null;
    private ListItem prev = null;

    protected void setNext( final ListItem nextItem) {
        next = nextItem;
    }

    protected ListItem getNext() {
        return next;
    }

    protected void setPrev( final ListItem prevItem) {
        prev = prevItem;
    }

    protected ListItem getPrev() {
        return prev;
    }
}