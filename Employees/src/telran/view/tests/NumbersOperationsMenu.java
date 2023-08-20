package telran.view.tests;
import java.util.ArrayList;
import java.util.Arrays;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class NumbersOperationsMenu {
public static Item getOperationsItem(String name) {
	return Item.of(name, io -> {
		Item [] items = {
		Item.of("Add two numbers", NumbersOperationsMenu::addTwoNumbers),
		Item.of("Subtract two numbers", NumbersOperationsMenu::subtractTwoNumbers),
		Item.of("Divide two numbers", NumbersOperationsMenu::divideTwoNumbers),
		Item.of("Multiply two numbers", NumbersOperationsMenu::multiplyTwoNumbers),
		Item.ofExit()
	};
		Menu menu = new Menu(name, items);
		menu.perform(io);
	});
	
	
	
}
static int[] getTwoNumbers(InputOutput io) {
	int firstNumber = io.readInt("Enter first number", "no number");
	int secondNumber = io.readInt("Enter second number","no number");
	return new int[] {firstNumber, secondNumber};
}
static void addTwoNumbers(InputOutput io) {
	int [] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] + numbers[1]);
}
static void subtractTwoNumbers(InputOutput io) {
	int [] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] - numbers[1]);
}
static void divideTwoNumbers(InputOutput io) {
	int [] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] / numbers[1]);
}
static void multiplyTwoNumbers(InputOutput io) {
	int [] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] * numbers[1]);
}
}
