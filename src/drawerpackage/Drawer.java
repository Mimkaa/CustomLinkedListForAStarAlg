package drawerpackage;
import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 
import nodeastarpackage.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.lang.Math.*;
import linkedlistpackage.*;
import java.util.function.BiPredicate;
import nodepackage.*;

public class Drawer extends JPanel implements MouseListener, MouseMotionListener,KeyListener
{
    private ArrayList<NodeAStar> nodes;
    private int width;
    private int height;
    private int nodeSize = 20;
    private NodeAStar mouseClosest = null;
    private float mouseX;
    private float mouseY;
    private NodeAStar StartNode;
    private NodeAStar EndNode;

    private void InitNodes()
    {
        // create nodes
        int padding = 20;
        int numNodesWidth = width/(nodeSize+padding);
        int numNodesHeight = height/(nodeSize+padding);
        for (int i = 0; i<numNodesWidth-1;i++)
        {
            for(int j = 0; j< numNodesHeight-1;j++)
            {
                NodeAStar node = new NodeAStar();
                node.x = (nodeSize + padding)*i+padding;
                node.y = (nodeSize+ padding)*j+padding/2;
                node.originalX = i;
                node.originalY = j;
                node.gloabalDist=Float.POSITIVE_INFINITY;
                node.localDist = Float.POSITIVE_INFINITY;
                node.owner = null;
                node.isWall = false;
                node.visited = false;
                nodes.add(node);
            }
        }


        // make connections
        for (NodeAStar n: nodes) 
        {
            // ok i will not be making anything super hyper netty witty here
            for (NodeAStar nn : nodes)
            {
                if(n!=nn)
                {
                    if(n.originalX+1 == nn.originalX && n.originalY == nn.originalY)
                    {
                        n.neighbours.add(nn);
                    }
                    if(n.originalX-1 == nn.originalX && n.originalY == nn.originalY)
                    {
                        n.neighbours.add(nn);
                    }
                    if(n.originalX == nn.originalX && n.originalY-1 == nn.originalY)
                    {
                        n.neighbours.add(nn);
                    }
                    if(n.originalX == nn.originalX && n.originalY+1 == nn.originalY)
                    {
                        n.neighbours.add(nn);
                    }
                }
            }
            
            
        }

        // start end nodes

        StartNode = nodes.get(0);
        EndNode = nodes.get(10);

    }

