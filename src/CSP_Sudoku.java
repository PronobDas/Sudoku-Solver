import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class CSP_Sudoku
{
        static int N = 10;
        static int noOfNode = 0;
        static int noOfBT = 0;
        static int currentNode = 0;
        static ArrayList<BoardSlot> arrlist = new ArrayList<BoardSlot>();

        public static int calDegree(int[][] sudokuBoard, int r, int c)
        {
            //System.out.println("Calculating degree");
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
            //for(int i = 0; i<list.size(); i++)
            //    System.out.print(list.get(i)+" ");

            //System.out.println();

            for (int i = 0; i < N; i++)
            {
                if (sudokuBoard[r][i] != 0 && i != c)
                    list.remove(Integer.valueOf(sudokuBoard[r][i]));

                if (sudokuBoard[i][c] != 0 && i != r)
                    list.remove( Integer.valueOf(sudokuBoard[i][c]));
            }
            //for(int i = 0; i<list.size(); i++)
            //    System.out.print(list.get(i)+" ");
            //System.out.println();
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

        public static boolean isSafe2(int[][] sudokuBoard, int r, int c, int value)
        {
            sudokuBoard[r][c] = value;

            //check in the row
            for (int i = 0; i < N; i++)
            {
                if ( sudokuBoard[r][i] == 0 && getDomain(sudokuBoard,r,i).size() == 0 )
                {
                    sudokuBoard[r][c] = 0;
                    return false;
                }

            }

            for (int i = 0; i < N; i++)
            {
                if ( sudokuBoard[i][c] == 0 && getDomain(sudokuBoard,i,c).size() == 0)
                {
                    sudokuBoard[r][c] = 0;
                    return false;
                }
            }
            sudokuBoard[r][c] = 0;
            return true;
        }

        public static boolean solve_1(int[][] sudokuBoard) //random + bt
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
                if( isSafe(sudokuBoard, row, col, value))
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

        public static boolean solve_2(int[][] sudokuBoard) //random + fc
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
            if( isSafe(sudokuBoard, row, col, value) && isSafe2(sudokuBoard, row, col, value))
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_2(sudokuBoard) )
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

        static int t1 = 0;
        public static boolean solve_3(int[][] sudokuBoard) //sdf(static) + bt
        {
            int row = -1, col = -1;

            if(currentNode == N*N-t1)
                return true;

            while(arrlist.get(0).domainList.size() == 0)
            {
                arrlist.remove(0);
                t1++;
            }

            row = arrlist.get(currentNode).row;
            col = arrlist.get(currentNode).col;

            for (int value = 1; value <= N ; value++)
            {
                if( isSafe(sudokuBoard, row, col, value) )
                {
                    noOfNode++;
                    sudokuBoard[row][col] = value;

                    currentNode++;
                    if ( solve_3(sudokuBoard) )
                    {
                        return true;
                    }
                    else
                    {
                        currentNode--;
                        sudokuBoard[row][col] = 0;
                        noOfBT++;
                    }
                }
            }
            return false;
        }

        static int t2 = 0;
        public static boolean solve_4(int[][] sudokuBoard) //sdf(static) + fc
        {
        int row = -1, col = -1;

        if(currentNode == N*N - t2)
            return true;

        while(arrlist.get(0).domainList.size() == 0)
        {
            arrlist.remove(0);
            t2++;
        }

        row = arrlist.get(currentNode).row;
        col = arrlist.get(currentNode).col;

        ArrayList l = arrlist.get(currentNode).domainList;
        for (int i = 0; i < l.size(); i++)
        {
            int value = (int)l.get(i);
            if( isSafe(sudokuBoard, row, col, value) && isSafe2(sudokuBoard, row, col, value))
            {
                noOfNode++;
                sudokuBoard[row][col] = value;

                currentNode++;
                if ( solve_4(sudokuBoard) )
                {
                    return true;
                }
                else
                {
                    currentNode--;
                    sudokuBoard[row][col] = 0;
                    noOfBT++;
                }
            }
        }
        return false;
        }

        public static boolean solve_5(int[][] sudokuBoard) // sdf(dynamic) + fc
        {
        int row = -1, col = -1;
        boolean empty = false;

        int lowestDomainSize = 9999;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if(sudokuBoard[i][j] == 0) {
                    int temp = getDomain(sudokuBoard, i, j).size();
                    if (temp < lowestDomainSize) {
                        row = i;
                        col = j;
                        lowestDomainSize = temp;
                        empty = true;
                        //break;
                    }
                }
            }
        }

        if( !empty )
            return true;

        for (int value = 1; value <= N ; value++)
        {
            if( isSafe(sudokuBoard, row, col, value) && isSafe2(sudokuBoard, row, col, value))
            {
                noOfNode++;
                sudokuBoard[row][col] = value;
                if ( solve_5(sudokuBoard) )
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
                File myObj = new File("data/d-10-09.txt.txt");
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
                            sudokuBoard[a][i] = temp;
                        }
                        else {
                            var data2 = data[i].split(" ");
                            int temp = Integer.parseInt(data2[0]);
                            sudokuBoard[a][i] = temp;
                        }
                    }
                    a++;
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            for (int i = 0; i < N ; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    //System.out.println(getDomain(sudokuBoard, i, j).size() + "Dom Size");
                    arrlist.add( new BoardSlot( i, j, calDegree(sudokuBoard, i, j), getDomain(sudokuBoard, i, j)));
                }
            }
            //Collections.sort(arrlist, new SortbyDegree());
            Collections.sort(arrlist, new SortbyDomainSize());

            solve_5(sudokuBoard);
            print(sudokuBoard);
            System.out.println("No of Nodes: " + noOfNode);
            System.out.println("No of Back Tracks: " + noOfBT);
        }
}