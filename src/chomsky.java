/*
Michael Wahlberg
COT 4210
Program 1: Chomsky Normal Form
*/

import java.util.*;
import java.io.*;

//Variable class to store each variable as they appear.
class Variable
{
	public Character character;
	public ArrayList<String> vRules;
	public ArrayList<Character> tRules;

	public Variable(Scanner input)
	{
		int numRules = input.nextInt();
		character = input.next().charAt(0);
		vRules = new ArrayList<String>();
		tRules = new ArrayList<Character>();

		for(int i = 0; i < numRules; i++)
		{

			String temp = input.next();

			//Terminal Rule
			if(temp.length() == 1)
				tRules.add(temp.charAt(0));
			//Variables
			else
				vRules.add(temp);
		}
	}

	//Getters to safely return variables.
	public Character getCharacter()
	{
		return character;
	}

	public ArrayList<String> getVRules()
	{
		return vRules;
	}

	public ArrayList<Character> getTRules()
	{
		return tRules;
	}
}

public class chomsky 
{
	static int numGram, numVar;
	static Variable[] varArr;

	static boolean DEBUG = true; 

	//Finds index of a character
	public static int findIndex(char character)
	{
		for(int i = 0; i < varArr.length; i++)
			if(varArr[i].getCharacter() == character)
				return i;

		//No such variable, search failed.
		return -1;
	}

/*
	Following the CYK algorithm as mentioned in class.
	Each step is explained with regards to this algorithm.
*/
	public static String isValid(String str)
	{

		//Check for epsilon in the case that the test string is the empty string.
		if(str.equals("@"))
		{
			for(int i = 0; i < numVar; i++)
				if(varArr[i].getCharacter() == 'S')
					for(Character chr : varArr[i].getTRules())
						if(chr == '@')
							return "YES";
			return "NO";
		}

		//x: length of our input string
		//y: The number of Variable objects we have.
		int x = str.length(), y = numVar;
		boolean[][][] table = new boolean[x][x][y];

		//Go through string.
		for(int i = 0; i < x; i++)
		{
			//Go through variables
			for(int j = 0; j < y; j++)
			{
				//For each Character in varArr[j] set the corresponding index of table[] to true.
				for(Character chr : varArr[j].getTRules())
				{
					if(chr == str.charAt(i))
					{
						table[i][0][j] = true;
					}
				}
			}
		}

		//This block checks the table to see if it is valid.
		for(int i = 1; i < x; i++)
		{
			//Subset of string not including i.
			for(int j = 0; j < x-i; j++)
			{
				//String up to i-1
				for(int k = 0; k < i; k++)
				{
					for(int l = 0; l < varArr.length; l++)
					{
						for(String rule : varArr[l].getVRules())
						{
							//Character to set to true.
							int arrC = findIndex(varArr[l].getCharacter());
							//Rule #1
							int rule1 = findIndex(rule.charAt(0));
							//Rule #2
							int rule2 = findIndex(rule.charAt(1));

							if(table[j][k][rule1] && table[j+k+1][i-k-1][rule2])
							{
								table[j][i][arrC] = true;
							}
						}
					}
				}
			}
		}

		int sIn = findIndex('S');

		//Determines whether or not to return YES or NO.
		if(table[0][x-1][sIn])
			return "YES";
		return "NO";
	}


	//Straight forward main method.
	public static void main(String[] args) throws FileNotFoundException
	{
		String fileName = "chomsky.in";
		File inFile = new File(fileName);
		Scanner in = new Scanner(inFile);

		numGram = in.nextInt();
		int numTests;

		
		//int numTests, numTerm;

		for(int i = 0; i < numGram; i++)
		{
			System.out.println("Grammar #" + (i+1) + ": ");

			numVar = in.nextInt();
			varArr = new Variable[numVar];

			for(int v = 0; v < numVar; v++)
				varArr[v] = new Variable(in);

			numTests = in.nextInt();
			for(int j = 0; j < numTests; j++)
			{
				String test = in.next();
				System.out.println(test + ": " + isValid(test));

			}

			System.out.println();
		}

		in.close();
	}
}