package nl.niek.minor.aa.sudoku;

import java.util.ArrayList;
import java.util.List;

public class Sudoku
{
	private static final int		BOARD_SIZE	= 9;

	private static final int		BOX_SIZE	= 3;

	private int[][]					sudokuBoard;
	private boolean					solved		= false;
	private ArrayList<Integer>[][]	possibleOptions;

	public Sudoku()
	{
		sudokuBoard = new int[BOARD_SIZE][BOARD_SIZE];
		possibleOptions = new ArrayList[BOARD_SIZE + 1][BOARD_SIZE + 1];
	}

	public Sudoku(final int[][] existingBoard)
	{
		this();
		this.sudokuBoard = existingBoard;
	}

	public void go()
	{
		initBoard();
		checkPossibleNumbers();
		solve();
	}

	private void initBoard()
	{
		if (sudokuBoard == null)
		{
			// use default board
		}
		else
		{
			// use existing board
		}
	}

	private void solve()
	{
		// starter call for recursive solve funtion
		solve(0, 0);
	}

	private void solve(int row, int col)
	{
		// if we reach the end of the column, go to the next row.
		if (col == BOARD_SIZE)
		{
			col = 0;
			row++;
		}
		if (row == BOARD_SIZE)
		{
			solved = true;
			return;
		}
		if (sudokuBoard[row][col] == 0)
		{
			for (Integer number : possibleOptions[row][col])
			{
				if (isPossible(row, col, number) && !solved)
				{
					sudokuBoard[row][col] = number;
					solve(row, col + 1);
				}
			}
			if (!solved)
			{
				sudokuBoard[row][col] = 0;
			}
		}
		else
		{
			solve(row, col + 1);
		}
	}

	/**
	 * Check the whole field for the possibilities per numberbox.
	 */
	private void checkPossibleNumbers()
	{
		for (int row = 0; row < BOARD_SIZE; row++)
		{
			for (int col = 0; col < BOARD_SIZE; col++)
			{
				possibleOptions[row][col].addAll(getPossibleOptions(row, col));
			}
		}
	}

	private List<Integer> getPossibleOptions(int row, int col)
	{
		List<Integer> options = new ArrayList<Integer>();

		// For every unique number.
		for (int i = 0; i < 10; i++)
		{
			if (isPossible(row, col, i))
			{
				options.add(i);
			}
		}

		return options;
	}

	/**
	 * Check if the given number can be placed at the given indices.
	 * 
	 * @param row
	 * @param col
	 * @param number
	 * @return
	 */
	private boolean isPossible(int row, int col, int number)
	{
		// check if number is already in row
		if (!rowHasNumber(row, number))
		{
			// check if number is already in column
			if (!columnHasNumber(col, number))
			{
				// check if number is already in box
				if (!boxHasNumber(row, col, number))
				{
					return true;
				}
			}
		}

		return false;
	}

	private boolean rowHasNumber(int row, int number)
	{
		for (int i = 0; i < BOARD_SIZE; i++)
		{
			if (sudokuBoard[row][i] == number)
			{
				return true;
			}
		}

		return false;
	}

	private boolean columnHasNumber(int col, int number)
	{
		for (int i = 0; i < BOARD_SIZE; i++)
		{
			if (sudokuBoard[i][col] == number)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if the (default 3x3 in a 9x9 sudoku) box contains the number.
	 * 
	 * @param row
	 * @param col
	 * @param number
	 * @return
	 */
	private boolean boxHasNumber(int row, int col, int number)
	{
		// get the box that contains the given coordinates
		int boxRow = row - (row % BOX_SIZE);
		int boxCol = col - (col % BOX_SIZE);

		for (int i = 0; i < BOX_SIZE; i++)
		{
			for (int j = 0; j < BOX_SIZE; j++)
			{
				if (sudokuBoard[boxRow + i][boxCol + j] == number)
				{
					return true;
				}
			}
		}

		return false;
	}
}
