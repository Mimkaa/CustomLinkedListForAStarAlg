package nodeastarpackage;
import java.util.ArrayList;

public class NodeAStar
{
    public float localDist;
    public float gloabalDist;
    public float x;
    public float y;
    public int originalX;
    public int originalY;
    public ArrayList<NodeAStar> neighbours = new ArrayList<NodeAStar>();
    public NodeAStar owner;
    public boolean visited = false;
    public boolean isWall = false;

    
}