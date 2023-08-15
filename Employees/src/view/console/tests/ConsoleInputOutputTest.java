package view.console.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.Line;

import org.junit.jupiter.api.Test;

import view.console.ConsoleInputOutput;

class ConsoleInputOutputTest {

	ConsoleInputOutput consolInputOutput = new ConsoleInputOutput();
	
	@Test
	void testWriteString() {
		String string1 = "Test string 1 ";
		String string2 = "Test string 2 ";
		consolInputOutput.write(string1);
		consolInputOutput.write(string2);
	}
	
	@Test
	void testWriteLine() {
		String line1 = "Test line 1 ";
		String line2 = "Test line 2 ";
		consolInputOutput.writeLine(line1);
		consolInputOutput.writeLine(line2);
	}
	
	@Test
	void testPrintString() {
		String string = "Test string";
		consolInputOutput.readString(string);
	}
	
	@Test
	void testReadString() {
		String prompt = "enter yor name";
		consolInputOutput.readString(prompt);
	}

	@Test
	void testReadStringPredicate() {
		String prompt = "enter a word starts \"R\"";
		String errorPrompt = "The entered word does not start from \"R\"";
		consolInputOutput.readString(prompt, errorPrompt, s -> s.startsWith("R")).startsWith(("R"));
	}
	
	@Test
	void testReadStringOptions() {
		Set<String> options = new HashSet<>();
		options.add("Java");
		options.add("C++");
		options.add("C");
		options.add("Swift");
		options.add("Piton");
		String prompt = "enter a word matchs to options";
		String errorPrompt = "The entered word does not match to options";
		
		//enter value from set
		assertTrue(
			options.contains(consolInputOutput.readString(prompt, errorPrompt, options))
		);
	}
	
	@Test
	void testReadInt() {
		
		String prompt = "enter an int number";
		String errorPrompt = "was entered not a number";
		
		consolInputOutput.readInt(prompt, errorPrompt);
	}
	
	@Test
	void testReadIntMinMax() {
		
		String prompt = "enter an int number";
		String errorPrompt = "entered data is not correct";
		int min = 100;
		int max = 199;

		consolInputOutput.readInt(prompt, errorPrompt, min, max);
	}
	
	@Test
	void testReadLong() {
		
		String prompt = "enter an value long type";
		String errorPrompt = "was entered not a number";
		
		consolInputOutput.readLong(prompt, errorPrompt);
	}
	
	@Test
	void testReadLongMinMax() {
		
		String prompt = "enter an long number";
		String errorPrompt = "entered data is not correct";
		int min = 100;
		int max = 199;

		consolInputOutput.readLong(prompt, errorPrompt, min, max);
	}
	
	@Test
	void testReadDouble() {
		
		String prompt = "enter an value type double";
		String errorPrompt = "entered data is not correct";
		
		consolInputOutput.readDouble(prompt, errorPrompt);
		
	}
	
	@Test
	void testReadLocalData() {
		
		String prompt = "enter a date in format YYYY-mm-dd";
		String errorPrompt = "entered data is not correct";
		
		consolInputOutput.readDate(prompt, errorPrompt);
	}
	
	@Test
	void testReadLocalDataFromTo() {
		
		String prompt = "enter a date in format YYYY-mm-dd";
		String errorPrompt = "entered data is not correct";
		consolInputOutput.readDate(
					prompt,
					errorPrompt,
					LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(1)
		);
	}
	
}
