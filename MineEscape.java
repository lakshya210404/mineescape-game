import java.util.Stack;

public class MineEscape {
    private Map map; // Object representing the map of the mine
    private int numGold; // Number of gold pieces collected
    private int[] numKeys; // Array to store the count of red, green, and blue keys

    public MineEscape(String filename) {
        try {
            map = new Map(filename); // Load map from a file
            numGold = 0; // Initialize gold count
            numKeys = new int[3]; // Initialize key counts for red, green, and blue keys
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Handle exceptions during map loading
        }
    }

    private MapCell findNextCell(MapCell curr) {
        final int MAX_NEIGHBORS = 4; // Assuming each cell has up to 4 neighbors

        // Special handling for adjacent lava cells
        for (int i = 0; i < MAX_NEIGHBORS; i++) {
            try {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && neighbor.isLava()) {
                    numGold = 0; // Reset gold count if adjacent to lava
                }
            } catch (InvalidNeighbourIndexException e) {
                // Ignore invalid neighbor exceptions
            }
        }

        // Check various conditions to find the next cell to move to

        // 1. Adjacent to the exit cell
        for (int i = 0; i < MAX_NEIGHBORS; i++) {
            try {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && neighbor.isExit() && !neighbor.isMarked()) {
                    return neighbor;
                }
            } catch (InvalidNeighbourIndexException e) {
                // Ignore invalid neighbor exceptions
            }
        }

        // 2. Cell with a collectible item (key or gold)
        for (int i = 0; i < MAX_NEIGHBORS; i++) {
            try {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && (neighbor.isGoldCell() || neighbor.isKeyCell()) && !neighbor.isMarked()) {
                    // Update gold or key counts
                    if (neighbor.isGoldCell()) numGold++;
                    if (neighbor.isKeyCell()) {
                        // Increment key count based on the color
                        if (neighbor.isRed()) numKeys[0]++;
                        if (neighbor.isGreen()) numKeys[1]++;
                        if (neighbor.isBlue()) numKeys[2]++;
                    }
                    return neighbor;
                }
            } catch (InvalidNeighbourIndexException e) {
                // Ignore invalid neighbor exceptions
            }
        }

        // 3. Floor cell
        for (int i = 0; i < MAX_NEIGHBORS; i++) {
            try {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && neighbor.isFloor() && !neighbor.isMarked()) {
                    return neighbor;
                }
            } catch (InvalidNeighbourIndexException e) {
                // Ignore invalid neighbor exceptions
            }
        }

        // 4. Locked door cell for which a key is available
        for (int i = 0; i < MAX_NEIGHBORS; i++) {
            try {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && neighbor.isLockCell() && !neighbor.isMarked()) {
                    if ((neighbor.isRed() && numKeys[0] > 0) ||
                        (neighbor.isGreen() && numKeys[1] > 0) ||
                        (neighbor.isBlue() && numKeys[2] > 0)) {
                        return neighbor;
                    }
                }
            } catch (InvalidNeighbourIndexException e) {
                // Ignore invalid neighbor exceptions
            }
        }

        // 5. No valid move available, return null for backtracking
        return null;
    }

    public String findEscapePath() {
        Stack<MapCell> stack = new Stack<>(); // Stack for pathfinding
        MapCell startCell = map.getStart(); // Starting cell
        stack.push(startCell);
        startCell.markInStack();
        StringBuilder path = new StringBuilder("Path: "); // Path description

        // Depth-first search to find escape path
        while (!stack.isEmpty()) {
            MapCell curr = stack.peek();

            // Check if the current cell is the exit
            if (curr.isExit()) {
                break;
            }

            // Handle collectibles (gold and keys)
            if (curr.isGoldCell()) {
                numGold++;
                curr.changeToFloor(); // Convert gold cell to floor after collection
            } else if (curr.isKeyCell()) {
                // Increment key count and convert key cell to floor
                if (curr.isRed()) numKeys[0]++;
                else if (curr.isGreen()) numKeys[1]++;
                else if (curr.isBlue()) numKeys[2]++;
                curr.changeToFloor();
            }

            // Find the next cell to move to
            MapCell next = findNextCell(curr);
            if (next == null) {
                // No valid move, backtrack
                curr.markOutStack();
                stack.pop();
            } else {
                // Valid move found, update path and continue
                path.append(next.getID()).append(" ");
                next.markInStack();
                stack.push(next);
            }
        }

        // Append the number of gold collected to the path
        path.append(numGold).append("G");
        return stack.isEmpty() ? "No solution found" : path.toString();
    }

    public static void main(String[] args) {
        // Main method to execute the program
        if (args.length != 1) {
            System.out.println("Map file not given in the arguments.");
            return;
        }
        MineEscape search = new MineEscape(args[0]); // Initialize with map file
        String result = search.findEscapePath(); // Execute the search for escape path
        System.out.println(result); // Print the result
    }
}
