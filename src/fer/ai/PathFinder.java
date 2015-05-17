package fer.ai;

import fer.Map;
import fer.Tile;
import fer.Unit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Evan Stewart
 * 
 *         A utility class that contains various methods which return sets of
 *         tiles found by running various pathfinding and search algorithms with
 *         variable constraints as arguments. Returned collections often include
 *         either an ordered path of tiles or a set of all tiles reachable based
 *         on specified limitations (e.g. movable tiles, attackable tiles).
 */
public class PathFinder {

	private Exclusions exclusions = new Exclusions();
	private boolean unitCollision = true;

	private class TreeNode {

		private TreeNode parent;
		private Tile tile;
		private ArrayList<TreeNode> children;

		public TreeNode(Tile tile) {
			this.tile = tile;
			children = new ArrayList<>();
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
				Tile t1 = ((TreeNode) o1).getTile();
				Tile t2 = ((TreeNode) o2).getTile();
				if (t1.getCost(fUnit, fTarget, unitCollision, exclusions) < t2
						.getCost(fUnit, fTarget, unitCollision, exclusions)) {
					return -1;
				} else if (t1
						.getCost(fUnit, fTarget, unitCollision, exclusions) == t2
						.getCost(fUnit, fTarget, unitCollision, exclusions)) {
					return 0;
				} else {
					return 1;
				}
			}
		};
		ArrayList<Tile> exploredTiles = new ArrayList<>();
		ArrayList<TreeNode> pathTree = new ArrayList<>();
		PriorityQueue<TreeNode> frontier = new PriorityQueue(map.getNumTiles(),
				fComp);
		ArrayList<Tile> shortestPath = new ArrayList<>();

		frontier.add(new TreeNode(start));

