package telran.view.tests;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import telran.view.*;

public class DateOperationsMenu {
	static String format = "d/M/y";
public static Item getDateOperationsItem(String name) {
	return Item.of(name, io -> {
		Item items[] = {
			Item.of("Date after a given number of days", DateOperationsMenu::dateAdding),
			Item.of("Date before a given number of days", DateOperationsMenu::dateSubtracting),
			Item.of("Days between two dates", DateOperationsMenu::daysBetween),
			Item.ofExit()
		};
		Menu menu = new Menu(name, new ArrayList<Item>(Arrays.asList(items)));
		menu.perform(io);
	}); 
}
static private void dateAfter(InputOutput io, boolean isSubtract) {
	
	LocalDate date = io.readDate("Enter date in ISO format ", "Wrong Date " );
	int days = io.readInt("Enter number of days", "Wrong number of days", 1, Integer.MAX_VALUE);
	if (isSubtract) {
		days = -days;
	}
	io.writeLine(date.plusDays(days));
}
static void dateAdding(InputOutput io) {
	dateAfter(io, false);
}
static void dateSubtracting(InputOutput io) {
	dateAfter(io, true);
}
static void daysBetween(InputOutput io) {
	LocalDate date1 = io.readDate("Enter first Date", "Wrong Date " );
	LocalDate date2 = io.readDate("Enter second Date", "Wrong Date  " );
	io.writeLine(ChronoUnit.DAYS.between(date1, date2));
	
}
}
