import java.util.Scanner;

public class BrickworkAlgorithm {

    //Used to check if all the bricks have been placed
    private int filled;
    //Used to number the bricks when placing them
    private int move;

    public static void main(String[] args) {
        new BrickworkAlgorithm().run();
    }

    public  void run() {

        Scanner scanner = new Scanner(System.in);

        //make sure these are 0 before running GetSecondLayer
        filled = 0;
        move = 1;

        //number of rows and columns
        int[] parameters = null;


        //get and verify parameters
        while (parameters == null) {
            System.out.print("Enter number of rows and columns separated by space: ");
            String input = scanner.nextLine();

            String[] split = input.split(" ");

            if(split.length != 2) {
                System.out.println("Incorrect input");
            } else {
                try {
                    parameters = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};

                    int value1 = Integer.parseInt(split[0]);
                    int value2 = Integer.parseInt(split[1]);

                    if (value1%2 != 0 || value1 >= 100 || value2%2 != 0 || value2 >= 100) {
                        System.out.println("Values should be even and less than 100");
                        parameters = null;
                    }

                } catch (Exception e) {
                    System.out.println("Incorrect input!");
                }
            }
        }

        int[][] input = new int[parameters[0]][parameters[1]];

        boolean inputSet = false;

        //get and verify brick positions
        while (!inputSet) {

            inputSet = true;

            for (int i = 0; i < input.length; i++) {
                System.out.print("Enter values for line " + i + " separated by spaces: ");

                String values = scanner.nextLine();

                String[] split = values.split(" ");

                if (split.length == parameters[1]) {
                    try {
                        for (int j = 0; j < parameters[1]; j++) {
                            input[i][j] = Integer.parseInt(split[j]);
                        }

                    } catch (Exception e) {
                        System.out.println("Incorrect input");
                    }
                } else {
                    inputSet = false;
                }
            }

            if(!ValidateArray(input)) {
                inputSet = false;
            }

            if (!inputSet) {
                System.out.println("Incorrect input");
            }
        }

        if(GetSecondLayer(input, 0, 0)) {
            PrintArray(input);
        } else System.out.println(-1 + " no solution exists");


    }

    //print the array, bricks will be surrounded by * signs
    public  void PrintArray(int[][] array) {
        System.out.println();

        for (int i = 0; i < array.length; i++) {
            System.out.print("*");
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(-array[i][j]);
                if(j == array[i].length - 1) {
                    System.out.print("*");
                } else if (j != array[i].length - 1 && array[i][j] != array[i][j+1]) {
                    System.out.print("*");
                } else System.out.print(" ");
            }

            System.out.println();
            for (int j = 0; j < array[i].length; j++) {
                if((i != array.length -1 && array[i + 1][j] != array[i][j]) ) {
                    System.out.print(" *");
                } else System.out.print("  ");
            }
            System.out.println();
        }

    }

    //make sure bricks no bricks span 3 rows or columns
    public boolean ValidateArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {

                if(j < array[i].length - 2 && array[i][j] == array[i][j + 1] && array[i][j] == array[i][j + 2]) {
                    System.out.println("false");
                    return false;
                }

                if (i < array.length - 2 && array[i][j] == array[i + 1][j] && array[i][j] == array[i + 2][j]) {
                    System.out.println("false");
                    return false;
                }

            }
        }

        return true;
    }

    //replace each int in the array so that the new array represents the second layer of bricks
    //uses recursion and backtracking
    public boolean GetSecondLayer(int[][] array, int startRow, int startCol) {
        if(filled == array.length*array[0].length/2) return true;

        int nextStartRow = startRow;
        int nextStartCol = startCol;
        int[] originalValues = {0, 0};
        boolean pathOneTaken = false;

        for (int i = nextStartRow; i < array.length; i++) {
            for (int j = nextStartCol; j < array[0].length; j++) {
                if(array[i][j] > 0) {
                    if(j + 1 < array[i].length && array[i][j] != array[i][j+1] && array[i][j + 1] > 0) {
                        originalValues[0] = array[i][j];
                        originalValues[1] = array[i][j + 1];
                        //- before the int is used to represent second layer bricks
                        array[i][j] = -move;
                        array[i][j + 1] = -move;
                        move++;
                        j++;
                        filled++;
                        pathOneTaken = true;

                        if (j + 1 < array[i].length) {
                            nextStartCol+=2;
                        } else {
                            nextStartRow++;
                            nextStartCol = 0;
                        }
                    } else if(i + 1 < array.length && array[i + 1][j] != array[i][j] && array[i + 1][j] > 0) {
                        array[i][j] = -move;
                        array[i + 1][j] = -move;
                        move++;
                        filled++;

                        if (j + 1 < array[i].length) {
                            nextStartCol++;
                        } else {
                            nextStartRow++;
                            nextStartCol = 0;
                        }
                    } else {
                        return false;
                    }

                    if(GetSecondLayer(array, nextStartRow, nextStartCol)) {
                        return true;
                    }

                    if(i + 1 < array.length && array[i + 1][j] != array[i][j] && pathOneTaken) {
                        pathOneTaken = false;
                        j--;
                        array[i][j] = originalValues[0];
                        array[i][j + 1] = originalValues[1];

                        array[i][j] = - (move - 1);
                        array[i + 1][j] = - (move - 1);
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
