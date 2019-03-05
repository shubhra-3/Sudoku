import java.util.*;
import java.io.*;

class dll{
int MAX_ROW = 740;
int MAX_COL = 350;

class Node {
    public Node left;
    public Node right;
    public Node up;
    public Node down;
    public Node column;
    public int rowID;
    public int colID;
    public int nodeCount;
    //constructor
   Node(){
        this.left=null;
        this.right=null;
        this.up=null;
        this.down=null;
        this.column=null;
        this.rowID=0;
        this.colID=0;
    }
} 

int target[][] ;
int c;
boolean ProbMat[][];
Node Matrix[][];

void initMat(){
    Matrix=new Node[MAX_ROW][MAX_COL] ;
}

int flag;
int n=81;
Node header = new Node();
//for storing final solutions
//ArrayList< Integer >solutions = new ArrayList< Integer >(81);
Node solutions[];
int index=0;
//List<int> solutions = new ArrayList<int>();
int nRow ,nCol ;

// Functions to get next index in any direction
// for given index (circular in nature) 
int getRight(int i){return (i+1) % nCol; }
int getLeft(int i){return (i-1 < 0) ? nCol-1 : i-1 ; }
int getUp(int i){return (i-1 < 0) ? nRow : i-1 ; }  
int getDown(int i){return (i+1) % (nRow+1); }

Node createToridolMatrix(){

for(int i = 0; i < nRow; i++)
    {
        for(int j = 0; j < nCol; j++)
        {
            // If it's 1 in the problem matrix then 
            // only create a node 
            if(ProbMat[i][j] == true)
            {
                int a, b;
                Matrix[i][j].colID=j; 
                Matrix[i][j].rowID=i;
                
                // If it's 1, other than 1 in 0th row
                // then count it as node of column 
                // and increment node count in column header
                if(i!=0) Matrix[0][j].nodeCount += 1;
		Matrix[i][j].column = Matrix[0][j];
		Matrix[i][j].rowID = i;
                Matrix[i][j].colID = j;
                // Link the node with neighbors
                // Left pointer
                a = i; b = j;
                do
                {
                    b = getLeft(b); 
                } while(!ProbMat[a][b] && b != j);
                Matrix[i][j].left = Matrix[i][b];
 
                // Right pointer
                a = i; b = j;
                do { b = getRight(b); } while(!ProbMat[a][b] && b != j);
                Matrix[i][j].right = Matrix[i][b];
 
                // Up pointer
                a = i; b = j;
                do { a = getUp(a); } while(!ProbMat[a][b] && a != i);
                Matrix[i][j].up = Matrix[a][j];
 
                // Down pointer
                a = i; b = j;
                do { a = getDown(a); } while(!ProbMat[a][b] && a != i);
                Matrix[i][j].down = Matrix[a][j];
            }
        }
    }
    // link header right pointer to column 
    // header of first column 
    header.right = Matrix[0][0];
 
    // link header left pointer to column 
    // header of last column 
    header.left = Matrix[0][nCol-1];
 
    Matrix[0][0].left = header;
    Matrix[0][nCol-1].right = header;
    return header;
}

void cover(Node targetNode)
{
     Node row, rightNode;
 
    // get the pointer to the header of column
    // to which this node belong 
    Node colNode = targetNode.column;
 
    // unlink column header from it's neighbors
    colNode.left.right = colNode.right;
    colNode.right.left = colNode.left;
 
    // Move down the column and remove each row
    // by traversing right
    for(row = colNode.down; row != colNode; row = row.down)
    {
        for(rightNode = row.right; rightNode != row;
            rightNode = rightNode.right)
        {
            rightNode.up.down = rightNode.down;
            rightNode.down.up = rightNode.up;
 
            // after unlinking row node, decrement the
            // node count in column header
            Matrix[0][rightNode.colID].nodeCount -= 1;
        }
    }
}
 

void uncover(Node targetNode)
{
    Node rowNode, leftNode;
 
    // get the pointer to the header of column
    // to which this node belong 
    
	Node colNode = targetNode.column;
 
    // Move down the column and link back
    // each row by traversing left
    for(rowNode = colNode.up; rowNode != colNode; rowNode = rowNode.up)
    {
        for(leftNode = rowNode.left; leftNode != rowNode;
            leftNode = leftNode.left)
        {
            leftNode.up.down = leftNode;
            leftNode.down.up = leftNode;
 
            // after linking row node, increment the
            // node count in column header
            Matrix[0][leftNode.colID].nodeCount += 1;
        }
    }
 
    // link the column header from it's neighbors
    colNode.left.right = colNode;
    colNode.right.left = colNode;
}

Node getMinColumn()
{
    Node h = header;
    Node min_col = h.right;
    h = h.right.right;
    do
    {
        if(h.nodeCount < min_col.nodeCount)
        {
            min_col = h;
        }
        h = h.right;
    }while(h != header);
 
    return min_col;
}

void printSolutionsNow()
{
	flag++;
      System.out.println("Printing solutions");
    //cout<<"Printing Solutions: ";
}

void solve(int k)
{
	
	if(flag>=2) return;
    Node rowNode;
    Node rightNode;
    Node leftNode;
    Node column;
    // if no column left, then we must
    // have found the solution
    if(header.right == header  && flag<2)
    { 
        printSolutionsNow();
        return;
    }
 
	
	column = getMinColumn();
    cover(column);
 
    for(rowNode = column.down; rowNode != column; 
        rowNode = rowNode.down )
    {
       // solutions.add(rowNode);
       solutions[index++]=rowNode;
        for(rightNode = rowNode.right; rightNode != rowNode;
            rightNode = rightNode.right)
            cover(rightNode);
 
        // move to level k+1 (recursively)
        search(k+1);
 
        // if solution in not possible, backtrack (uncover)
        // and remove the selected row (set) from solution
       // solutions.remove(solutions.size()-1);
       index--;
       
        column = rowNode.column;
        for(leftNode = rowNode.left; leftNode != rowNode;
            leftNode = leftNode.left)
            uncover(leftNode);
    }
 
    uncover(column);
}

int sol[][] = new int[9][9];
void printSolutions()
{
	flag++;
   System.out.println("Printing solutions");

    //int sol[9][9];
    for(int a1=0;a1<9;a1++)
    for(int b1=0;b1<9;b1++)
     sol[a1][b1]=0;
    /*
     for(int i = 0; i<solutions.size(); i++)
        {int d=solutions.get(i).rowID;*/
    for(int i = 0; i<index; i++)
        {//int d=solutions.get(i).rowID;
            int d=solutions[index-1].rowID;
		System.out.println(d+ " ");
		d--;
		int r=d%9;
        sol[d/81][(d%81)/9]=r+1;
	    }
	System.out.println("");
    for(int a1=0;a1<9;a1++){
    for(int b1=0;b1<9;b1++)
     System.out.println(sol[a1][b1]+" "); 
    System.out.println(" ");
    }
 System.out.println();

}

int y=0;
// Search for exact covers
void search(int k)
{
	
	if(flag>=100) return;
     Node rowNode;
     Node rightNode;
    Node leftNode;
    Node column;
    // if no column left, then we must
    // have found the solution
    if(header.right == header  && flag<100)
    { 
        printSolutions();
        return;
    }
 
	
	column = getMinColumn();
    cover(column);
 
    for(rowNode = column.down; rowNode != column; 
        rowNode = rowNode.down )
    {
        //solutions.add(rowNode);
          solutions[index++]=rowNode;
          
        for(rightNode = rowNode.right; rightNode != rowNode;
            rightNode = rightNode.right)
            cover(rightNode);
 
        // move to level k+1 (recursively)
        search(k+1);
        // if solution in not possible, backtrack (uncover)
        // and remove the selected row (set) from solution
       // solutions.remove(solutions.size()-1);
       index--;
        if(flag>=100 && c<20){c++;return;}
		if(flag>=100 && c==20){

	//solutions.size() in place of index in next line
		for(int i = 0; i<index; i++)
        {//Node x=solutions.get(i);
	   Node x = solutions[index];
            int d = x.rowID;
	    //	cout<<d<<" ";
		d--;
		int r=d%9;
        target[d/81][(d%81)/9]=r+1;
	    }
		
		return;
		}
        
        column = rowNode.column;
        for(leftNode = rowNode.left; leftNode != rowNode;
            leftNode = leftNode.left)
            uncover(leftNode);
    }
 
    uncover(column);
}
}
class sudokuGenerator{
public static void main(String args[]) throws  IOException 
{    
    dll ob = new dll();
    ob.flag=0;
    ob.nRow = 729;
    ob.nCol = 9*9*4;
    ob.target = new int[9][9];
    ob.initMat();
   // ob.Matrix= new dll.Node[ob.MAX_ROW][ob.MAX_COL]; 
    ob.ProbMat = new boolean[ob.MAX_ROW][ob.MAX_COL];
   // ob.Matrix = new dll.Node[ob.MAX_ROW][ob.MAX_COL]; 
    ob.solutions = new dll.Node[81];
    // initialize the problem matrix 
    // ( matrix of 0 and 1) with 0
    for(int i=0; i<ob.nRow; i++)
    {
        for(int j=0; j<ob.nCol; j++)
        {
            // if it's row 0, it consist of column
            // headers. Initialize it with 1
            if(i == 0) ob.ProbMat[i][j] = true;
            else ob.ProbMat[i][j] = false;
        }
    }
  
   int ct=1; 
   for(int i=0;i<9;i++)
   {
   	for(int j=0;j<9;j++)
   	{   //[i,j]
   	    	for(int k=0;k<9;k++)  //what in i,j 1/2
   	    	{
   	    		for(int l=0;l<ob.nCol;l++)
   	    		{
   	    		    //first 81 columns
					if(l<81){
					ob.ProbMat[ct][9*i+j]=true;
					       }
				    //next 4     	
				    else if(l>=81 && l<162){
					          ob.ProbMat[ct][81+9*k+j]=true;
						   }
					//next 4
				    else if(l>=162 && l<243){
				    	      ob.ProbMat[ct][162+9*k+i]=true;
					       } 
				    else   {  if(i<3)
				    	      ob.ProbMat[ct][243+9*k+j/3]=true;
				    	      else if(i<6)
				    	      ob.ProbMat[ct][243+9*k+3+j/3]=true;
				    	      else
				    	      ob.ProbMat[ct][243+9*k+6+j/3]=true;
					       }
				   
				}ct++;
			}
	}
   }
   ob.c=0;
   int i,j;
    ob.createToridolMatrix();
    for(i=0;i<9;i++)
    for(j=0;j<9;j++)
    ob.target[i][j]=0;
    ob.search(0);
	//cout<<"ok\n\n";
	for(i=0;i<9;i++)
   {
     for(j=0;j<9;j++)
     System.out.println(ob.target[i][j]+ " ");
     System.out.println();}
//	generator();
//	return 0;
}



}
