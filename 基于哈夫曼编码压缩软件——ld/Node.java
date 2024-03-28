//树的节点
public class Node implements Comparable<Node> {
    Byte date;
    int weight;
    Node left;
    Node right;
    public Node(Byte date, int weight) {
        this.date = date;
        this.weight = weight;
    }
    @Override
    public int compareTo(Node o) {
        return this.weight - o.weight;
    }
}

