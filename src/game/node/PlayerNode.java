package game.node;

import game.BlockIt;
import game.GameBoard;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class PlayerNode extends GameNode implements Comparable<PlayerNode>
{
    private int[] position;
    private char color;
    private int barriers;
    private int difficulty;
    private Double value;

    public PlayerNode(PlayerNode parentNode, int depth, int pathCost, String operator, GameBoard board,
        int difficulty, Double value, int[] position, char color, int barriers)
    {
        super(parentNode, depth, pathCost, operator, board);

        this.position = position;
        this.color = color;
        this.id = position[0] + "-" + position[1];
        this.value = value;
        this.difficulty = difficulty;
        this.barriers = barriers;
    }

    public PlayerNode(PlayerNode parentNode, String operator, GameBoard board, int[] position, char color, int barriers)
    {
        super(parentNode, operator, board);

        this.position = position;
        this.color = color;
        this.id = position[0] + "-" + position[1];
        this.difficulty = parentNode.getDifficulty();
    }

    public PlayerNode(String operator, GameBoard board, int[] position, char color, int barriers)
    {
        super(operator, board);

        this.position = position;
        this.color = color;
        this.barriers = barriers;
        this.id = position[0] + "-" + position[1];
    }

    public PlayerNode(PlayerNode father)
    {
        super(father);

        this.position = father.getPosition().clone();
        this.color = father.getColor();
        this.barriers = father.getBarriers();
        this.id = position[0] + "-" + position[1];
    }

    public ArrayList<PlayerNode> expandPlayerNode()
    {
        ArrayList<PlayerNode> nodeList = new ArrayList<PlayerNode>();
        GameBoard newBoard;
        char nextColor = color;
        int[] nextPosition = board.getPlayerPosition(nextColor);
        char[][] charBoard = board.getBoard();

        if((newBoard = this.board.moveDown(nextPosition)) != null)
            nodeList.add(new PlayerNode(this, "move down " + nextPosition[0] + " " 
                + nextPosition[1], newBoard, new int[] {nextPosition[0] + 2, nextPosition[1]}, nextColor, barriers));

        if((newBoard = this.board.moveUp(nextPosition)) != null)
            nodeList.add(new PlayerNode(this, "move up " + nextPosition[0] + " " 
                + nextPosition[1], newBoard, new int[] {nextPosition[0] - 2, nextPosition[1]}, nextColor, barriers));  

        if((newBoard = this.board.moveLeft(nextPosition)) != null)
            nodeList.add(new PlayerNode(this, "move left " + position[0] + " " 
                + nextPosition[1], newBoard, new int[] {nextPosition[0], nextPosition[1] - 2}, nextColor, barriers));    

        if((newBoard = this.board.moveRight(nextPosition)) != null)
            nodeList.add(new PlayerNode(this, "move right " + position[0] + " " 
                + nextPosition[1], newBoard, new int[] {nextPosition[0], nextPosition[1] + 2}, nextColor, barriers)); 

        if(barriers <= 0)
            return nodeList;

        for(int i = 0; i < charBoard.length; i++)
            for(int j = 0; j < charBoard[i].length; j++)
            if(charBoard[i][j] != '_' && charBoard[i][j] != 'X' && charBoard[i][j] != ' ')
                {
                    if(charBoard[i][j] != nextColor)
                    {
                        if ((newBoard = board.placeBarrier(j + 1, i - 1, 'v')) != null)
                            nodeList.add(new PlayerNode(this, "barrier v " + (i - 1) + " " 
                            + (j + 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j + 1, i + 1, 'v')) != null)
                            nodeList.add(new PlayerNode(this, "barrier v " + (i + 1) + " " 
                            + (j + 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j - 1, i - 1, 'v')) != null)
                            nodeList.add(new PlayerNode(this, "barrier v " + (i - 1) + " " 
                            + (j - 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j - 1, i + 1, 'v')) != null)
                            nodeList.add(new PlayerNode(this, "barrier v " + (i + 1) + " " 
                            + (j - 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j + 1, i - 1, 'h')) != null)
                            nodeList.add(new PlayerNode(this, "barrier h " + (i - 1) + " " 
                            + (j + 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j + 1, i + 1, 'h')) != null)
                            nodeList.add(new PlayerNode(this, "barrier h " + (i + 1) + " " 
                            + (j + 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j - 1, i - 1, 'h')) != null)
                            nodeList.add(new PlayerNode(this, "barrier h " + (i - 1) + " " 
                            + (j - 1), newBoard, nextPosition, nextColor, barriers - 1));

                        if ((newBoard = board.placeBarrier(j - 1, i + 1, 'h')) != null)
                            nodeList.add(new PlayerNode(this, "barrier h " + (i + 1) + " " 
                            + (j - 1), newBoard, nextPosition, nextColor, barriers - 1));
                    }
                }
            /*
            {
                if((newBoard = board.placeBarrier(j, i, 'v')) != null)
                    nodeList.add(new PlayerNode(this, "barrier v " + i + " " + j, newBoard, position, nextColor, 
                    barriers - 1));

                if((newBoard = board.placeBarrier(j, i, 'h')) != null)
                    nodeList.add(new PlayerNode(this, "barrier h " + i + " " + j, newBoard, position, nextColor, 
                    barriers - 1));
            }*/
            
                
                
        return nodeList;
    }

    /**
     * Determines the player bot's next move through the use of the minimax algorithm
     * @param node
     * @param depth
     * @param maximizingPlayer
     * @return
     */
    public String minimax(int depth, boolean maximizingPlayer) 
    {        
        ArrayList<PlayerNode> childNodes = expandPlayerNode();
        String op = "";
        Double val = null;

        for(PlayerNode child: childNodes)
        {
            System.out.print(child.getOperator() + " => ");

            child.color = BlockIt.getPlayerAfter(color).getColor();

            child.minimaxAux(depth, null, null, false);

            if(val == null || child.getValue() > val)
            {
                op = child.getOperator();
                val = child.getValue();
            }
                
        }

        return op;
    }

    /**
     * Determines the player bot's next move through the use of the minimax algorithm with alpha beta pruning
     * @param node
     * @param depth
     * @param alpha
     * @param beta
     * @param maximizingPlayer
     * @return
     */
    public void alphaBeta(int depth, PlayerNode alpha, PlayerNode beta, boolean maximizingPlayer) 
    {
        minimaxAux(depth, alpha, beta, maximizingPlayer);
    }

    /**
     * Determines the player bot's next move through the use of the minimax algorithm, with or without alpha beta pruning depending on the user input
     * @param node
     * @param depth
     * @param maximizingPlayer
     * @return
     */
    private void minimaxAux(int depth, PlayerNode alpha, PlayerNode beta, boolean maximizingPlayer) 
    {
        boolean isAlphaBeta = (alpha != null && beta != null);
        ArrayList<PlayerNode> childNodes;

        if (depth >= Node.MAX_SEARCH_DEPTH) 
        {
            if(depth % 2 == 0)
                this.color = BlockIt.getPlayerAfter(color).getColor();

            
            calculateHeuristic();
            return;    
        }

        childNodes = expandPlayerNode();

        //System.out.println("Children of " + father.getOperator());

        /*
        for(PlayerNode n: childNodes)
        {
            n.calculateHeuristic(playerColor);
            System.out.print(n.getOperator() + "-" + n.getHeuristic().getValue() + " " ); 
        } */
            
        //System.out.println("\n");

        for (PlayerNode child : childNodes) 
        {
            System.out.print(child.getOperator() + " => ");

            child.color = BlockIt.getPlayerAfter(color).getColor();

            if(maximizingPlayer)
            {
                child.minimaxAux(depth + 1, alpha, beta, false);

                if(this.value == null || child.getValue() >= value)
                {
                    this.value = child.getValue();
                    //this.operator = child.getOperator();
                }
            
                /*
                if (isAlphaBeta) 
                {
                    if(PlayerNode.max(alpha, value) == 1)
                        alpha = value;

                    if (alpha.ge(beta)) 
                        break;
                } */
            }
            else
            {
                child.minimaxAux(depth + 1, alpha, beta, true);

                if(this.value == null || child.getValue() <= value)
                {
                    this.value = child.getValue();
                    //this.operator = child.getOperator();
                }

                /*
                if(isAlphaBeta) 
                {
                    if(PlayerNode.min(beta, value) == 1)
                        beta = value;

                    if (beta.ge(alpha)) //Changed from alpha.ge(beta)
                        break;
                } */
            }
            
        }
    }

    public boolean isWinner()
    {
        return isWinner(this.color, this.position);
    }

    public void useBarrier()
    {
        barriers--;
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

    public int[] getPosition()
    {
        return position;
    }

    public char getColor()
    {
        return color;
    }

    public int getBarriers()
    {
        return barriers;
    }

    public void setPosition(int[] newPosition)
    {
        position = newPosition.clone();
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    public double getValue()
    {
        return value;
    }

    @Override
    public int compareTo(PlayerNode pn) 
    {
        int heurComp, depthComp, comp;

        if(pn.getColor() == color)
        {
            switch(color)
            {
                case 'R':
                    heurComp = position[0] - pn.getPosition()[0];
                    break;
        
                case 'B':
                    heurComp = (GameBoard.getBoardSize() - 1 - position[0]) 
                        - (GameBoard.getBoardSize() - 1 - pn.getPosition()[0]);
                    break;
    
                case 'G':
                    heurComp = (GameBoard.getBoardSize() - 1 - position[1]) 
                        - (GameBoard.getBoardSize() - 1 - pn.getPosition()[1]);
                    break;
    
                case 'Y':
                    heurComp = position[1] - pn.getPosition()[1];
                    break;
    
                default:
                    heurComp = 0;
            }
        }
        else
            heurComp = 0;

        depthComp = depth - pn.getDepth();

        comp = heurComp + depthComp;

        if(comp < 0)
            return -1;
        else if(comp > 0)
            return 1;
        return 0;
    }

    /**
     * Determines the greater of two nodes, in regards to the heuristic value
     * @param n
     * @param m
     * @return
     */

    public static int max(PlayerNode n, PlayerNode m) {
        if (n == null) {
            return 1;
        } 

        if (m == null) {
            return 0;
        }

        return (n.getValue() > m.getValue() ? 0 : 1);
    }

    /**
     * 
     * @param n
     * @param m
     * @return
     */

    public static int min(PlayerNode n, PlayerNode m) {
        if (n == null) {
            return 1;
        } 

        if (m == null) {
            return 0;
        }

        return (n.getValue() < m.getValue() ? 0 : 1);
    }


    /**
     * Checks if the heuristic value of node a is greater than or equal to that of node b
     * @param a
     * @param b
     * @return
     */

    public boolean ge(PlayerNode b) {
        return this.getValue() >= b.getValue();
    }

    public void calculateHeuristic() 
    {
        switch(difficulty)
        {
            case 2:
                //return new DirectHeuristic();

            case 3:
                competitiveHeuristic();
                break;
            case 4:
                shortestPathHeuristic();
                break;

            default:
                System.out.println("Unknown difficulty: " + difficulty);
                value = 0.0;
        }
    }

    public void shortestPathHeuristic()
    {
        double currentPlayerValue = AStar(color), 
            adversaryValue = AStar(BlockIt.getPlayerAfter(color).getColor());

        value = (GameBoard.getPlayBoardSize() - currentPlayerValue) - (GameBoard.getPlayBoardSize() - adversaryValue);

        System.out.println("Value of " + color + ": " + value);
    }

    public double directHeuristic()
    {
        return 1;
    }

    public void competitiveHeuristic()
    {
        Integer r = 0, g = 0, b = 0, y = 0;

        System.out.println("\n\n" + board.getPlayers()[0][0] + " " + board.getPlayers()[0][1] + "\n\n");

        if(board.getPlayers()[0][0] != -1)
            r = GameBoard.getBoardSize() - board.getPlayers()[0][0];

        if(board.getPlayers()[1][0] != -1)
            g = board.getPlayers()[1][1];

        if(board.getPlayers()[2][0] != -1)
            b = board.getPlayers()[2][0];

        if(board.getPlayers()[3][0] != -1)
            y = GameBoard.getBoardSize() - board.getPlayers()[3][1];

        char other = BlockIt.getPlayerAfter(color).getColor();
        int mine, his;

        mine = getVal(color, r, g, b, y);
        his = getVal(other, r, g, b, y);

        value = (double) (GameBoard.getBoardSize() - mine) - (GameBoard.getBoardSize() - his);
    }

    private int getVal(char color, Integer r, Integer g, Integer b, Integer y) {
        int val = 0;
        switch (color) {
            case 'R':
                val = r;
                break;
            case 'G':
                val = g;
                break;
            case 'B':
                val = b;
                break;
            case 'Y':
                val = y;
                break;
        }
        return val;
    }

    boolean win(char color, Integer r, Integer g, Integer b, Integer y){
        switch(color) {
            case 'R':
                return r == 0;
            case 'G':
                return g == 0;
            case 'B':
                return b == 0;
            case 'Y':
                return y == 0;
        }

        return false;
    }

    public double AStar(char playerColor)
    {
        PriorityQueue<PlayerNode> activeNodes = new PriorityQueue<PlayerNode>();
        ArrayList<PlayerNode> children = new ArrayList<PlayerNode>();
        ArrayList<String> visitedNodes = new ArrayList<String>();
        PlayerNode currentNode;
        boolean active = false, visited = false, foundSolution;
        int[] playerPos = board.getPlayerPosition(playerColor);

        Node.getSolution().clear();
        Node.getSolution().trimToSize();
        
        foundSolution = false;

        //barrier no doesn't matter here
        activeNodes.add(new PlayerNode(null, 0, 1, "root", board, difficulty, null, playerPos, playerColor, barriers));

        while (!activeNodes.isEmpty())
        {
            currentNode = activeNodes.peek();

            if(currentNode.isWinner())
            {
                currentNode.traceSolutionUp();
                foundSolution = true;
                break;
            }

            activeNodes.poll();
            visitedNodes.add(currentNode.getId());

            children = currentNode.expandPlayerNode();

            for (PlayerNode child : children)
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

                for (PlayerNode n : activeNodes)
                    if (n.getId().equals(child.getId())) {
                        active = true;
                        break;
                    }

                if (!active)
                    activeNodes.add((PlayerNode) child);

                active = false;
            }
        }

        if(foundSolution)
            return Node.getSolution().size();
        else
            return Integer.MAX_VALUE;
    }
}