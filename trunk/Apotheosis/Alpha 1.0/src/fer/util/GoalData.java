package fer.util;

/**
 * @author Evan Stewart
 */
public class GoalData {
    
    private int index;
    private String type;
    private int[] targets;
    private int turns, mapx, mapy;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getTargets() {
        return targets;
    }

    public void setTargets(int[] targets) {
        this.targets = targets;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getMapx() {
        return mapx;
    }

    public void setMapx(int mapx) {
        this.mapx = mapx;
    }

    public int getMapy() {
        return mapy;
    }

    public void setMapy(int mapy) {
        this.mapy = mapy;
    }
}
