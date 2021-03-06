package nl.niek.minor.aa.sudoku;

import java.util.ArrayList;
import java.util.List;

public class Sudoku
{
	private static final int		BOARD_SIZE		= 9;

	private static final int		BOX_SIZE		= 3;

	private int[][]					sudokuBoard;
	private int[][]					existingBoard	= null;
	private ArrayList<Integer>[][]	possibleOptions;

	private boolean					solved			= false;

	private SudokuPrinter			printer;

	private boolean					unsolvable		= false;

	public Sudoku()
	{
		sudokuBoard = new int[BOARD_SIZE][BOARD_SIZE];
		possibleOptions = new ArrayList[BOARD_SIZE + 1][BOARD_SIZE + 1];
		printer = new SudokuPrinter();
	}

	public Sudoku(final int[][] existingBoard)
	{
		this();
		this.existingBoard = existingBoard;
	}

	public void go()
	{
		initBoard();
		printer.printLine("Before:");
		printer.printSudokuField(sudokuBoard, BOX_SIZE);
		preSolve();

		if (unsolvable)
		{
			printer.printLine("Sudoku is not solvable.");
		}
		else
		{
			solve();
			printer.printLine("After:");
			printer.printSudokuField(sudokuBoard, BOX_SIZE);
		}
	}

	private void initBoard()
	{
		initPossibleOptions();

		if (existingBoard == null)
		{
			// use default board
			sudokuBoard[0][2] = 4;
			sudokuBoard[0][3] = 3;
			sudokuBoard[0][5] = 2;
			sudokuBoard[0][6] = 1;

			sudokuBoard[1][1] = 6;
			sudokuBoard[1][7] = 2;

			sudokuBoard[2][0] = 9;
			sudokuBoard[2][8] = 7;

			sudokuBoard[3][2] = 1;
			sudokuBoard[3][4] = 3;
			sudokuBoard[3][6] = 2;
			
			sudokuBoard[4][0] = 2;
			sudokuBoard[4][3] = 9;
			sudokuBoard[4][5] = 6;
			sudokuBoard[4][8] = 1;
			
			sudokuBoard[5][2] = 9;
			sudokuBoard[5][4] = 5;
			sudokuBoard[5][6] = 4;
			
			sudokuBoard[6][0] = 7;
			sudokuBoard[6][8] = 3;
			
			sudokuBoard[7][1] = 1;
			sudokuBoard[7][7] = 4;
			
			sudokuBoard[8][2] = 8;
			sudokuBoard[8][3] = 5;
			sudokuBoard[8][5] = 3;
			sudokuBoard[8][6] = 9;
		}
		else
		{
			// use existing board
			// copy existing board into sudokuBoard
			sudokuBoard = existingBoard;
		}

		if (unsolvable)
		{
			printer.printLine("Sudoku is unsolvable");
		}
	}

	private void initPossibleOptions()
	{
		for (int i = 0; i < BOARD_SIZE; i++)
		{
			for (int j = 0; j < BOARD_SIZE; j++)
			{
				possibleOptions[i][j] = new ArrayList<Integer>();
			}
		}
	}

	private void solve()
	{
		solve(0, 0);
	}

	/**
	 * Visit every tile. For every option that tile has, try it. If it doesn't
	 * work: try the next. If none work: set to zero.
	 * 
	 * @param row
	 * @param column
	 */
	private void solve(int row, int column)
	{
		if (column == BOARD_SIZE)
		{
			column = 0;
			row++;
		}
		if (row == BOARD_SIZE || solved || unsolvable)
		{
			solved = true;
			return;
		}
		if (sudokuBoard[row][column] == 0)
		{
			for (Integer number : possibleOptions[row][column])
			{
				if (isPossible(row, column, number) && !solved)
				{
					sudokuBoard[row][column] = number;
					// recursive call
					solve(row, column + 1);
				}
			}
			if (!solved)
			{
				sudokuBoard[row][column] = 0;
			}
		}
		else
		{
			solve(row, column + 1);
		}
	}

	/**
	 * Check the whole field for the possibilities per numberbox. If a numberbox
	 * without possibilities is found: sets unsolvable to true;
	 */
	private void preSolve()
	{
		for (int row = 0; row < BOARD_SIZE; row++)
		{
			for (int col = 0; col < BOARD_SIZE; col++)
			{
				if (sudokuBoard[row][col] == 0)
				{
					List<Integer> possibilitiesForBox = getPossibleOptions(row,
							col);
					if (possibilitiesForBox.isEmpty())
					{
						unsolvable = true;
						return;
					}
					possibleOptions[row][col].addAll(possibilitiesForBox);
				}
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

	/**
	 * Does the given row already have number in it?
	 * 
	 * @param row
	 * @param number
	 * @return
	 */
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

	/**
	 * Does the given col(umn) already have number in it?
	 * 
	 * @param col
	 * @param number
	 * @return
	 */
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
