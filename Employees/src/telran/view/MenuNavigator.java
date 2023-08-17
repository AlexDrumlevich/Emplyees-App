package telran.view;

import java.util.HashMap;
import java.util.Stack;

import javax.swing.Popup;

public class MenuNavigator {

	
	private	Stack<Menu> menuNavigationStack;
	
	//keep MenuNavigators for different (not connecting) menus, witch present at the same time
	static private HashMap<String, MenuNavigator> menus = new HashMap<>();
	
	//push 
	public static Menu push(Menu menu) {
		return getMenuMavigator(menu).getMenuNavigatioStack().push(menu);
	}
	
	public static Menu peek(Menu menu) {
		return getMenuMavigator(menu).getMenuNavigatioStack().peek();
	}
	
	//pop and remove menu from  menuNavigationStack if it is empty
	public static Menu pop(Menu menu) {
		Menu menuRes = getMenuMavigator(menu).getMenuNavigatioStack().pop();
		if(getMenuMavigator(menu).getMenuNavigatioStack().isEmpty()) {
			menus.remove(menu.getId());
		}
		return menuRes;
	}

	public static boolean isEmpty(Menu menu) {
		boolean res = getMenuMavigator(menu).getMenuNavigatioStack().isEmpty();
		if(res)
			menus.remove(menu.getId());
		return res;
	}
	
	//sub menu gets present instance for menu tree using id
	static private MenuNavigator getMenuMavigator(Menu menu) {
		return menus.getOrDefault(menu.getId(), menus.put(menu.getId(), new MenuNavigator()));
	}
	
	private MenuNavigator() {
		menuNavigationStack = new Stack<>();
	}
	
	private Stack<Menu> getMenuNavigatioStack() {
		return menuNavigationStack;
	}
	

}
