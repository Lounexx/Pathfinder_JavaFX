package utils;

import Objects.PathNode;
import Structure.NodeStructure;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class OptimizerPath {

    public static void optimizePath(ArrayList<PathNode> chosenNodes){
        ArrayList<PathNode> nodesToRemove = new ArrayList<>();
        int index;
        int gCost = chosenNodes.get(0).getgCost(),hCost = chosenNodes.get(0).gethCost();
        for(PathNode pathNode: chosenNodes){
            index = chosenNodes.indexOf(pathNode)+1;
            if(index < chosenNodes.size()){
                if(chosenNodes.get(index).getgCost() < gCost && chosenNodes.get(index).gethCost() > hCost){
                    nodesToRemove.add(chosenNodes.get(index));
                }else if(chosenNodes.get(index).getfCost() == pathNode.getfCost() && chosenNodes.get(index).getgCost() > pathNode.getgCost() && chosenNodes.get(index).gethCost() < pathNode.gethCost()){
                    nodesToRemove.add(pathNode);
                }
                else{
                    gCost = chosenNodes.get(index).getgCost();
                    hCost = chosenNodes.get(index).gethCost();
                }
            }

        }
        for (PathNode node:nodesToRemove){
            System.out.println(node.getfCost());
        }
        chosenNodes.removeAll(nodesToRemove);
        for(PathNode node:chosenNodes){
            node.getRectangle().setFill(Color.LIGHTCYAN);
        }
    }
}