		TreeNode lastNode = null;
		while (!frontier.isEmpty()) {
			TreeNode nextClosest = frontier.poll();

			pathTree.add(nextClosest);
			if (nextClosest.getParent() != null) {
				// lastNode.addChild(node);
				nextClosest.getParent().addChild(nextClosest);
			}

			if (nextClosest.getTile().equals(target)) {
				break;
			}

			addFrontier(map, exploredTiles, frontier, nextClosest);

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

	private void addFrontier(Map map, ArrayList<Tile> exploredTiles,
			PriorityQueue<TreeNode> frontier, TreeNode nextClosest) {
		if (nextClosest.getTile().getMapX() + 1 < map.getWidth()
				&& !exploredTiles.contains(map.getTile(nextClosest.getTile()
						.getMapX() + 1
						+ nextClosest.getTile().getMapY()
						* map.getWidth()))) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getTile()
					.getMapX() + 1
					+ nextClosest.getTile().getMapY()
					* map.getWidth()), nextClosest));
		}
		if (nextClosest.getTile().getMapX() - 1 >= 0
				&& !exploredTiles.contains(map.getTile((nextClosest.getTile()
						.getMapX() - 1)
						+ nextClosest.getTile().getMapY()
						* map.getWidth()))) {
			frontier.add(new TreeNode(map.getTile((nextClosest.getTile()
					.getMapX() - 1)
					+ nextClosest.getTile().getMapY()
					* map.getWidth()), nextClosest));
		}
		if (nextClosest.getTile().getMapY() + 1 < map.getHeight()
				&& !exploredTiles.contains(map.getTile(nextClosest.getTile()
						.getMapX()
						+ (nextClosest.getTile().getMapY() + 1)
						* map.getWidth()))) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getTile()
					.getMapX()
					+ (nextClosest.getTile().getMapY() + 1)
					* map.getWidth()), nextClosest));
		}
		if (nextClosest.getTile().getMapY() - 1 >= 0
				&& !exploredTiles.contains(map.getTile(nextClosest.getTile()
						.getMapX()
						+ (nextClosest.getTile().getMapY() - 1)
						* map.getWidth()))) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getTile()
					.getMapX()
					+ (nextClosest.getTile().getMapY() - 1)
					* map.getWidth()), nextClosest));
		}
	}

	public ArrayList<Tile> getMovableTiles(Unit unit, Map map) {
		final Unit fUnit = unit;
		PriorityQueue<TreeNode> frontier = getMovableTilesFrontier(map);
		ArrayList<TreeNode> pathTree = new ArrayList<>();
		ArrayList<Tile> moveableTiles = new ArrayList<>();
		ArrayList<Tile> exploredTiles = new ArrayList<>();

		frontier.add(new TreeNode(map.getTile(unit.getMapx() + unit.getMapy()
				* map.getWidth())));

		TreeNode lastNode = null;
		while (!frontier.isEmpty()) {
			TreeNode node = frontier.poll();
			Tile nextClosest = node.getTile();
			exploredTiles.add(nextClosest);

			pathTree.add(node);

			int pathCost = getPathCost(unit, node);

			if (pathCost <= unit.getMov()) {
				// Even if tile is alrady explored, add it if it is reachable
				// through a different route.
				if (!moveableTiles.contains(nextClosest)) {
					moveableTiles.add(nextClosest);
				}
				addFrontier(map, frontier, node, nextClosest);

				exploredTiles.add(nextClosest);

				TreeNode tempNode = lastNode;
				lastNode = new TreeNode(nextClosest, tempNode);
			}
		}

		return moveableTiles;
	}

	private void addFrontier(Map map, PriorityQueue<TreeNode> frontier,
			TreeNode node, Tile nextClosest) {
		if (nextClosest.getMapX() + 1 < map.getWidth() /*
														 * &&
														 * !exploredTiles.contains
														 * (
														 * map.getTile((nextClosest
														 * .getMapX() + 1) +
														 * (nextClosest
														 * .getMapY()) *
														 * map.getWidth()))
														 */) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getMapX() + 1
					+ nextClosest.getMapY() * map.getWidth()), node));
		}
		if (nextClosest.getMapX() - 1 >= 0 /*
											 * &&
											 * !exploredTiles.contains(map.getTile
											 * ((nextClosest.getMapX() - 1) +
											 * (nextClosest.getMapY()) *
											 * map.getWidth()))
											 */) {
			frontier.add(new TreeNode(map.getTile((nextClosest.getMapX() - 1)
					+ nextClosest.getMapY() * map.getWidth()), node));
		}
		if (nextClosest.getMapY() + 1 < map.getHeight() /*
														 * &&
														 * !exploredTiles.contains
														 * (
														 * map.getTile((nextClosest
														 * .getMapX()) +
														 * (nextClosest
														 * .getMapY() + 1) *
														 * map.getWidth()))
														 */) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getMapX()
					+ (nextClosest.getMapY() + 1) * map.getWidth()), node));
		}
		if (nextClosest.getMapY() - 1 >= 0 /*
											 * &&
											 * !exploredTiles.contains(map.getTile
											 * ((nextClosest.getMapX()) +
											 * (nextClosest.getMapY() - 1) *
											 * map.getWidth()))
											 */) {
			frontier.add(new TreeNode(map.getTile(nextClosest.getMapX()
					+ (nextClosest.getMapY() - 1) * map.getWidth()), node));
		}
	}

	private PriorityQueue<TreeNode> getMovableTilesFrontier(Map map) {
		Comparator cComp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				TreeNode n1 = (TreeNode) o1;
				TreeNode n2 = (TreeNode) o2;
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
		return new PriorityQueue(map.getNumTiles(),
				cComp);
	}

	private int getPathCost(Unit unit, TreeNode node) {
		int pathCost = 0;
		TreeNode currentNode = node;
		while (currentNode.getParent() != null) {
			pathCost += currentNode.getTile().getMovementCost(unit,
					unitCollision, exclusions);
			currentNode = currentNode.getParent();
		}
		return pathCost;
	}

	public ArrayList<Tile> getAttackableTiles(Unit unit, int x, int y, Map map,
			int weapon) {
		PriorityQueue<TreeNode> frontier = getAttackableTilesFrontier(unit, map);
		ArrayList<TreeNode> pathTree = new ArrayList<>();
		ArrayList<Tile> attackableTiles = new ArrayList<>();
		ArrayList<Tile> exploredTiles = new ArrayList<>();

		frontier.add(new TreeNode(map.getTile(x + y * map.getWidth())));

		TreeNode lastNode = null;
		while (!frontier.isEmpty()) {
			TreeNode node = frontier.poll();
			Tile nextClosest = node.getTile();
			exploredTiles.add(nextClosest);

			pathTree.add(node);

			int pathCost = 0;
			TreeNode currentNode = node;
			while (currentNode.getParent() != null) {
				pathCost += 1;
				currentNode = currentNode.getParent();
			}

			if (pathCost <= unit.getWeapon(weapon).getRange()) {
				// Even if tile is alrady explored, add it if it is reachable
				// through a different route.
				if (!attackableTiles.contains(nextClosest)) {
					attackableTiles.add(nextClosest);
				}
				addFrontier(map, frontier, node, nextClosest);

				exploredTiles.add(nextClosest);

				TreeNode tempNode = lastNode;
				lastNode = new TreeNode(nextClosest, tempNode);
			}
		}

		return attackableTiles;
	}

	private PriorityQueue<TreeNode> getAttackableTilesFrontier(Unit unit,
			Map map) {
		final Unit fUnit = unit;
		Comparator cComp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				TreeNode n1 = (TreeNode) o1;
				TreeNode n2 = (TreeNode) o2;
				Tile t1 = n1.getTile();
				Tile t2 = n2.getTile();
				if (t1.getMovementCost(fUnit, unitCollision, exclusions) < t2
						.getMovementCost(fUnit, unitCollision, exclusions)) {
					return -1;
				} else if (t1.getMovementCost(fUnit, unitCollision, exclusions) == t2
						.getMovementCost(fUnit, unitCollision, exclusions)) {
					return 0;
				} else {
					return 1;
				}
			}
		};
		return new PriorityQueue(map.getNumTiles(), cComp);
	}

	public void setUnitCollision(boolean unitCollision) {
		this.unitCollision = unitCollision;
	}

	public void setExcludeCollision(int x, int y) {
		exclusions.setExcludeCollision(x, y);
	}
}
