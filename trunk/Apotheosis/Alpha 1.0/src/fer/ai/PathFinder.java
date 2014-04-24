package fer.ai;

import fer.Game;
import fer.Map;
import fer.Tile;
import fer.Unit;
import fer.UnitClass;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author Evan Stewart
 * 
 * A utility class that contains various methods which return sets of tiles
 * found by running various pathfinding and search algorithms with variable
 * constraints as arguments.  Returned collections often include either an 
 * ordered path of tiles or a set of all tiles reachable based on specified
 * limitations (e.g. movable tiles, attackable tiles).
 */
public class PathFinder {
    
    private boolean unitCollision = true;
    private int excludeX = -1;
    private int excludeY = -1;
    
    private class TreeNode {
        
        private TreeNode parent;
        private Tile tile;
        private ArrayList<TreeNode> children;
        
        public TreeNode(Tile tile) {
            this.tile = tile;
            children = new ArrayList();
            parent = null;
        }
        
        public TreeNode(Tile tile, TreeNode parent) {
            this(tile);
            this.parent = parent;
        }
        
        public TreeNode getParent() {
            return parent;
        }
        
        public Tile getTile() {
            return tile;
        }
        
        public ArrayList<TreeNode> getChildren() {
            return children;
        }
        
        public TreeNode getChild(int index) {
            return children.get(index);
        }
        
        public void addChild(TreeNode child) {
            children.add(child);
        }
        
        public void setParent(TreeNode parent) {
            this.parent = parent;
        }
    }
    
    public ArrayList<Tile> getShortestPathAStar(Map map, Unit unit, Tile start, 
            Tile target) {
        final Unit fUnit = unit;
        final Tile fTarget = target;
        Comparator fComp = new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {
                        Tile t1 = ((TreeNode)o1).getTile();
                        Tile t2 = ((TreeNode)o2).getTile();
                        if (getCost(fUnit, t1, fTarget) < getCost(fUnit, t2, fTarget)) {
                            return -1;
                        } else if (getCost(fUnit, t1, fTarget) == getCost(fUnit, t2, fTarget)) {
                            return 0; 
                        } else {
                            return 1;
                        }
                    }
                };
        ArrayList<Tile> exploredTiles = new ArrayList();
        ArrayList<TreeNode> pathTree = new ArrayList();
        PriorityQueue<TreeNode> frontier = new PriorityQueue(map.getNumTiles(), fComp);
        ArrayList<Tile> shortestPath = new ArrayList();
        
        
        frontier.add(new TreeNode(start));
        
        TreeNode lastNode = null;
        while (!frontier.isEmpty()) {
            TreeNode nextClosest = frontier.poll();
            
            if (nextClosest.getParent() != null) {
                pathTree.add(nextClosest);
                //lastNode.addChild(node);
                nextClosest.getParent().addChild(nextClosest);
            } else {
                pathTree.add(nextClosest);
            }
            
            if (nextClosest.getTile().equals(target)) {
                break;
            }
            
            if ((nextClosest.getTile().getMapX() + 1) < map.getWidth() && 
                    !exploredTiles.contains(map.getTile((nextClosest.getTile().getMapX() 
                    + 1) + (nextClosest.getTile().getMapY()) * map.getWidth()))) {
                frontier.add(new TreeNode((map.getTile((nextClosest.getTile().getMapX() + 1) + 
                        (nextClosest.getTile().getMapY()) * map.getWidth())), nextClosest));
            }
            if ((nextClosest.getTile().getMapX() - 1) >= 0 && 
                    !exploredTiles.contains(map.getTile((nextClosest.getTile().getMapX() 
                    - 1) + (nextClosest.getTile().getMapY()) * map.getWidth()))) {
                frontier.add(new TreeNode((map.getTile((nextClosest.getTile().getMapX() - 1) + 
                        (nextClosest.getTile().getMapY()) * map.getWidth())), nextClosest));
            }
            if ((nextClosest.getTile().getMapY() + 1) < map.getHeight() && 
                    !exploredTiles.contains(map.getTile((nextClosest.getTile().getMapX()) 
                    + (nextClosest.getTile().getMapY() + 1) * map.getWidth()))) {
                frontier.add(new TreeNode((map.getTile((nextClosest.getTile().getMapX()) + 
                        (nextClosest.getTile().getMapY() + 1) * map.getWidth())), nextClosest));
            }
            if ((nextClosest.getTile().getMapY() - 1) >= 0 && 
                    !exploredTiles.contains(map.getTile((nextClosest.getTile().getMapX()) 
                    + (nextClosest.getTile().getMapY() - 1) * map.getWidth()))) {
                frontier.add(new TreeNode((map.getTile((nextClosest.getTile().getMapX()) + 
                        (nextClosest.getTile().getMapY() - 1) * map.getWidth())), nextClosest));
            }
            
            exploredTiles.add(nextClosest.getTile());
            
            TreeNode tempNode = lastNode;
            lastNode = new TreeNode(nextClosest.getTile(), tempNode);
        }
        
