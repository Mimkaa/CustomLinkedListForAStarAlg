package nodepackage;
public class Node<T> 
{
    private Node<T> next;
    private Node<T> prev;
    private T current;

    public Node(T obj)
    {
        current = obj;
        next = null;
        prev = null;
    }

    public void SetPrev(Node<T> obj)
    {
        prev = obj;
    }

    public void SetNext(Node<T> obj)
    {
        next = obj;
    }

    public void print()
    {
        System.out.println(current);
    }

    public T GetValue()
    {
        return current;
    }

    
    public Node<T> GetNext()
    {
        return next;
    }

    public Node<T> GetPrev()
    {
        return prev;
    }

}
