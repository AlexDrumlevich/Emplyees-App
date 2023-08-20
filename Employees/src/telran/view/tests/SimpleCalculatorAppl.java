package telran.view.tests;

import java.util.function.BinaryOperator;

import telran.view.*;

public class SimpleCalculatorAppl {

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Calculator", getItems());
		menu.perform(io);

	}

	static void calculate(InputOutput io, BinaryOperator<Double> operator) {
		double first = io.readDouble("Enter first number", "Must be any number");
		double second = io.readDouble("Enter second number", "Must be any number");
		io.writeLine(operator.apply(first, second));
	}

	static Item[] getItems() {
		Item[] items = { Item.of("Add numbers", io -> calculate(io, (a,b)->a + b)),
				Item.of("Subtract numbers", io -> calculate(io, (a,b)->a - b)),
				Item.of("Multiply numbers", io -> calculate(io, (a,b)->a * b)),
				Item.of("Divide numbers", io -> calculate(io, (a,b)->a / b)),
				Item.ofExit()};
		return items;
	}

	

}
