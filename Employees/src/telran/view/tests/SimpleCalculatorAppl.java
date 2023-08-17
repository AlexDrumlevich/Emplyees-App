package telran.view.tests;

import java.time.LocalDate;
import java.time.Period;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import telran.view.*;

public class SimpleCalculatorAppl {

	
	Stack<Menu> stackMenu = new Stack<>();
	
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Operations", getItemsOperationsMenu());
		menu.perform(io);

	}

		
	//get items
	static Item[] getItemsOperationsMenu() {
		Item[] items = {
				new Menu("Number operations", getItemsNumderOperationsMenu()),
				new Menu("Date operations", getItemsDateOperationsMenu()),
				Item.ofExit()};
		return items;
	}
	
	static Item[] getItemsNumderOperationsMenu() {
		Item[] items = {
				Item.of("Add numbers", io -> calculate(io, (a, b) -> a + b)),
				Item.of("Subtract numbers", io -> calculate(io, (a, b) -> a - b)),
				Item.of("Multiply numbers", io -> calculate(io, (a, b) -> a * b)),
				Item.of("Divide numbers", io -> calculate(io, (a,b)->a / b)),
				Item.ofExit()
				};
		return items;
	}
	static private void calculate(InputOutput io, BinaryOperator<Double> operator) {
		double first = io.readDouble("Enter first number", "Must be any number");
		double second = io.readDouble("Enter second number", "Must be any number");
		io.writeLine(operator.apply(first, second));
	}

	
	static Item[] getItemsDateOperationsMenu() {
		Item[] items = { 
				Item.of("Date after ... days", io -> calculateDateUsingDaysAmount(io, daysAmount -> LocalDate.now().plusDays(daysAmount))),
				Item.of("Date before ... days", io -> calculateDateUsingDaysAmount(io, daysAmount -> LocalDate.now().minusDays(daysAmount))),
				Item.of("Days between dates", io -> calculateValueUsingTwoDates(io, (date1, date2) -> Period.between(date1, date2).getDays())),
				Item.ofExit()
				};
		return items;
	}
	
	static private void calculateDateUsingDaysAmount(InputOutput io, Function<Integer, LocalDate> operator) {
		int amountOfDays = io.readIntPositive("Enter amount of days", "Must be an integer positive number");
		io.writeLine(operator.apply(amountOfDays));
	}
	static private <T> void calculateValueUsingTwoDates(InputOutput io, BiFunction<LocalDate, LocalDate, T> operator) {
		LocalDate firstDate = io.readDate("Enter first date in format \"yyyy MM dd\"", "Incorrect date format");
		LocalDate secondDate = io.readDate("Enter second date in format \"yyyy MM dd\"", "Incorrect date format");
		io.writeLine(operator.apply(firstDate, secondDate));
	}

}
