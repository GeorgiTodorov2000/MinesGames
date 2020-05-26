import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.WildcardType;
import java.util.*;

public class MinesGames {
    public static final Scanner scan = new Scanner(System.in);
    public static final Random rand = new Random();
    public static final int START = rand.nextInt(4);
    public static int DISARM_COUNTER = 0;
    public static int ANALYSIS_COUNTER = 0;


    public static void main(String[] args) throws FileNotFoundException {
        fileReader();
    }

    public static int[] fileReader() throws FileNotFoundException {

        Scanner scanner = new Scanner( new File("C:\\Users\\bgrok\\IdeaProjects\\MinesGames\\src\\input") );
        String text = scanner.useDelimiter("\\A").next();
        char[] fileChars = new char[3];

        int count = 0;
        int[] fileCharsAsInt = new int[3];

        for(int i =0; i < text.length(); i++) {
            if(Character.isDigit(text.charAt(i))) {
                fileChars[count] = text.charAt(i);
                fileCharsAsInt[count] = Character.getNumericValue(fileChars[count]);
                count++;
            }
        }
        if (fileCharsAsInt[0] < 4 || fileCharsAsInt[1] < 4) {
            System.out.println("Minimum input for game table 4");
            System.exit(1);
        }
        if (fileCharsAsInt[2] < 5) {
            System.out.println("Minimum number of mines is 5");
            System.exit(1);
        }
        if (fileCharsAsInt[0] * fileCharsAsInt[1] - 2 < fileCharsAsInt[2]) {
            System.out.println("Too many mines");
            System.exit(1);
        }
        gameBoard(fileCharsAsInt);
        return fileCharsAsInt;
    }

    public static int[] configReader() throws FileNotFoundException {
        Scanner scanner = new Scanner( new File("C:\\Users\\bgrok\\IdeaProjects\\MinesGames\\src\\config") );
        String text = scanner.useDelimiter("\\A").next();
        char[] configChars = new char[2];
        int count = 0;
        int[] configCharsAsInt = new int[2];
        for(int i =0; i < text.length()-1; i++) {
            if(Character.isDigit(text.charAt(i))) {
                configChars[count] = text.charAt(i);
                configCharsAsInt[count] = Character.getNumericValue(configChars[count]);
                count++;
            }
        }
        return configCharsAsInt;
    }

    public static char[][] gameBoard(int[] fileCharsAsInt) throws FileNotFoundException {
        int count = 0;
        char[][] gameBoard = new char[fileCharsAsInt[0]][fileCharsAsInt[1]];

        for(int i = 0; i < fileCharsAsInt[0]; i++) {
            for(int j = 0; j < fileCharsAsInt[1]; j++) {
                gameBoard[i][j] = 'X';

            }
        }
        startToFinish(gameBoard, count, fileCharsAsInt, configReader());

        movementOptions(gameBoard, startToFinish(gameBoard, count, fileCharsAsInt, configReader()),
                mines(gameBoard, fileCharsAsInt), fileCharsAsInt, configReader());
        return gameBoard;
    }

    public static int[] startToFinish(char gameBoard[][], int count, int[] fileCharsAsInt, int[] configCharsAsInt) {
        int[] rowCol = new int[4];

        if (count == 0) {
        if(START == 0) {
            gameBoard[0][0] = 'S';
            gameBoard[fileCharsAsInt[0]-1][fileCharsAsInt[1]-1] = 'F';
            rowCol[0] = 0;
            rowCol[1] = 0;
            rowCol[2] = fileCharsAsInt[0]-1;
            rowCol[3] = fileCharsAsInt[1]-1;
            count++;
        } else if(START == 1) {
            gameBoard[0][fileCharsAsInt[1]-1] = 'S';
            gameBoard[fileCharsAsInt[0]-1][0] = 'F';
            rowCol[0] = 0;
            rowCol[1] = fileCharsAsInt[1]-1;
            rowCol[2] = fileCharsAsInt[0]-1;
            rowCol[3] = 0;
            count++;
        } else if(START == 2) {
            gameBoard[fileCharsAsInt[0]-1][0] = 'S';
            gameBoard[0][fileCharsAsInt[1]-1] = 'F';
            rowCol[0] = fileCharsAsInt[0]-1;
            rowCol[1] = 0;
            rowCol[2] = 0;
            rowCol[3] = fileCharsAsInt[1]-1;
            count++;
        } if(START == 3) {
            gameBoard[fileCharsAsInt[0]-1][fileCharsAsInt[1]-1] = 'S';
            gameBoard[0][0] = 'F';
            rowCol[0] = fileCharsAsInt[0]-1;
            rowCol[1] = fileCharsAsInt[1]-1;
            rowCol[2] = 0;
            rowCol[3] = 0;
            count++;
        }
        }
        return rowCol;
    }

