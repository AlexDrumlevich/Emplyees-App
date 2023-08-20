package telran.view.tests;


import telran.view.*;

public class ViewTestAppl {

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		
		Menu menu = new Menu("Operations", getItems());

		menu.perform(io);

	}

	private static Item[] getItems() {
		Item items[] = {
			NumbersOperationsMenu.getOperationsItem( "Number Operations"),
			DateOperationsMenu.getDateOperationsItem("Date Operations"),
			Item.ofExit()
		};
		return items;
	}

}
