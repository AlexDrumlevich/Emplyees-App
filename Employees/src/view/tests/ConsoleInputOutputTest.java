package view.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.Line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import view.ConsoleInputOutput;

class ConsoleInputOutputTest {
ConsoleInputOutput io = new ConsoleInputOutput();
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	
	void testReadObject() {
		int[] numbers = io.readObject("Enter two integer numbers separated by #",
				"Must be two numbers", str -> {
					String[] numbersStr = str.split("#");
					return new int[] {Integer.parseInt(numbersStr[0]),
							Integer.parseInt(numbersStr[1])
					};
				});
		io.writeLine("Sum of entered numbers is " + (numbers[0] + numbers[1]));
	}

	@Test
	void testReadInt() {
		int number = io.readInt("Enter any integer number", "It's no an integer number");
		io.writeLine("Entered integer number is " + number);
	}

	@Test
	void testReadIntRange() {
		int number = io.readInt("Enter integer number", "Wrong number", 10, 20);
		io.writeLine("Entered number is " + number);
	}

	@Test
	void testReadLong() {
		long number = io.readLong("Enter any long number", "It's no a long number");
		io.writeLine("Entered long number is " + number);
	}

	@Test
	void testReadLongRange() {
		long number = io.readLong("Enter long number", "Wrong number",
				Integer.MAX_VALUE, Long.MAX_VALUE);
		io.writeLine("Entered long number is " + number);
	}

	@Test
	void testReadStringPredicate() {
		String line = io.readString("Enter string with 4 symbols",
				"Must be 4 symbols", str -> str.length() == 4);
		io.writeLine("Entered string is " + line);
	}

	@Test
	void testReadStringOptions() {
		String line = io.readString("Enter either apple or orange",
				"Neither apple nor orange", new HashSet<>
		(Arrays.asList("apple", "orange")));
		io.writeLine("Entered string is " + line);
	}

	@Test
	void testReadDate() {
		LocalDate date = io.readDate("Enter any date in ISO format", "It's no a date");
		io.writeLine("Entered date is " + date);
	}

	@Test
	void testReadDateRange() {
		LocalDate date = io.readDate("Enter any date in the past",
				"Wrong past date", LocalDate.MIN, LocalDate.now().minusDays(1));
		io.writeLine("Entered date is " + date);
	}

	@Test
	void testReadDouble() {
		double number = io.readDouble("Enter any number", "It's no a number");
		io.writeLine("Entered number is " + number);
	}

}