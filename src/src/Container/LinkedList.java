package Container;

import Wire.Node.Node;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class LinkedList< W extends ListItem > extends ListItem implements Iterable< W > {
    private W   head = null;
    private int length = 0;

    public Iterator< W > iterator() {
        return new Iterator<>() {
            private ListItem nextItem    = head;

            @Override
            public boolean hasNext() {
                return nextItem != null;
            }

            @Override
            public W next() {
                ListItem currentItem = nextItem;
                nextItem = nextItem.getNext();
                return (W) currentItem;
            }
        };
    }

    public W add( final W newItem ) {
        length++;
        newItem.setNext( head );
        head = newItem;
        return newItem;
    }

    public boolean remove( final W itemToRemove ) {
        W previousItem = null;
        for ( W currentItem : this ) {
            if ( currentItem == itemToRemove ) {
                if ( previousItem == null ) {
                    head = (W) currentItem.getNext();
                } else {
                    previousItem.setNext( currentItem.getNext() );
                }
                length--;
                return true;
            }
            previousItem = currentItem;
        }
        return false;
    }

    public int getLength() {
        return length;
    }

    public W getHead() {
        return head;
    }

    public void removeHead() {
        if ( head == null ) {
            return;
        }
        head = (W) head.getNext();
    }

    public int getIndex( final ListItem item ) {
        ListItem next = head;
        for ( int index = 0; index < length; index++ ) {
            if ( next == item ) {
                return index;
            }
            next = next.getNext();
        }
        assert false : item + " is not in this linked list.";
        return -1;
    }

    public void removeAll() {
        ListItem current;
        ListItem next    = head;
        while ( next != null ) {
            current = next;
            next    = next.getNext();
            current.setNext( null );
        }
        head = null;
    }
}
