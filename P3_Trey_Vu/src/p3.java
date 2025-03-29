import java.io.FileNotFoundException;

public class p3 {
	
    public static void main(String[] args) throws FileNotFoundException {
        try {
            parseAndRun(args);
        } catch (IllegalCommandLineInputsException e) {
            System.err.println("Command line error: " + e.getMessage());
            System.exit(-1);
        } catch (IllegalMapCharacterException | IncompleteMapException | IncorrectMapFormatException e) {
            // or do separate catch blocks if you prefer
            System.err.println("Map/Format error: " + e.getMessage());
            System.exit(-1);
        } 
        // You can also catch (FileNotFoundException e) if your Maze constructor throws it
    } 

    private static void parseAndRun(String[] args)
            throws FileNotFoundException,
            	   IllegalCommandLineInputsException,
                   IllegalMapCharacterException,
                   IncompleteMapException,
                   IncorrectMapFormatException {
        // 1) Basic checks
        if (args.length < 2) {
            throw new IllegalCommandLineInputsException("No arguments provided. Usage: java p3 [options] <filename>");
        }

        // 2) The last argument is the filename
        String filename = args[args.length - 1];

        // Booleans for the switches
        boolean useStack       = false;
        boolean useQueue       = false;
        boolean useOpt         = false;
        boolean measureTime    = false;
        boolean inCoordinate   = false;
        boolean outCoordinate  = false;
        boolean showHelp       = false;

        // 3) Parse everything except the last arg
        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            switch (arg) {
                case "--Stack"        -> useStack       = true;
                case "--Queue"        -> useQueue       = true;
                case "--Opt"          -> useOpt         = true;
                case "--Time"         -> measureTime    = true;
                case "--Incoordinate" -> inCoordinate   = true;
                case "--Outcoordinate"-> outCoordinate  = true;
                case "--Help"         -> showHelp       = true;
                default -> throw new IllegalCommandLineInputsException("Unknown switch: " + arg);
            }
        }

        // 4) If help is set, print usage and exit
        if (showHelp) {
            printHelpMessage();
            System.exit(0);
        }

        // 5) Exactly one approach must be set
        int approachCount = 0;
        if (useStack) approachCount++;
        if (useQueue) approachCount++;
        if (useOpt)   approachCount++;

        if (approachCount != 1) {
            throw new IllegalCommandLineInputsException("Must specify exactly one approach: --Stack, --Queue, or --Opt (but not multiple).");
        }

        // 6) Build the Maze (auto-detect text or coordinate-based)
        // If you prefer to pass inCoordinate explicitly, create a second constructor: new Maze(filename, inCoordinate)
        Maze maze = new Maze(filename, inCoordinate);

        // 7) Create MazeSolver
        MazeSolver solver = new MazeSolver(maze);
        System.out.println("DEBUG: Number of starting positions (W): " + maze.getAllWolverines().size());
        for (Tile w : maze.getAllWolverines()) {
            System.out.printf("DEBUG: W at (room=%d, row=%d, col=%d)%n", w.getRoom(), w.getRow(), w.getCol());
        }



        // 8) Start timing if we want
        long startTime = System.nanoTime();

        // 9) Actually run BFS/DFS/Opt from each W in the Maze
        boolean foundCoin = false;
        // get all Wolverine positions
        var wolverines = maze.getAllWolverines();

        for (var w : wolverines) {
            boolean success;
            if (useStack) {
                success = solver.solveWithStack(w);
            } else if (useQueue) {
                success = solver.solveWithQueue(w);
            } else { // useOpt
                success = solver.solveWithOpt(w);
            }
            if (success) {
                foundCoin = true;
                break;
            }
        }

        // 10) End timing
        long endTime = System.nanoTime();

        // 11) If measureTime is set, print the runtime
        if (measureTime) {
            double totalSeconds = (endTime - startTime) / 1e9;
            System.out.printf("Total Runtime: %.3f seconds\n", totalSeconds);
        }

        // 12) If outCoordinate is set, print route in coordinate-based form
        if (outCoordinate) {
            // You need to adapt BFS/DFS to store the actual path if you want
            // a correct route in coordinate form. Here is just a placeholder:
            System.out.println("Coordinate-based route output (stub) not yet implemented!");
        } else {
            // Print the final Maze with '+' for the path
            maze.printMaze();
        }

        // 13) If no path found, print "The Wolverine Store is closed."
        if (!foundCoin) {
            System.out.println("The Wolverine Store is closed.");
        }
    }

    private static void printHelpMessage() {
        System.out.println("Usage: java p3 [options] <filename>");
        System.out.println("Options:");
        System.out.println("  --Stack         Use stack-based approach (DFS)");
        System.out.println("  --Queue         Use queue-based approach (BFS)");
        System.out.println("  --Opt           Use optimal path approach");
        System.out.println("  --Time          Print runtime of the search algorithm");
        System.out.println("  --Incoordinate  Input file is coordinate-based (if your Maze supports it)");
        System.out.println("  --Outcoordinate Output route in coordinate-based format");
        System.out.println("  --Help          Print this message and exit");
        System.out.println();
        System.out.println("Example: java p3 --Queue --Time myMaze.txt");
    }
    
    // for mac: java -cp . p3 --Opt C2
}
