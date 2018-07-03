package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Hashtable;

//SplayTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x
//boolean contains( x )  --> Return true if x is found
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements a top-down splay tree.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/
public class SplayTree<AnyType extends Comparable<? super AnyType>, Value>
{
 /**
  * Construct the tree.
  */
	static int counter ,range;
	static PrintStream printhtml;
	
 public SplayTree( )
 {
     nullNode = new BinaryNode<AnyType, Integer>( null ,null);
     nullNode.left = nullNode.right = nullNode;
     root = nullNode;
     counter=0;
     range=0;
 }

 private BinaryNode<AnyType,Integer> newNode = null;  // Used between different inserts
 
 /**
  * Insert into the tree.
  * @param x the item to insert.
  */
 @SuppressWarnings("unchecked")
public void insert( AnyType x , Integer y)
 {
     if( newNode == null )
         newNode = new BinaryNode<AnyType,Integer>( null, null );
     newNode.element = x;
     newNode.value = y;

     if( root == nullNode )
     {
         newNode.left = newNode.right = nullNode;
         root = newNode;
     }
     else
     {
         root = splay( x, root , y );
			
			int compareResult = y.compareTo( root.value );
			
         if( compareResult > 0 )
         {
             newNode.left = root.left;
             newNode.right = root;
             root.left = nullNode;
             root = newNode;
         }
         else if( compareResult < 0 )
         {
             newNode.right = root.right;
             newNode.left = root;
             root.right = nullNode;
             root = newNode;
         }
         else
             root.element = (AnyType) (root.element.toString()+" "+x.toString());   // No duplicates
     }
     newNode = null;   // So next insert will call new
    
 }



 /**
  * Find the smallest item in the tree.
  * Not the most efficient implementation (uses two passes), but has correct
  *     amortized behavior.
  * A good alternative is to first call find with parameter
  *     smaller than any item in the tree, then call findMin.
  * @return the smallest item or throw UnderflowException if empty.
  */
 public AnyType findMin( )
 {
     if( isEmpty( ) )
        return null;

     BinaryNode<AnyType,Integer> ptr = root;

     while( ptr.left != nullNode )
         ptr = ptr.left;

     root = splay( ptr.element, root, ptr.value );
     return ptr.element;
 }

 /**
  * Find the largest item in the tree.
  * Not the most efficient implementation (uses two passes), but has correct
  *     amortized behavior.
  * A good alternative is to first call find with parameter
  *     larger than any item in the tree, then call findMax.
  * @return the largest item or throw UnderflowException if empty.
  */
 public AnyType findMax( )
 {
     if( isEmpty( ) )
        return null;

     BinaryNode<AnyType,Integer> ptr = root;

     while( ptr.right != nullNode )
         ptr = ptr.right;

     root = splay( ptr.element, root,ptr.value );
     return ptr.element;
 }

 /**
  * Find an item in the tree.
  * @param x the item to search for.
  * @return true if x is found; otherwise false.
  */


public int check(AnyType x,BinaryNode<AnyType, java.lang.Integer> search) throws NullPointerException {
	int val = x.compareTo(search.element);
	if(val==0)
		return search.value;
	else
	{
		check(x,search.left);
		check(x,search.right);
	}
		
	return -1;
		
}

/**
  * Make the tree logically empty.
  */
 public void makeEmpty( )
 {
     root = nullNode;
 }

 /**
  * Test if the tree is logically empty.
  * @return true if empty, false otherwise.
  */
 public boolean isEmpty( )
 {
     return root == nullNode;
 }

 private BinaryNode<AnyType,Integer> header = new BinaryNode<AnyType,Integer>( null,null ); // For splay
 
 /**
  * Internal method to perform a top-down splay.
  * The last accessed node becomes the new root.
  * @param x the target item to splay around.
  * @param t the root of the subtree to splay.
  * @return the subtree after the splay.
  */
 private BinaryNode<AnyType,Integer> splay( AnyType x, BinaryNode<AnyType,Integer> t, Integer y )
 {
     BinaryNode<AnyType,Integer> leftTreeMax, rightTreeMin;

     header.left = header.right = nullNode;
     leftTreeMax = rightTreeMin = header;

     nullNode.value = y;   // Guarantee a match

     for( ; ; )
     {
			int compareResult = y.compareTo( t.value );
			
         if( compareResult > 0 )
         {
             if( y.compareTo( t.left.value ) > 0 )
                 t = rotateWithLeftChild( t );
             if( t.left == nullNode )
                 break;
             // Link Right
             rightTreeMin.left = t;
             rightTreeMin = t;
             t = t.left;
         }
         else if( compareResult < 0 )
         {
             if( y.compareTo( t.right.value ) < 0 )
                 t = rotateWithRightChild( t );
             if( t.right == nullNode )
                 break;
             // Link Left
             leftTreeMax.right = t;
             leftTreeMax = t;
             t = t.right;
         }
         else
             break;
     }	

     leftTreeMax.right = t.left;
     rightTreeMin.left = t.right;
     t.left = header.right;
     t.right = header.left;
     return t;
 }

