// Henrik Thulin heth7132
// Mattin Lotfi malo5163

package alda.heap;
// BinaryHeap class
//
// CONSTRUCTION: with optional capacity (that defaults to 100)
//               or an array containing initial items
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( )  --> Return smallest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class DHeap<AnyType extends Comparable<? super AnyType>>
{
    private int d = 2;

    public DHeap( )
    {
        this( 2 );
    }

    public DHeap( int d )
    {
    	if (d < 2)
    		throw new IllegalArgumentException("Must be atleast 2");
    	
    	this.d = d;    	
        currentSize = 0;        
        array = (AnyType[]) new Comparable[ DEFAULT_CAPACITY + 1 ];
    }
    
    public int parentIndex(int i) {
    	
    	if (i < 2)
    		throw new IllegalArgumentException();
    	
    	return (i + (d - 2)) / d;
    }

    public int firstChildIndex(int i) {
    	
    	if (i < 1)
    		throw new IllegalArgumentException();
    	
    	return i * d - (d - 2);    	
    }

    public int size() {
    	return currentSize;
    }
    
    
    public AnyType get(int i) {
    	return array[i];
    }
    
    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        if( currentSize == array.length - 1 )
            enlargeArray( array.length * 2 + 1 );

        int hole = ++currentSize;

        if (hole > 1) {
        
        	int parentHole;        	        	
        	
        	do {
        		
        		parentHole = parentIndex(hole);
        		
        		if (x.compareTo(array[parentHole]) < 0) {        			
        			array[hole] = array[parentHole];
        			hole = parentHole;
        		}
        		else
        			break;        		
        		         	
        	}
        	while (parentHole != 1);        	        
        	
        }
        
        array[ hole ] = x;
    }


    private void enlargeArray( int newSize )
    {
            AnyType [] old = array;
            array = (AnyType []) new Comparable[ newSize ];
            for( int i = 0; i < old.length; i++ )
                array[ i ] = old[ i ];
            
            System.out.println("New size = "+newSize);
    }
    
    /**
     * Find the smallest item in the priority queue.
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public AnyType findMin( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return array[ 1 ];
    }

    /**
     * Remove the smallest item from the priority queue.
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public AnyType deleteMin( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );

        AnyType minItem = findMin( );
        array[ 1 ] = array[ currentSize-- ];
        percolateDown( 1 );

        return minItem;
    }



    /**
     * Test if the priority queue is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return currentSize == 0;
    }

    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty( )
    {
        currentSize = 0;
    }

    private static final int DEFAULT_CAPACITY = 100;

    private int currentSize;      // Number of elements in heap
    private AnyType [ ] array; // The heap array

    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown( int hole )
    {    	    
    	int smallest =  firstChildIndex(hole);
    	
    	if (smallest <= currentSize) {
    	
    		for (int p = smallest + 1; p < smallest + d && p <= currentSize; p++) 
    			if (array[smallest].compareTo(array[p]) > 0)     				
    				smallest = p;    				
    		
    		if (array[hole].compareTo(array[smallest]) > 0) {    			    			
    			
    			AnyType tmp = array[hole];
    			array[hole] = array[smallest];
    			array[smallest] = tmp;
    			
    			percolateDown(smallest);
    		}    		
    	}
    }

        // Test program
    public static void main( String [ ] args )
    {
        int numItems = 10000;
        DHeap<Integer> h = new DHeap<>(5);
        int i = 37;

        for( i = 37; i != 0; i = ( i + 37 ) % numItems )
            h.insert( i );
        
        System.out.println(h.toString());
        
        for( i = 1; i < numItems; i++ )
        	
            if( h.deleteMin( ) != i )
                System.out.println( "Oops! " + i );
    }
    
    public String toString() {
    	
    	String str = "";
    	
    	for (int i = 1; i <= currentSize; i++)
    		str += array[i]+" ";
    	
    	return str + "\n";
    	
    }
    
}