        TreeNode currentNode = pathTree.get(pathTree.size() - 1);
        while (currentNode != null) {
            shortestPath.add(currentNode.getTile());
            currentNode = currentNode.getParent();
        }
        
        return shortestPath;
    }
    
    public ArrayList<Tile> getMovableTiles(Unit unit, Map map) {
        final Unit fUnit = unit;
        Comparator cComp = new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {
                        TreeNode n1 = (TreeNode)o1;
                        TreeNode n2 = (TreeNode)o2;
                        Tile t1 = n1.getTile();
                        Tile t2 = n2.getTile();
                        if (!t1.isAttackable() && t2.isAttackable()) {
                            return -1;
                        } else if (t1.isAttackable() == t2.isAttackable()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                };
        PriorityQueue<TreeNode> frontier = new PriorityQueue(map.getNumTiles(), cComp);
        ArrayList<TreeNode> pathTree = new ArrayList();
        ArrayList<Tile> moveableTiles = new ArrayList();
        ArrayList<Tile> exploredTiles = new ArrayList();
        
        frontier.add(new TreeNode(map.getTile(unit.getMapx() + unit.getMapy() * map.getWidth())));
        
        TreeNode lastNode = null;
        while(!frontier.isEmpty()) {
            TreeNode node = frontier.poll();
            Tile nextClosest = node.getTile();
            exploredTiles.add(nextClosest);
            
            pathTree.add(node);
            
            int pathCost = 0;
            TreeNode currentNode = node;
            while(currentNode.getParent() != null) {
                pathCost += getMovementCost(unit, currentNode.getTile());
                currentNode = currentNode.getParent();
            }
            
            if (pathCost <= unit.getMov()) {
                //Even if tile is alrady explored, add it if it is reachable
                //through a different route.
                if (!moveableTiles.contains(nextClosest)) {
                    moveableTiles.add(nextClosest);
                }
                if ((nextClosest.getMapX() + 1) < map.getWidth() /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX() 
                    + 1) + (nextClosest.getMapY()) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX() + 1) + 
                        (nextClosest.getMapY()) * map.getWidth()), node));
                }
                if ((nextClosest.getMapX() - 1) >= 0 /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX() 
                    - 1) + (nextClosest.getMapY()) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX() - 1) + 
                        (nextClosest.getMapY()) * map.getWidth()), node));
                }
                if ((nextClosest.getMapY() + 1) < map.getHeight() /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX()) 
                    + (nextClosest.getMapY() + 1) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX()) + 
                        (nextClosest.getMapY() + 1) * map.getWidth()), node));
                }
                if ((nextClosest.getMapY() - 1) >= 0 /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX()) 
                    + (nextClosest.getMapY() - 1) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX()) + 
                        (nextClosest.getMapY() - 1) * map.getWidth()), node));
                }
                
                exploredTiles.add(nextClosest);
                
                TreeNode tempNode = lastNode;
                lastNode = new TreeNode(nextClosest, tempNode);
            } 
        }
        
        return moveableTiles;
    }
    
    public ArrayList<Tile> getAttackableTiles(Unit unit, int x, int y, Map map, int weapon) {
        final Unit fUnit = unit;
        Comparator cComp = new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {
                        TreeNode n1 = (TreeNode)o1;
                        TreeNode n2 = (TreeNode)o2;
                        Tile t1 = n1.getTile();
                        Tile t2 = n2.getTile();
                        if (getMovementCost(fUnit, t1) < getMovementCost(fUnit, t2)) {
                            return -1;
                        } else if (getMovementCost(fUnit, t1) == getMovementCost(fUnit, t2)) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                };
        PriorityQueue<TreeNode> frontier = new PriorityQueue(map.getNumTiles(), cComp);
        ArrayList<TreeNode> pathTree = new ArrayList();
        ArrayList<Tile> attackableTiles = new ArrayList();
        ArrayList<Tile> exploredTiles = new ArrayList();
        
        frontier.add(new TreeNode(map.getTile(x + y * map.getWidth())));
        
        TreeNode lastNode = null;
        while(!frontier.isEmpty()) {
            TreeNode node = frontier.poll();
            Tile nextClosest = node.getTile();
            exploredTiles.add(nextClosest);
            
            pathTree.add(node);
            
            int pathCost = 0;
            TreeNode currentNode = node;
            while(currentNode.getParent() != null) {
                pathCost += 1;
                currentNode = currentNode.getParent();
            }
            
            if (pathCost <= unit.getWeapon(weapon).getRange()) {
                //Even if tile is alrady explored, add it if it is reachable
                //through a different route.
                if (!attackableTiles.contains(nextClosest)) {
                    attackableTiles.add(nextClosest);
                }
                if ((nextClosest.getMapX() + 1) < map.getWidth() /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX() 
                    + 1) + (nextClosest.getMapY()) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX() + 1) + 
                        (nextClosest.getMapY()) * map.getWidth()), node));
                }
                if ((nextClosest.getMapX() - 1) >= 0 /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX() 
                    - 1) + (nextClosest.getMapY()) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX() - 1) + 
                        (nextClosest.getMapY()) * map.getWidth()), node));
                }
                if ((nextClosest.getMapY() + 1) < map.getHeight() /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX()) 
                    + (nextClosest.getMapY() + 1) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX()) + 
                        (nextClosest.getMapY() + 1) * map.getWidth()), node));
                }
                if ((nextClosest.getMapY() - 1) >= 0 /*&& 
                    !exploredTiles.contains(map.getTile((nextClosest.getMapX()) 
                    + (nextClosest.getMapY() - 1) * map.getWidth()))*/) {
                    frontier.add(new TreeNode(map.getTile((nextClosest.getMapX()) + 
                        (nextClosest.getMapY() - 1) * map.getWidth()), node));
                }
                
                exploredTiles.add(nextClosest);
                
                TreeNode tempNode = lastNode;
                lastNode = new TreeNode(nextClosest, tempNode);
            } 
        }
        
        return attackableTiles;
    }
    
    public int getManhattanHeuristic(Tile location, Tile target) {
        return (Math.abs(location.getMapX() - target.getMapX()) + 
                Math.abs(location.getMapY() - target.getMapY()));
    }
    
    public int getMovementCost(Unit unit, Tile target) {
        if (Game.getCurrentMap().getUnitTile(target.getMapX() + target.getMapY() * Game.getCurrentMap().getWidth()) != null) {
            if (Game.getCurrentMap().getUnitTile(target.getMapX() + target.getMapY() * Game.getCurrentMap().getWidth()).getFaction() != unit.getFaction() && unitCollision && target.getMapX() != excludeX && target.getMapY() != excludeY) {
                return Tile.IMPASSIBLE;
            }
        }
        return unit.getUnitClass().getMoveCost(target.getTerrain());
    }
    
    public int getCost(Unit unit, Tile location, Tile target) {
        return getManhattanHeuristic(location, target) + 
               getMovementCost(unit, location);
    }
    
    public void setUnitCollision(boolean unitCollision) {
        this.unitCollision = unitCollision;
    }
    
    public void setExcludeCollision(int x, int y) {
        excludeX = x;
        excludeY = y;
    }
}
