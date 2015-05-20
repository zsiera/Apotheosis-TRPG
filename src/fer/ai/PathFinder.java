/*
 * 
 */
package fer.ai;

import fer.Map;
import fer.Tile;
import fer.Unit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class PathFinder.
 *
 * @author Evan Stewart
 * 
 *         A utility class that contains various methods which return sets of
 *         tiles found by running various pathfinding and search algorithms with
 *         variable constraints as arguments. Returned collections often include
 *         either an ordered path of tiles or a set of all tiles reachable based
 *         on specified limitations (e.g. movable tiles, attackable tiles).
 */
public class PathFinder {

	/** The exclusions. */
	private Exclusions exclusions = new Exclusions();
	
	/** The unit collision. */
	private boolean unitCollision = true;

	/**
	 * The Class TreeNode.
	 */
	private class TreeNode {

		/** The parent. */
		private TreeNode parent;
		
		/** The tile. */
		private Tile tile;
		
		/** The children. */
		private ArrayList<TreeNode> children;

		/**
		 * Instantiates a new tree node.
		 *
		 * @param tile the tile
		 */
		public TreeNode(final Tile tile) {
			this.tile = tile;
			children = new ArrayList<>();
			parent = null;
		}

		/**
		 * Instantiates a new tree node.
		 *
		 * @param tile the tile
		 * @param parent the parent
		 */
		public TreeNode(final Tile tile, final TreeNode parent) {
			this(tile);
			this.parent = parent;
		}

		/**
		 * Gets the parent.
		 *
		 * @return the parent
		 */
		public TreeNode getParent() {
			return parent;
		}

		/**
		 * Gets the tile.
		 *
		 * @return the tile
		 */
		public Tile getTile() {
			return tile;
		}

		/**
		 * Gets the children.
		 *
		 * @return the children
		 */
		public ArrayList<TreeNode> getChildren() {
			return children;
		}

		/**
		 * Gets the child.
		 *
		 * @param index the index
		 * @return the child
		 */
		public TreeNode getChild(final int index) {
			return children.get(index);
		}

		/**
		 * Adds the child.
		 *
		 * @param child the child
		 */
		public void addChild(final TreeNode child) {
			children.add(child);
		}

		/**
		 * Sets the parent.
		 *
		 * @param parent the new parent
		 */
		public void setParent(final TreeNode parent) {
			this.parent = parent;
		}
	}

	/**
	 * Gets the shortest path a star.
	 *
	 * @param map the map
	 * @param unit the unit
	 * @param start the start
	 * @param target the target
	 * @return the shortest path a star
	 */
	public final ArrayList<Tile> getShortestPathAStar(final Map map, final Unit unit, final Tile start,
			final Tile target) {
		final Unit fUnit = unit;
		final Tile fTarget = target;
		Comparator fComp = new Comparator() {

			@Override
			public int compare(final Object o1, final Object o2) {
				Tile t1 = ((TreeNode) o1).getTile();
				Tile t2 = ((TreeNode) o2).getTile();
				if (t1.getCost(fUnit, fTarget, unitCollision, exclusions) < t2
						.getCost(fUnit, fTarget, unitCollision, exclusions)) {
					return -1;
				} else
					return t1
							.getCost(fUnit, fTarget, unitCollision, exclusions) == t2
							.getCost(fUnit, fTarget, unitCollision, exclusions) ? 0 : 1;
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

	/**
	 * Adds the frontier.
	 *
	 * @param map the map
	 * @param exploredTiles the explored tiles
	 * @param frontier the frontier
	 * @param nextClosest the next closest
	 */
	private void addFrontier(final Map map, final ArrayList<Tile> exploredTiles,
			final PriorityQueue<TreeNode> frontier, final TreeNode nextClosest) {
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

	/**
	 * Gets the movable tiles.
	 *
	 * @param unit the unit
	 * @param map the map
	 * @return the movable tiles
	 */
	public final ArrayList<Tile> getMovableTiles(final Unit unit, final Map map) {
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

	/**
	 * Adds the frontier.
	 *
	 * @param map the map
	 * @param frontier the frontier
	 * @param node the node
	 * @param nextClosest the next closest
	 */
	private void addFrontier(final Map map, final PriorityQueue<TreeNode> frontier,
			final TreeNode node, final Tile nextClosest) {
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

	/**
	 * Gets the movable tiles frontier.
	 *
	 * @param map the map
	 * @return the movable tiles frontier
	 */
	private PriorityQueue<TreeNode> getMovableTilesFrontier(final Map map) {
		Comparator cComp = new Comparator() {

			@Override
			public int compare(final Object o1, final Object o2) {
				TreeNode n1 = (TreeNode) o1;
				TreeNode n2 = (TreeNode) o2;
				Tile t1 = n1.getTile();
				Tile t2 = n2.getTile();
				if (!t1.isAttackable() && t2.isAttackable()) {
					return -1;
				} else
					return t1.isAttackable() == t2.isAttackable() ? 0 : 1;
			}
		};
		return new PriorityQueue(map.getNumTiles(),
				cComp);
	}

	/**
	 * Gets the path cost.
	 *
	 * @param unit the unit
	 * @param node the node
	 * @return the path cost
	 */
	private int getPathCost(final Unit unit, final TreeNode node) {
		int pathCost = 0;
		TreeNode currentNode = node;
		while (currentNode.getParent() != null) {
			pathCost += currentNode.getTile().getMovementCost(unit,
					unitCollision, exclusions);
			currentNode = currentNode.getParent();
		}
		return pathCost;
	}

	/**
	 * Gets the attackable tiles.
	 *
	 * @param unit the unit
	 * @param x the x
	 * @param y the y
	 * @param map the map
	 * @param weapon the weapon
	 * @return the attackable tiles
	 */
	public final ArrayList<Tile> getAttackableTiles(final Unit unit, final int x, final int y, final Map map,
			final int weapon) {
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

	/**
	 * Gets the attackable tiles frontier.
	 *
	 * @param unit the unit
	 * @param map the map
	 * @return the attackable tiles frontier
	 */
	private PriorityQueue<TreeNode> getAttackableTilesFrontier(final Unit unit,
			final Map map) {
		final Unit fUnit = unit;
		Comparator cComp = new Comparator() {

			@Override
			public int compare(final Object o1, final Object o2) {
				TreeNode n1 = (TreeNode) o1;
				TreeNode n2 = (TreeNode) o2;
				Tile t1 = n1.getTile();
				Tile t2 = n2.getTile();
				if (t1.getMovementCost(fUnit, unitCollision, exclusions) < t2
						.getMovementCost(fUnit, unitCollision, exclusions)) {
					return -1;
				} else
					return t1.getMovementCost(fUnit, unitCollision, exclusions) == t2
							.getMovementCost(fUnit, unitCollision, exclusions) ? 0 : 1;
			}
		};
		return new PriorityQueue(map.getNumTiles(), cComp);
	}

	/**
	 * Sets the unit collision.
	 *
	 * @param unitCollision the new unit collision
	 */
	public final void setUnitCollision(final boolean unitCollision) {
		this.unitCollision = unitCollision;
	}

	/**
	 * Sets the exclude collision.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public final void setExcludeCollision(final int x, final int y) {
		exclusions.setExcludeCollision(x, y);
	}
}
