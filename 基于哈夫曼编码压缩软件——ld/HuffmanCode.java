import java.util.*;
public class HuffmanCode {
    Map<Byte, String> mymap;
    List<Node> nodes;
    byte[] content;
    public HuffmanCode(byte[] contentin) {
        this.content = contentin;
    }

    public void Huffmandisplay() {
        mymap=new HashMap<>();
        nodes = getNodes(content);
        Node huffmanTreeRoot = createHuffmanTree(nodes);
        getCode(huffmanTreeRoot,"");
    }
    //由原始字节数组获得节点以及权值
    public List<Node> getNodes(byte[] bytes) {
        ArrayList<Node> nodes = new ArrayList<>();
        Map<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes) {
            counts.merge(b, 1, Integer::sum);
        }
        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }
    //由节点造哈夫曼树
    public Node createHuffmanTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            Node parent = new Node(null, leftNode.weight + rightNode.weight);
            parent.left = leftNode;
            parent.right = rightNode;
            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }
        return nodes.get(0);
    }
    //依据哈夫曼树将每个节点赋予Huffman编码，结果存在mymap
    public void getCode(Node node,String path){
        if(node.left!=null){
            getCode(node.left,path+"0");
        }
        if(node.right!=null){
            getCode(node.right,path+"1");
        }
        if(node.left==null&&node.right==null){
            if(path.isEmpty()) {
                mymap.put(node.date, "0");
                return;
            }
            mymap.put(node.date,path);
        }
    }

}

