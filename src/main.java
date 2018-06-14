import lab.Edge;
import lab.Navigation;
import lab.Node;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class main {
    public static void main(String[] args) {
        Navigation n = new Navigation("TestFile2");
        /*ArrayList<Node> testNodes = n.getCities();

        System.out.println("Cities: ");
        for (int i = 0; i < testNodes.size(); i ++){
            System.out.println(testNodes.get(i).getName() + " " + testNodes.get(i).getDelay());
        }

        System.out.println("Edges: ");
        for(int j = 0; j < testNodes.size(); j ++) {
            for (int i = 0; i < testNodes.get(j).getEdges().size(); i++) {
                System.out.println(testNodes.get(j).getEdges().get(i).getA().getName() + " " + testNodes.get(j).getEdges().get(i).getB().getName() + " " + testNodes.get(j).getEdges().get(i).getDistance() + " " + testNodes.get(j).getEdges().get(i).getMaxSpeed());
            }
        }

        PriorityQueue<Node> q = new PriorityQueue<Node>(1, (Node n1, Node n2) -> n1.getDistanceToStart() < n2.getDistanceToStart() ? -1 : 1);

        for(int i = 0; i < testNodes.size(); i ++){
            q.offer(testNodes.get(i));
        }
        System.out.println("done adding");
        int counter = 0;
        while(q.size() > 0){
            if (counter == 2) {
                testNodes.get(3).setDistanceToStart(0);
                q.remove(testNodes.get(3));
                q.add(testNodes.get(3));
            }
            Node tempNode = q.poll();
            if (tempNode != null)
                System.out.println(tempNode.getName() + " " + tempNode.getDistanceToStart());
            else
                System.out.println("tempNode == null");
            counter++;
        }*/
        ArrayList<String> test = n.findShortestRoute("A", "E");
        System.out.println("Dijkstra");
        for (int i = 0; i < test.size(); i ++){
            System.out.println(test.get(i));
        }
    }
}
