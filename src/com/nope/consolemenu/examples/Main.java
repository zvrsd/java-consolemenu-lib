package com.nope.consolemenu.examples;

import com.nope.consolemenu.MenuElement;
import com.nope.consolemenu.MenuInterface;
import com.nope.utilslib.LogTool;
import com.nope.utilslib.Util;
import com.nope.consolemenu.MethodExecutor;

/**
 * 
 * @author zvr
 */
public class Main{
	
	//private Scanner input;
	private MenuInterface menu;
    
    private Util util;
    private LogTool logTool;
	
	public Main(){

        util = Util.getInstance();
        logTool = LogTool.getInstance();
        logTool.setLevel(LogTool.DEBUG);
		setup();
	}
	
    /**
     * Set the menu layout
     * 
     */
	public void setup(){
		
		// Menus and submenus layout
		menu = new MenuInterface();
        
        menu.setRootElement(
            new MenuElement("Main Menu").add(

                new MenuElement("Play").add(
                    new MenuElement("New Game"),
                    new MenuElement("Load Game")
                ),

                new MenuElement("Settings").add(
                    new MenuElement("Sound").add(
                        new MenuElement("SoundTest")
                    ),
                    new MenuElement("Graphics").add(
                        new MenuElement("GraphicsTest")
                    ),
                    new MenuElement("Controls").add(
                        new MenuElement("ControlsTest")
                    )
                ),

                new MenuElement("Other").add(
                    new MenuElement("ItemEditor"),
                    new MenuElement("DevMenu"),
                    new MenuElement("Display Menu Layout"),
                    new MenuElement("???")
                )
            )
		);
        
		// Defining what will be executed when user selects this element
		menu.getElementByName("Display Menu Layout").setMethodExecutor(new MethodExecutor(){
				public void execute(){
				}
		});
		menu.getElementByName("???").setMethodExecutor(new MethodExecutor(){
				public void execute(){
                    test();
				}
		});
		menu.getElementByName("ItemEditor").setMethodExecutor(new MethodExecutor(){
				public void execute(){
				}
		});

		util.display(menu.getDisplayableLayout());
	
	}
	/*
	 * Utility methods used in MenuElements
	 */
	private void test(){
        util.display("hello world");
	}
	   
	// Display the main menu
	private void mainMenu(){

		util.display("\n_..---====*****[ Kraft ]*****====---.._\n");
		menu.displayMenu();
		menu.selectElement(util.inputString(""));

	}
	
	public static void main(String[] args){
		
		Main m = new Main();
		
		// Main Program Loop
		while(true){
			m.mainMenu();
		}
	}
}