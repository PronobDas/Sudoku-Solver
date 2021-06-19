import java.util.*;
import java.lang.*;
import java.io.*;

public class BoardSlot
{
    int degree;
    int row;
    int col;
    ArrayList<Integer> domainList;

    public BoardSlot(int row, int col, int degree, ArrayList list)
    {
        this.row = row;
        this.col = col;
        this.degree = degree;
        this.domainList = list;
    }

    public void print()
    {
        System.out.println("row: "+ row + " col: "+col +" degree: "+ degree);
        System.out.println(this.domainList.size());
        for (int i =0; i < this.domainList.size(); i++)
            System.out.print(this.domainList.get(i) + " ");
    }
}

class SortbyDegree implements Comparator<BoardSlot>
{
    public int compare(BoardSlot a, BoardSlot b)
    {
        return a.degree - b.degree;
    }
}

class SortbyDomainSize implements Comparator<BoardSlot>
{
    public int compare(BoardSlot a, BoardSlot b)
    {
        return a.domainList.size() - b.domainList.size();
    }
}