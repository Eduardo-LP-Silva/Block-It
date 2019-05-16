package game;

import game.GameBoard;
import game.node.*;
import java.util.PriorityQueue;

import java.util.ArrayList;

public class Player
{
    protected static int MAX_BARRIERS;
    protected static GameNode node;

    protected int[] position;
    protected int barriers;
    protected int difficulty;
    protected char color;

    public Player(int difficulty, int[] position, char color)
    {
        this.position = position;
        this.barriers = MAX_BARRIERS;
        this.difficulty = difficulty;
        this.color = color;
    }

    //Bot function
    public void play()
    {
        System.out.println(this.getDistanceFromBorder(color));

        for(int i = 0; i < Node.getSolution().size(); i++)
            System.out.println(Node.getSolution().get(i));
    }

    public boolean move(String move)
    {
        GameBoard newBoard;

        switch(move)
        {
            case "up":
                newBoard = node.getGameBoard().moveUp(position);
                break;

            case "down":
                newBoard = node.getGameBoard().moveDown(position);
                break;

            case "left":    
                newBoard = node.getGameBoard().moveLeft(position);
                break;

            case "right":
                newBoard = node.getGameBoard().moveRight(position);
                break;

            default:
                System.out.println("Invalid move: " + move);
                return false;
        }

        if(newBoard != null)
        {
            node.setGameBoard(newBoard);
            return true;
        }
        else
            return false;
    }

    public boolean useBarrier(String coords)
    {
        String[] params = coords.split(",");

        if(params.length != 3)
            return false;

        GameBoard newBoard = node.getGameBoard().placeBarrier(Integer.parseInt(params[0]), 
            Integer.parseInt(params[1]), params[2].charAt(0));

        if(newBoard != null)
        {
            node.setGameBoard(newBoard);
            barriers--;
            return true;
        }
        else
        {
            System.out.println("Couldn't place a barrier there");
            return false;
        }
            
    }

    public int getDistanceFromBorder(char compColor)
    {
        PriorityQueue<PlayerNode> activeNodes = new PriorityQueue<PlayerNode>(); 
        ArrayList<PlayerNode> children = new ArrayList<PlayerNode>();
        ArrayList<String> visitedNodes = new ArrayList<String>();
        PlayerNode currentNode;
        boolean active = false, visited = false;

        Node.getSolution().clear();
        Node.getSolution().trimToSize();

        activeNodes.add(new PlayerNode(node, node.getPlayerPosition(compColor), compColor));

        while (!activeNodes.isEmpty()) 
        {
            currentNode = activeNodes.peek();

            if(Player.isWinner(currentNode.getColor(), currentNode.getPosition()))
            {
                currentNode.traceSolutionUp();
                return Node.getSolution().size();
            } 
                
            
            activeNodes.poll();
            visitedNodes.add(currentNode.getId());

            children = currentNode.expandPlayerNode();

            for (Node child : children) 
            {
                for (String id : visitedNodes)
                    if (id.equals(child.getId())) 
                    {
                        visited = true;
                        break;
                    }

                if (visited) 
                {
                    visited = false;
                    continue;
                }

                for (Node n : activeNodes)
                    if (n.getId().equals(child.getId())) {
                        active = true;
                        break;
                    }

                if (!active)
                    activeNodes.add((PlayerNode) child);

                active = false;
            }
        }

        children.clear();
        children.trimToSize();

        return -1;
    }

    public boolean isWinner()
    {
        return isWinner(this.color, this.position);
    }

    public static boolean isWinner(char playerColor, int[] playerPosition)
    {
        switch(playerColor)
        {
            case 'R':
                return playerPosition[0] == GameBoard.getBoardSize() - 1;

            case 'B':
                return playerPosition[0] == 0;

            case 'G':
                return playerPosition[1] == 0;

            case 'Y':
                return playerPosition[1] == GameBoard.getBoardSize() - 1;

            default:
                return false;
        }
    }

    public String getName()
    {
        switch(color)
        {
            case 'R':
                return "Red";
                
            case 'Y':
                return "Yellow";

            case 'B':
                return "Blue";

            case 'G':
                return "Green";

            default:
                return "Unknown";
        }
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    public int getBarriers()
    {
        return barriers;
    }

    public int[] getPosition()
    {
        return position;
    }

    public static int getMaxBarriers()
    {
        return MAX_BARRIERS;
    }

    public char getColor()
    {
        return color;
    }

    public static GameBoard getBoard()
    {
        return node.getGameBoard();
    }

    public static GameNode getNode()
    {
        return node;
    }

    public static void setMaxBarriers(int maxBarriers)
    {
        MAX_BARRIERS = maxBarriers;
    }

    public static void setNode(GameNode node)
    {
        Player.node = node;
    }

    public static void setBoard(GameBoard gameBoard)
    {
        node.setGameBoard(gameBoard);
    }

    // Algorithms: pseudo-code

    /*
    function minimax(node, depth, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, minimax(child, depth − 1, FALSE))
        return value
    else (* minimizing player *)
        value := +∞
        for each child of node do
            value := min(value, minimax(child, depth − 1, TRUE))
        return value
    */

    public int minimax(GameNode node, int depth, boolean maximazingPlayer) {
        if (depth == 0 || node.isTerminal()) {
            //return the heuristic value of node
        }

        if (maximazingPlayer) {
            //value = minInfinity
            ArrayList<Node> childNodes = node.expandNode();
            for (Node child : childNodes) {
                //value = max(value, minimax(child, depth − 1, false))
            }
            //return value;
        } else {
            //value = maxInfinity
            ArrayList<Node> childNodes = node.expandNode();
            for (Node child : childNodes) {
                //value = min(value, minimax(child, depth − 1, false))
            }  
            //return value;
        }
        return 1;
    }
}