 /**
  * Rotate binary tree node with left child.
  * For AVL trees, this is a single rotation for case 1.
  */
 private static <AnyType> BinaryNode<AnyType,Integer> rotateWithLeftChild( BinaryNode<AnyType,Integer> k2 )
 {
     BinaryNode<AnyType,Integer> k1 = k2.left;
     k2.left = k1.right;
     k1.right = k2;
     return k1;
 }

 /**
  * Rotate binary tree node with right child.
  * For AVL trees, this is a single rotation for case 4.
  */
 private static <AnyType> BinaryNode<AnyType,Integer> rotateWithRightChild( BinaryNode<AnyType,Integer> k1 )
 {
     BinaryNode<AnyType,Integer> k2 = k1.right;
     k1.right = k2.left;
     k2.left = k1;
     return k2;
 }

 /**
  * Print the tree contents in sorted order.
  */
 public void printTree( )
 {
	 
     if( isEmpty( ) )
         System.out.println( "Empty tree" );
     else
         printTree( root );
     
 }

 public void printTree(BinaryNode t)
 {
     if (t != nullNode) {
   		printTree(t.left);
     	System.out.println(t.element);
   		printTree(t.right);
     }
 }

 public void printrange(int r,String j, PrintStream print_html) throws FileNotFoundException {
	 range = r;
	 int c = 0,d=0;
	 printhtml=print_html;
	 printhtml.print("<tr><td>");
	 printhtml.print(j);
	 printhtml.print("</td>");
	 if( isEmpty( ) )
         System.out.println( "Empty tree" );
     else
          c = rangeprintleft( root);
	 if((range-c)>0)
		 c = rangeprintright(root.right);
	 if((range-c)>0)
		 System.out.println("No more data than "+(c));
	 print_html.print("</tr>");
	 
	}
 public int rangeprintleft(BinaryNode<AnyType, java.lang.Integer> t) {
	 
	 if (t != nullNode && counter<range) 
	 {
		 	
	   		rangeprintleft(t.left);
	   		String[] text = t.element.toString().split(" ");
	   		for(String s: text)
	   		{
	   			
	   			if(counter<range)
	   			{
	   				
	   				printhtml.print("<td>");
	   				printhtml.print(s);
	   				printhtml.print("</td>");
	   				
	   				//System.out.println(s+" "+t.value);
	   			}
	   			
	   			counter++;
	   		}
	   		
	 }
	 return counter;
}
public int rangeprintright(BinaryNode<AnyType, java.lang.Integer> t) {
	 
	 if (t != nullNode && counter<range) 
	 {
		 	
		 	String[] text = t.element.toString().split(" ");
	   		for(String s: text)
	   		{
	   			if(counter<range)
	   			{
	   				printhtml.print("<td>");
	   				printhtml.print(s);
	   				printhtml.print("</td>");
		   			//System.out.println(s);
	   			}
	   			counter++;
	   		}
	   			
	   		rangeprintright(t.right);
	   		
	   			   		
	 }
	 return counter;
}
// Basic node stored in unbalanced binary search trees
 private static class BinaryNode<AnyType , Integer>
 {
         // Constructors
     BinaryNode( AnyType theElement, Integer theValue )
     {
         this( theElement, null, null, theValue );
     }

     BinaryNode( AnyType theElement, BinaryNode<AnyType,Integer> lt, BinaryNode<AnyType,Integer> rt,Integer theValue )
     {
         element  = theElement;
         left     = lt;
         right    = rt;
         value    = theValue;
     }

     
	AnyType element;            // The data in the node
     Integer value;
     BinaryNode<AnyType,Integer> left;   // Left child
     BinaryNode<AnyType,Integer> right;  // Right child
 }

 private BinaryNode<AnyType,Integer> root;
 private BinaryNode<AnyType,Integer> nullNode;

 public static void main( String [ ] args )
 {
     SplayTree<String,Integer> t = new SplayTree<String,Integer>( );

     t.insert("raja", 10);
     t.insert("ra", 10);
     t.insert("rja", 10);
     
//        t.insert("a", 4);
//        t.insert("w", 34);
//        t.insert("q", 43);
//        t.insert("i", 18);
//        t.insert("e", 21);
//        t.insert("x", 31);
//        
//        t.insert("f", 13);
//        t.insert("v", 2);t.insert("b", 24);
//        t.insert("p", 78);
//        t.insert("p", 178);
//        t.insert("p", 708);
//        t.insert("p", 7);
//        t.insert("p", 38);
//        t.insert("p", 58);
//        t.insert("p", 89);
//        t.insert("p", 60);
     //t.printrange(5);
    
     //t.printTree();
 }







}

