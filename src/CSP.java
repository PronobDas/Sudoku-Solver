import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSP {
    static int N = 4;
    static int noOfNode = 0;
    static int noOfBT = 0;

    static int currentNode = 0;

    static ArrayList<BoardSlot> arrlist = new ArrayList<BoardSlot>();

    public static int calDegree(int[][] sudokuBoard, int r, int c)
    {
        System.out.println("Calculating degree");
        int degree = 0;
        for (int i = 0; i < N; i++)
        {
            if (sudokuBoard[r][i] == 0 && i != c)
                degree++;

            if (sudokuBoard[i][c] == 0 && i != r)
                degree++;
        }
        return degree;
    }

    public static ArrayList getDomain(int[][] sudokuBoard, int r, int c)
    {
        ArrayList<Integer> list = new ArrayList<>();
        if ( sudokuBoard[r][c] != 0 )
            return list;

        for(int i = 1; i <= N; i++)
        {
            list.add(i);
        }
        for(int i = 0; i<list.size(); i++)
            System.out.print(list.get(i)+" ");

        System.out.println();

        for (int i = 0; i < N; i++)
        {
            if (sudokuBoard[r][i] != 0 && i != c)
                list.remove(Integer.valueOf(sudokuBoard[r][i]));

            if (sudokuBoard[i][c] != 0 && i != r)
                list.remove( Integer.valueOf(sudokuBoard[i][c]));
        }
        for(int i = 0; i<list.size(); i++)
            System.out.print(list.get(i)+" ");
        System.out.println();
        return list;
    }

    public static boolean isSafe(int[][] sudokuBoard, int r, int c, int value)
    {
        for (int i = 0; i < N; i++)
        {
            if (sudokuBoard[r][i] == value)
                return false;

            if (sudokuBoard[i][c] == value)
                return false;
        }

        /*for (int i = 0; i < N; i++)
        {
            if (sudokuBoard[i][c] == value)
                return false;
        }*/
        return true;
    }

    public static boolean solve_1(int[][] sudokuBoard) //find out the first empty slot, then assign
    {
        int row = -1, col = -1;
        boolean empty = false;
        for (int i = 0; i < N ; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if( sudokuBoard[i][j] == 0)
                {
                    row = i;
                    col = j;

                    empty = true;
                    break;
                }
            }
            if( empty )
                break;
        }

        if( !empty )
            return true;

        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) )
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_1(sudokuBoard) )
                {
                    return true;
                }
                else
                {
                    sudokuBoard[row][col] = 0;
                    noOfBT++;
                }
            }
        }
        return false;
    }

    public static boolean solve_2(int[][] sudokuBoard) //random assign
    {
        int row = -1, col = -1;
        boolean empty = false;
        Random random = new Random();

        for (int i = 0; i < N ; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if( sudokuBoard[i][j] == 0)
                {
                    //row = i;
                    //col = j;
                    row = random.nextInt(N);
                    col = random.nextInt(N);

                    while ( sudokuBoard[row][col] != 0 )
                    {
                        row = random.nextInt(N);
                        col = random.nextInt(N);
                    }

                    empty = true;
                    break;
                }
            }
            if( empty )
                break;
        }

        if( !empty )
            return true;

        System.out.println("Hii");
        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) )
            {
                System.out.println("Node");
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_2(sudokuBoard) )
                {
                    print(sudokuBoard);
                    return true;
                }
                else
                {
                    sudokuBoard[row][col] = 0;
                    noOfBT++;
                    System.out.println("BT");
                }
            }
        }
        return false;
    }


    public static boolean solve_3(int[][] sudokuBoard) //dynamic ordering
    {
        int row = -1, col = -1;
        boolean empty = false;

        int lowestDegree = 9999;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if(sudokuBoard[i][j] != 0) {
                    int temp = calDegree(sudokuBoard, i, j);
                    if (temp < lowestDegree) {
                        row = i;
                        col = j;
                        lowestDegree = temp;
                        empty = true;
                        //break;
                    }
                }
            }
            //if( empty )
              //  break;
        }

        if( !empty )
            return true;

        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) )
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_3(sudokuBoard) )
                {
                    return true;
                }
                else
                {
                    sudokuBoard[row][col] = 0;
                    noOfBT++;
                }
            }
        }
        return false;
    }

    static int b = 0;
    public static boolean solve_4(int[][] sudokuBoard) //static ordering
    {
        int row = -1, col = -1;

        if(currentNode == N*N - b)
            return true;

        row = arrlist.get(currentNode).row;
        col = arrlist.get(currentNode).col;
        //arrlist.remove(0);

        /*while ( sudokuBoard[row][col] != 0 )
        {
            b++;
            arrlist.remove(currentNode);
            row = arrlist.get(currentNode).row;
            col = arrlist.get(currentNode).col;
        }*/

        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) )
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                currentNode++;

                System.out.println("Insert value");
                print(sudokuBoard);

                if ( solve_4(sudokuBoard) )
                {
                    return true;
                }
                else
                {
                    currentNode--;
                    sudokuBoard[row][col] = 0;
                    noOfBT++;

                    System.out.println("remove value");
                    print(sudokuBoard);
                }
            }
        }
        return false;
    }

    public static boolean solve_5(int[][] sudokuBoard) //sdf
    {
        int row = -1, col = -1;

        if(arrlist.size() == 0)
            return true;

        while(arrlist.get(0).domainList.size() == 0)
            arrlist.remove(0);

        row = arrlist.get(0).row;
        col = arrlist.get(0).col;
        //arrlist.remove(0);

        /*while ( sudokuBoard[row][col] != 0 )
        {
            row = arrlist.get(0).row;
            col = arrlist.get(0).col;
            arrlist.remove(0);

            if( arrlist.size() == 0)
                return true;
        }*/
        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) )
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_5(sudokuBoard) )
                {
                    arrlist.remove(0);
                    return true;
                }
                else
                {
                    sudokuBoard[row][col] = 0;
                    noOfBT++;
                }
            }
        }
        return false;
    }

    public static void print(int[][] sudokuBoard)
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                System.out.print(sudokuBoard[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        int sudokuBoard[][] = new int[N][N];

        try {
            File myObj = new File("data/d-10-010.txt.txt");
            Scanner myReader = new Scanner(myObj);

            //Skip the first 3 lines.
            for(int i = 0; i < 3; i++)
            {
                myReader.nextLine();
            }
            int a = 0;
            while (myReader.hasNextLine())
            {
                var data = myReader.nextLine().split(", ");
                for (int i = 0; i < data.length; i++)
                {
                    if(i != data.length-1) {
                        int temp = Integer.parseInt(data[i]);
                        System.out.print(temp + " ");
                        sudokuBoard[a][i] = temp;
                    }
                    else {
                        var data2 = data[i].split(" ");
                        int temp = Integer.parseInt(data2[0]);
                        System.out.print(temp);
                        sudokuBoard[a][i] = temp;
                    }
                }
                System.out.println();
                a++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

       // System.out.println("\nHelloooooooo");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                if (sudokuBoard[i][j] == 0)
                    b++;
        }

        for (int i = 0; i < N ; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (sudokuBoard[i][j] != 0)
                    arrlist.add( new BoardSlot( i, j, calDegree(sudokuBoard, i, j), getDomain(sudokuBoard, i, j)));
            }
        }
        Collections.sort(arrlist, new SortbyDegree());

        //Collections.sort(arrlist, new SortbyDomainSize());

        System.out.println("Sorting");
        for(int i = 0; i<arrlist.size(); i++)
        {
            System.out.println(i);
            arrlist.get(i).print();
        }

        System.out.println(arrlist.size());

        solve_4(sudokuBoard);
        print(sudokuBoard);
        System.out.println("No of Back Tracks: " + noOfBT);
        System.out.println("No of Nodes: " + noOfNode);


        /*for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
            {
                ArrayList a = getDomain(sudokuBoard, i, j);
                if(a.size() == 0)
                {
                    System.out.println("empty");
                }
            }

            System.out.println("\n");
        }*/
    }
}
