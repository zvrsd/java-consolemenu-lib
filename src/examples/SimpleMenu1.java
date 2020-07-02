package examples;

import com.nope.consolemenu.MenuElement;
import com.nope.consolemenu.MenuInterface;
import com.nope.consolemenu.MethodExecutor;
import java.util.Scanner;

/**
 * A basic Example on how to create a menu hierarchy with sub menus
 * and execute some code when specific elements are selected
 * 
 * @author zvr
 */
public class SimpleMenu1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Create new MenuInterface Object
        MenuInterface menuInterface = new MenuInterface();
        
        // Create new MenuElements that are going to populate our MenuInterface
        MenuElement mainElement = new MenuElement("Main Menu");
        
        MenuElement elementA = new MenuElement("Menu A");
        MenuElement elementA1 = new MenuElement("Menu A1");
        MenuElement elementA2 = new MenuElement("Menu A2");
        
        MenuElement elementB = new MenuElement("Menu B");
        MenuElement elementB1 = new MenuElement("Menu B1");
        
        MenuElement elementC = new MenuElement("Menu C");
        MenuElement elementC1 = new MenuElement("Menu C1");
        
        
        // Organazing the MenuElements like so :
        /*
        Main Menu
            Menu A
                Menu A1
                Menu A2
                Menu A3
            Menu B
                Menu B1
        
            Menu C
                Menu C1
        */
        mainElement.add(elementA, elementB, elementC);
        
        elementA.add(elementA1, elementA2);
        elementB.add(elementB1);
        elementC.add(elementC1);
        
        // Set the root MenuElement of this MenuInterface
        menuInterface.setRootElement(mainElement);
        
        
        
        // Create a MethodExecutor that contains some code to run into its
        // execute() method
        MethodExecutor executor = new MethodExecutor() {
            @Override
            public void execute() {
                System.out.println("This message has been successfully displayed");
            }
        };
        
        // Assign that executor to one of the MenuElement
        elementA1.setMethodExecutor(executor);
        
        
        
        // Now to display and use the menu
        Scanner s = new Scanner(System.in);
        while(true){
            menuInterface.displayMenu();
            menuInterface.selectElement(s.nextLine());
        } 
    }
}
