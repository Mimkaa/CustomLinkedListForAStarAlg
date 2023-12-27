package linkedlistpackage;
import nodepackage.Node;
import customexceptionpackage.CustomException;
import java.util.function.BiPredicate; 


public class LinkedList<T> 
{
    private Node<T> startingNode;
    private int size;


    public LinkedList(){
        this.startingNode = null;
        size=0;
    }
    
    public void Add(T obj)
    {
        Node<T> newNode = new Node<T>(obj);
        if (startingNode == null)
        {
            startingNode = newNode;
        } 
      
        else
        {
            // iteratively go till the end and then set the linkage
            Node<T> toIterate = startingNode;
           
            while (toIterate.GetNext()!=null) {
                toIterate = toIterate.GetNext();
            }

            toIterate.SetNext(newNode);
            newNode.SetPrev(toIterate);
            
        }
    }

    public Node<T> getFirst()
    {
        return startingNode;
    }

    public int GetSize()
    {
        if (startingNode!=null)
        {
            int i = 1;
            Node<T> toIterate = startingNode;
            
            while (toIterate.GetNext()!=null) {
                i+=1;
                toIterate = toIterate.GetNext();
            }
            
            return i;
        }
        else{return 0;}
    }

    
    public void Sort(BiPredicate<Node<T>,Node<T>> predicate)
    {
        int sizee = GetSize();
        Node<T> head = startingNode;
        for (int i = 0; i<= sizee-1; i++)
        {
            head = startingNode;
            
            for(int j = 0; j<=sizee-i-2; j++)
            {
                
                Node<T> currentHead = head;
                Node<T> nextNode = head.GetNext();
                
                if (predicate.test(head,nextNode))
                {
                    
                    //swap prev
                    Node<T> temp2 = currentHead.GetPrev();
                    currentHead.SetPrev(nextNode);
                    nextNode.SetPrev(temp2);
                    if(temp2!=null)
                    {
                        temp2.SetNext(nextNode);
                    }
                    else {
                        startingNode = nextNode; // Update startingNode if the first node is swapped(quite important)
                    }
                
                    // swap next
                    Node<T> temp = nextNode.GetNext();
                    nextNode.SetNext(currentHead);
                    currentHead.SetNext(temp);
                    if(temp!=null)
                    {
                        temp.SetPrev(currentHead);
                    }


                    head = nextNode;
                }
                
                head=head.GetNext(); 
                  
                    
                    
            }
            
        }
    }

    public boolean isEmpty()
    {
        return startingNode==null;
    }

    public void printAll()
    {
        System.out.println();
        Node<T> toIterate = startingNode;
        System.out.print(toIterate.GetValue() + " ");
        while (toIterate.GetNext()!=null) {
            toIterate = toIterate.GetNext();
            System.out.print(toIterate.GetValue() + " ");
        }
        System.out.println();
        
    }

    public Node<T> getLast()
    {
        Node<T> toIterate = startingNode;
           
        while (toIterate.GetNext()!=null) {
            toIterate = toIterate.GetNext();
        }
        return toIterate;
    }

    public void removeLast()
    {
        Node<T> toIterate = startingNode;
           
        while (toIterate.GetNext()!=null) {
            toIterate = toIterate.GetNext();
        }
        Node<T> prev = toIterate.GetPrev();
        prev.SetNext(null);
    }

    public void removeFirst()
    {
        Node<T> second = startingNode.GetNext();
        if (second==null)
        {
            startingNode = null;
        }
        else
        {
            startingNode = second;
            startingNode.SetPrev(null);
        }
    }



    public void AddFirst(T obj)
    {
        Node<T> newNode = new Node<T>(obj);
        startingNode.SetPrev(newNode);
        newNode.SetNext(startingNode);
        startingNode = newNode;
    }

    public void printLast()
    {
        try{
        Node<T> toIterate = startingNode;
        if (toIterate==null)
            {
                throw new CustomException("the list is empty");
            }
        while (toIterate.GetNext()!=null) {
            toIterate = toIterate.GetNext();
        }
        
        toIterate.print();
        }
        catch (CustomException e) {
            // Handle the exception
            System.out.println("Caught an exception: " + e.getMessage());
        }
        
    }

    public void printFirst()
    {
        startingNode.print();
    }
}
