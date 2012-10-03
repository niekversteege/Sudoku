package nl.niek.minor.aa.sudoku;

public class SudokuPrinter
{
	public SudokuPrinter()
	{
		
	}
	
	public void printSudokuField(final int[][] sudokuField, final int blockSize)
	{		
		printLine();
		for (int i = 0; i < sudokuField.length; i++)
		{	
			if (i % blockSize == 0)
			{
				printLine("*******************************");
			}
			for (int j = 0; j < sudokuField[i].length; j++)
			{
				if (j % blockSize == 0)
				{
					print("|");
				}
				printNumber(sudokuField[i][j]);
			}
			print("|");
			printLine();
		}
		printLine("*******************************");
		printLine();
	}
	
	public void printNumber(Integer number)
	{
		print(" " + number.toString() + " ");
	}
	
	private void print(String string)
	{
		System.out.print(string);
	}

	public void printLine()
	{
		printLine("");
	}
	
	public void printLine(String toPrint)
	{
		System.out.println(toPrint);
	}
}
