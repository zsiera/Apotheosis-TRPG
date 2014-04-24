package fer.ai;

/**
 * @author Evan Stewart
 * A struct class that stores the parameters surrounding a potential action for
 * and AI player to take, the the unit to which it is assigned, and its 
 * priority, which is evaluated based on its type.
 */
public class AITask {
    
    public enum TaskType {
        ATTACK_UNIT, GO_TO_TILE
    }
    
    private TaskType type;
    private int priority;
    private int mapx, mapy, assignedIndex = -1, targetIndex = -1;
    
    /**
     * Creates a new AITask of type AITask.TaskType.GO_TO_TILE
     * @param mapx The x-coordinate of the destination tile
     * @param mapy The y-coordinate of the destination tile
     */
    public AITask(int mapx, int mapy) {
        type = TaskType.GO_TO_TILE;
        this.mapx = mapx;
        this.mapy = mapy;
    }
    
    /**
     * Creates a new AITask of type AITask.TaskType.ATTACK_UNIT
     * @param targetIndex The index on the current map of the target unit
     */
    public AITask(int targetIndex) {
        type = TaskType.ATTACK_UNIT;
        this.targetIndex = targetIndex;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public int getAssignedIndex() {
        return assignedIndex;
    }

    public void setAssignedIndex(int assignedIndex) {
        this.assignedIndex = assignedIndex;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }
}