    public Drawer(int widthin, int heightin)
    {
        width = widthin;
        height = heightin;
        nodeSize = widthin/40;
        // Create a Timer with a 16 milliseconds delay (approximately 60 frames per second)
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code inside this actionPerformed method will be executed at the specified frame rate
                update();
                repaint();
            }
        });

        // Start the timer
        timer.start();
        nodes = new ArrayList<NodeAStar>();
        InitNodes();

        // Add the MouseListener to the panel
        addMouseListener(this);
        addMouseMotionListener(this);
        
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        // draw connections
        for (NodeAStar n: nodes) {
            for (NodeAStar nn:n.neighbours)
            {
                g.setColor(Color.BLUE);
                g.drawLine((int)n.x+nodeSize/2, (int)n.y+nodeSize/2, (int)nn.x+nodeSize/2, (int)nn.y+nodeSize/2);
            }
        }

        // draw nodes
        for (NodeAStar n: nodes) {
            
            g.setColor(Color.BLUE);
            
            if (n.visited)
            {
                Color customColor = new Color(0,0,139); 
                g.setColor(customColor);
            }
            if (n.isWall)
            {
                g.setColor(Color.GRAY);
            }
            g.drawRect((int)n.x, (int)n.y, nodeSize, nodeSize);
            g.fillRect((int)n.x, (int)n.y, nodeSize, nodeSize);
        }

        // draw start 
        g.setColor(Color.GREEN);
        g.drawRect((int)StartNode.x, (int)StartNode.y, nodeSize, nodeSize);
        g.fillRect((int)StartNode.x, (int)StartNode.y, nodeSize, nodeSize);

        // draw end
        g.setColor(Color.RED);
        g.drawRect((int)EndNode.x, (int)EndNode.y, nodeSize, nodeSize);
        g.fillRect((int)EndNode.x, (int)EndNode.y, nodeSize, nodeSize);

        // draw path
        if(EndNode.owner!=null)
        {
            NodeAStar endNodePtr = EndNode.owner;
            while (endNodePtr.owner!=null) 
            {
                g.setColor(Color.YELLOW);
                g.drawRect((int)endNodePtr.x, (int)endNodePtr.y, nodeSize, nodeSize);
                g.fillRect((int)endNodePtr.x, (int)endNodePtr.y, nodeSize, nodeSize);
                endNodePtr = endNodePtr.owner;

            }
        }

    }

    private float distance(float x1, float y1, float x2, float y2)
    {
        return (float)Math.sqrt(Math.pow((y2-y1),2)+Math.pow((x2-x1),2));
    }

    private void SolveAStar()
    {

        StartNode.gloabalDist = distance(StartNode.x, StartNode.y, EndNode.x, EndNode.y);
        StartNode.localDist = 0;
      

        LinkedList<NodeAStar> checkQueue = new LinkedList<NodeAStar>();
        checkQueue.Add(StartNode);
        

        while(!checkQueue.isEmpty())
        {
            
            // fisrt sort
            
            
            BiPredicate<Node<NodeAStar>,Node<NodeAStar>> cond = (a,b)->(a.GetValue().gloabalDist>b.GetValue().gloabalDist);// sort accending order
            checkQueue.Sort(cond);
            
            // remove all visited at the front
            while (!checkQueue.isEmpty() && checkQueue.getFirst().GetValue().visited) {
                checkQueue.removeFirst();
            }
            
            if(checkQueue.isEmpty())
            {
                break;
            }
            

            NodeAStar first = checkQueue.getFirst().GetValue();
            first.visited = true;

            if(first == EndNode)
            {
                break;
            }
            
            for (NodeAStar n: first.neighbours)
            {
                if(!n.visited && n.isWall == false)
                {
                    checkQueue.Add(n);
                    
                    
                }
                float probableClosest = first.localDist + distance(first.x, first.y, n.x, n.y);
                if(n.localDist > probableClosest && n.isWall == false)
                {
                    n.owner = first;
                    n.localDist = probableClosest;
                    n.gloabalDist = distance(n.x, n.y, EndNode.x, EndNode.y) + n.localDist;
                    

                }
            }
            
        }

    }

    public void update()
    {
        // find the mouse closest node
        float dist = Float.POSITIVE_INFINITY;
        for(NodeAStar n : nodes)
        {
            float distthere = distance(n.x,n.y,mouseX,mouseY);
            if(distthere<dist)
            {
                mouseClosest = n;
                dist = distthere;
            }
        }
        
        SolveAStar();
        
        

        
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        showMousePosition(e.getX(), e.getY(), "Mouse Clicked");
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showMousePosition(e.getX(), e.getY(), "Mouse Pressed");
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) 
        {
            if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) 
            {
                StartNode = mouseClosest;
                for(NodeAStar n:nodes)
                {
                    n.owner=null;
                    n.gloabalDist=Float.POSITIVE_INFINITY;
                    n.localDist = Float.POSITIVE_INFINITY;
                    n.visited = false;
                }
                
            }
            
        } else if (button == MouseEvent.BUTTON3) 
        {
            if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) 
            {
                EndNode = mouseClosest;
                for(NodeAStar n:nodes)
                {
                    n.owner=null;
                    n.gloabalDist=Float.POSITIVE_INFINITY;
                    n.localDist = Float.POSITIVE_INFINITY;
                    n.visited = false;
                }
            }
            else
            {
                mouseClosest.isWall = !mouseClosest.isWall;
                for(NodeAStar n:nodes)
                {
                    n.owner=null;
                    n.gloabalDist=Float.POSITIVE_INFINITY;
                    n.localDist = Float.POSITIVE_INFINITY;
                    n.visited = false;
                }
            }
        }

        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showMousePosition(e.getX(), e.getY(), "Mouse Released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        showMousePosition(e.getX(), e.getY(), "Mouse Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        showMousePosition(e.getX(), e.getY(), "Mouse Exited");
    }

    private void showMousePosition(int x, int y, String action) {
        System.out.println(action + ": X = " + x + ", Y = " + y);
    }

    // keyboard stuff
    @Override
    public void keyTyped(KeyEvent e) {
        // Implement if needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Implement if needed
    }

    private void handleKeyPress(KeyEvent e) 
    {
        

        
    }

}