    public static char[][] mines(char[][] gameBoard, int[] fileCharsAsInt) {
        for (int i = 0; i < fileCharsAsInt[0]; i++) {
            for (int j = 0; j < fileCharsAsInt[1]; j++) {
                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }
        int mineCount = 0;
        char[][] gameBoardWithMines = new char[fileCharsAsInt[0]][fileCharsAsInt[1]];
        for (int i = 0; i < fileCharsAsInt[0];i++) {
            for(int j = 0; j < fileCharsAsInt[1]; j++) {
                gameBoardWithMines[i][j] = gameBoard[i][j];
            }
        }

        if (fileCharsAsInt[0] * fileCharsAsInt[1] < fileCharsAsInt[2]) {
            System.out.println("To many mines try again");
            mines(gameBoard, fileCharsAsInt);
        }
        for (int i = 0; i < fileCharsAsInt[0]; i++) {
            for (int j = 0; j < fileCharsAsInt[1]; j++) {
                if(mineCount < fileCharsAsInt[2]) {
                    int chance = rand.nextInt(100);
                    if (!Objects.equals(gameBoard[i][j], 'S') && !Objects.equals(gameBoard[i][j], 'F')) {
                        if (chance < 50 ) {
                            gameBoardWithMines[i][j] = 'B';
                            mineCount++;
                        } else if (fileCharsAsInt[0] * fileCharsAsInt[1] - 2 <= fileCharsAsInt[2]) {
                            gameBoardWithMines[i][j] = 'B';
                            mineCount++;
                        }
                    }
                }
            }
        }
        return gameBoardWithMines;
    }

    private static void check(char check, char finVar, char[][] gameBoard, int[] rowCol,
                              char[][] gameBoardWithMines, int[] fileCharsAsInt) {

        int warStationCol = rowCol[0];
        int warStationRow = rowCol[1];
        int startCol = warStationCol - 1;
        int stopCol = warStationCol + 1;
        int startRow = warStationRow - 1;
        int stopRow = warStationRow + 1;

        if (startCol < 0)
        {
            startCol = 0;
        }

        if (stopCol > fileCharsAsInt[0]-1)
        {
            stopCol = fileCharsAsInt[0]-1;
        }

        if (startRow < 0)
        {
            startRow = 0;
        }

        if (stopRow > fileCharsAsInt[1]-1)
        {
            stopRow = fileCharsAsInt[1]-1;
        }

        for(int i = startCol; i <= stopCol; i++) {
            for(int j = startRow; j <= stopRow; j++) {
                if(Objects.equals(gameBoardWithMines[i][j], check)) {
                    gameBoard[i][j] = finVar;
                }
            }
        }
    }

    public static int movementOptions(char[][] gameBoard, int[] rowCol,
                                      char[][] gameBoardWithMines, int[] fileCharsAsInt, int[] configReader) throws FileNotFoundException {
        System.out.println("1.Analysis");
        System.out.println("2.Disarm");
        System.out.println("3.Move to");
        int[] count = new int[2];
        int moveOption = scan.nextInt();
        switch(moveOption) {
            case 1:
                if (ANALYSIS_COUNTER < configReader[0]) {
                    ANALYSIS_COUNTER++;
                    char hiddenMine = 'B';
                    char visibleMine = 'Y';
                    check(hiddenMine, visibleMine, gameBoard, rowCol, gameBoardWithMines, fileCharsAsInt);
                    movement(gameBoard, rowCol, gameBoardWithMines, moveOption, fileCharsAsInt);
                    return 1;
                } else {
                    System.out.println("Your scanner run out of power");
                    movementOptions(gameBoard, rowCol, gameBoardWithMines, fileCharsAsInt, configReader);
                }
            case 2:
                if (DISARM_COUNTER < configReader[1]) {
                    DISARM_COUNTER--;
                    movement(gameBoard, rowCol, gameBoardWithMines, moveOption, fileCharsAsInt);

                    return 2;
                } else {
                    System.out.println("Your disarm tool broke");
                    movementOptions(gameBoard, rowCol, gameBoardWithMines, fileCharsAsInt, configReader);
                }

            case 3:
                movement(gameBoard,rowCol, gameBoardWithMines, moveOption, fileCharsAsInt);
                return 3;
        }

        return moveOption;
    }

    public static int movement(char[][] gameBoard, int[] rowCol,
                               char[][] gameBoardWithMines, int moveOption, int[] fileCharsAsInt) throws FileNotFoundException {


        int warStationStartingCol = rowCol[0];
        int warStationStartingRow = rowCol[1];
        int warWinCol = rowCol[2];
        int warWinRow = rowCol[3];
        boolean win = false;
        int count = 0;
        if (gameBoard[warStationStartingCol][warStationStartingRow] == gameBoard[warWinCol][warWinRow]) {
            System.out.println("You won");
            System.exit(1);
        }
        while (!win) {
            for (int i = 0; i < fileCharsAsInt[0]; i++) {
                for (int j = 0; j < fileCharsAsInt[1]; j++) {
                    System.out.print(gameBoard[i][j]);
                }
                System.out.println();
            }
            System.out.println("Where do you want to move to, input N S E W");
            String move = scan.next();
            if (move.equals("N")) {
                if (moveOption != 2) {
                    if (Objects.equals(gameBoardWithMines[warStationStartingCol-1][warStationStartingRow], 'B')) {
                        System.out.println("You lost");
                        System.exit(1);
                    } else if (Objects.equals(gameBoardWithMines[warStationStartingCol-1][warStationStartingRow], 'Y')) {
                        System.out.println("You lost");
                        System.exit(1);
                    }
                }
                if (gameBoard[warStationStartingCol-1][warStationStartingRow] == gameBoard[warWinCol][warWinRow]) {
                    System.out.println("You won");
                    System.exit(1);
                }
                gameBoardWithMines[warStationStartingCol-1][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol - 1][warStationStartingRow] = '*';
                count++;
                rowCol[0] = warStationStartingCol-1;
                rowCol[1] = warStationStartingRow;
            } else if (move.equals("S")) {
                if (moveOption != 2) {
                    if (Objects.equals(gameBoardWithMines[warStationStartingCol+1][warStationStartingRow], 'B')) {
                        System.out.println("You lost");
                        System.exit(1);
                    } else if (Objects.equals(gameBoardWithMines[warStationStartingCol+1][warStationStartingRow], 'Y')) {
                        System.out.println("You lost");
                        System.exit(1);
                    }
                }
                if (gameBoard[warStationStartingCol+1][warStationStartingRow] == gameBoard[warWinCol][warWinRow]) {
                    System.out.println("You won");
                    System.exit(1);
                }
                gameBoardWithMines[warStationStartingCol+1][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol + 1][warStationStartingRow] = '*';
                count++;
                rowCol[0] = warStationStartingCol+1;
                rowCol[1] = warStationStartingRow;
            } else if (move.equals("E")) {
                if (moveOption != 2) {
                    if (Objects.equals(gameBoardWithMines[warStationStartingCol][warStationStartingRow+1], 'B')) {
                        System.out.println("You lost");
                        System.exit(1);
                    } else if (Objects.equals(gameBoardWithMines[warStationStartingCol][warStationStartingRow+1], 'Y')) {
                        System.out.println("You lost");
                        System.exit(1);
                    }
                }
                if (gameBoard[warStationStartingCol][warStationStartingRow+1] == gameBoard[warWinCol][warWinRow]) {
                    System.out.println("You won");
                    System.exit(1);
                }
                gameBoardWithMines[warStationStartingCol][warStationStartingRow+1] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow + 1] = '*';
                count++;
                rowCol[0] = warStationStartingCol;
                rowCol[1] = warStationStartingRow+1;
            } else if (move.equals("W")) {
                if (moveOption != 2) {
                    if (Objects.equals(gameBoardWithMines[warStationStartingCol][warStationStartingRow-1], 'B')) {
                        System.out.println("You lost");
                        System.exit(1);
                    } else if (Objects.equals(gameBoardWithMines[warStationStartingCol][warStationStartingRow-1], 'Y')) {
                        System.out.println("You lost");
                        System.exit(1);
                    }
                }
                if (gameBoard[warStationStartingCol][warStationStartingRow-1] == gameBoard[warWinCol][warWinRow]) {
                    System.out.println("You won");
                    System.exit(1);
                }
                gameBoardWithMines[warStationStartingCol][warStationStartingRow-1] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow] = 'V';
                gameBoard[warStationStartingCol][warStationStartingRow - 1] = '*';
                count++;
                rowCol[0] = warStationStartingCol;
                rowCol[1] = warStationStartingRow-1;
            }

            for (int i = 0; i < fileCharsAsInt[0]; i++) {
                for (int j = 0; j < fileCharsAsInt[1]; j++) {
                    System.out.print(gameBoard[i][j]);
                }
                System.out.println();
            }

            movementOptions(gameBoard,rowCol, gameBoardWithMines, fileCharsAsInt, configReader());
        }
        return count;
    }

}
