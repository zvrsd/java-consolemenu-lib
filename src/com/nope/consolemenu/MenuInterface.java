package com.nope.consolemenu;

import com.nope.utilslib.LogTool;
import com.nope.utilslib.Util;

/**
 * Command Line Interface Class mainly designed to allow and manage interactions
 * between user and menu
 * <br>
 * Displays a menu and its elements Allows the user to navigate through them
 * Executes defined method when an element is selected<br>
 * <br>
 * First you need to create an instance of this class :<br>
 * MenuInterface menu = new MenuInterface();<br>
 * <br>
 * After that, you have to create and set an instance of MenuElement as the root
 * element of this menu :<br>
 * MenuElement element = new MenuElement("Main Menu");<br>
 * element.add(new MenuElement("SubMenu"); menu.setRootElement(element);<br>
 * <br>
 * Then you can run your menu like this :<br>
 * menu.displayMenu(); // Displays the menu at the currently selected
 * element<br>
 * menu.selectElement("2"); // Selects that element to be the current one<br>
 * menu.displayMenu();<br>
 * <br>
 *
 * @author zvr
 */
public class MenuInterface {

    private final static String LAYOUT_SYNTAX = "{?}[ID][NAME] (CHILDREN QTY) (CHILDREN IDs)";

    /**
     * Used to setRootElement Elements IDs
     */
    private int count = 0;  // Used to set Elements IDs

    private ElementMap elements;
    private MenuElement mainElement;
    private MenuElement currentElement;

    //private final int INDENT = 2;
    private char backSelectionID;
    private char mainMenuSelectionID;

    //Permits to switch from any element to any other
    private boolean indirectSelectionAllowed;

    //Shows an element that will allow to go back to the previous element
    private boolean backSelectionAllowed;

    //Shows an element that will allow to go to the main element
    private boolean mainMenuSelectionAllowed;

    Util util;
    LogTool logTool;

    /**
     * @deprecated Use MenuInterface() and setRootElement(element) instead
     * @see MenuInterface()
     * @see setRootElement(MenuElement element)
     * @param elm The HashMap containing MenuElements
     */
    public MenuInterface(ElementMap elm) {
        elements = elm;
        init();
    }

    /**
     * @deprecated Use MenuInterface() and setRootElement(element) instead
     * @see MenuInterface()
     * @see setRootElement(MenuElement element)
     * @param e Root MenuElement
     */
    public MenuInterface(MenuElement e) {
        this();
        currentElement = mainElement = e;
        init();
    }

    public MenuInterface() {
        elements = new ElementMap();
        init();
    }

    /**
     * Sets element as MenuInterface's root MenuElement
     *
     * @param element
     */
    public void setRootElement(MenuElement element) {
        currentElement = mainElement = element;
    }

    private void init() {

        MenuElement.setMenuInterface(this);

        backSelectionID = 'B';
        mainMenuSelectionID = 'M';

        indirectSelectionAllowed = false;
        backSelectionAllowed = true;
        mainMenuSelectionAllowed = true;

        logTool = LogTool.getInstance();
        util = Util.getInstance();
    }

    /**
     * Selects a MenuElement from the user input's string and executes something
     * if has to
     *
     * @param choice The element id to choose
     */
    public void selectElement(String choice) {
        choice = choice.toUpperCase();
        int elementID = -1;

        util.clear();

        if (choice.length() == 0){
            return;
        }
        // If back choice
        if (choice.charAt(0) == backSelectionID && currentElement != mainElement) {
            //elementID = 
            currentElement = elements.get(currentElement.getParentId());
            return;
        } // If main menu choice
        else if (choice.charAt(0) == mainMenuSelectionID) {
            currentElement = mainElement;
            return;
        } else {
            try {
                elementID = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                util.display("Invalid choice");
                util.pressEnter();
                return;
            }
        }

        // Checks if the id exists at all
        if (!elements.keySet().contains(elementID)) {
            logTool.E("MenuElement with id " + elementID + " does not exisit");
            util.pressEnter();
            return;
        }

        logTool.V("MenuElement with id " + elementID + " (" + elements.get(elementID).getName() + ") does exist");

        boolean isElementValid = false;
        // If indirect selection is not allowed we have to check if
        for (int id : currentElement.getChildren()) {

            if (id == elementID) {
                isElementValid = true;
                break;
            }
        }
        if (!isElementValid) {
            logTool.E("You can only select displayed elements");
            util.pressEnter();
            util.clear();
            return;
        }

        currentElement = elements.get(elementID);

        // If the element has executor
        if (currentElement.getMethodExecutor() != null) {
            //execute
            currentElement.getMethodExecutor().execute();
            util.pressEnter();
            util.clear();
        }

        // If the current element has no children
        if (currentElement.getChildrenQty() == 0) {
            // Make the parent element as the new current
            currentElement = elements.get(currentElement.getParentId());
        }
    }

    /**
     * @deprecated @see selectElement(String choice)
     * @param choice
     */
    public void selectElementOld(String choice) {

        util.clear();
        int id;

        // Prevents selection of non-displayed MenuElements
        boolean bypassIndirectSelection = indirectSelectionAllowed;

        if (choice.toUpperCase().charAt(0) == backSelectionID) {
            id = elements.get(currentElement.getParentId()).getId();
            bypassIndirectSelection = true;
        } else if (choice.toUpperCase().charAt(0) == mainMenuSelectionID) {
            id = mainElement.getId();
            bypassIndirectSelection = true;
        } else {
            id = Integer.parseInt(choice);
        }

        int currentElementChildrenQty = currentElement.getChildrenQty();
        // If we can not swap to any menu element, we have
        // to check that the id is one the currentelement's childs
        if (!bypassIndirectSelection) {
            int i = 0;
            while (i < currentElementChildrenQty) {

                if (id == currentElement.getChildren()[i]) {
                    i = currentElementChildrenQty + 1;
                }
                i++;
            }
            if (i == currentElementChildrenQty) {
                logTool.W("Can not select indirect elements.");
                return;
            }
        }
        //If the id does match anything
        if (elements.get(id) != null) {

            currentElement = elements.get(id);
            MethodExecutor currentElementExecutor = currentElement.getMethodExecutor();

            // If the current element has no child to display and no associated 
            // MethodExecutor then go back
            if (currentElementExecutor == null && currentElementChildrenQty == 0) {
                logTool.I(currentElement.getName() + " has nothing to display.");
                currentElement = elements.get(currentElement.getParentId());
            }

            // Executes element's method
            if (currentElementExecutor != null) {
                currentElementExecutor.execute();
                // Asking user to press enter then go back
                util.pressEnter();
                selectElement("" + backSelectionID);
            }
        }
        logTool.D("currently selected : " + currentElement.getName());
    }

    public void displayMenu() {

        if (elements.isEmpty()) {
            logTool.I("Nothing to display.");

        } else {

            MenuElement childE = null;
            util.display("== " + currentElement.getName() + " ==\n");

            //Loop throught all his childs
            for (int i = 0; i < currentElement.getChildrenQty(); i++) {
                childE = elements.get(currentElement.getChildren()[i]);
                util.display("[" + childE.getId() + "] " + childE.getName());
            }

            String s = "";

            if (backSelectionAllowed && currentElement != mainElement) {
                s += "\n[" + backSelectionID + "] " + elements.get(currentElement.getParentId()).getName() + "   ";
            }
            if (mainMenuSelectionAllowed && currentElement != mainElement && !currentElement.isChildOf(mainElement)) {
                s += "[" + mainMenuSelectionID + "] " + mainElement.getName();
            }

            util.display(s);
        }
    }

    /**
     * Returns the current Menu layout into a String
     *
     * @return The String containing the Menu Layout
     */
    public String getDisplayableLayout() {
        return LAYOUT_SYNTAX + "\n" + getElementLayout(mainElement);
    }

    /**
     * Returns a MenuElement layout including all its children
     *
     * @return The String containing the element's layout
     */
    private String getElementLayout(MenuElement element) {

        String elementsDetails = "";
        String extra = new String();
        String blankSpace = "";

        // Gets each child's ID to be shown near his parent
        for (int i = 0; i < element.getChildrenQty(); i++) {
            elementsDetails += element.getChildren()[i] + " ";
            extra += getElementLayout(getElements().get(element.getChildren()[i]));
        }

        // Indents each child
        if (MenuElement.INDENT_CHILDS) {
            for (int i = 0; i < element.getDisplayOffset() * MenuElement.CHILD_INDENT_SPACE; i++) {
                blankSpace += " ";
            }
        }

        return blankSpace
                + "{" + element.getDisplayOffset() + "}"
                + "[" + element.getId() + "]"
                + "[" + element.getName() + "] "
                + "(" + element.getChildrenQty() + ") "
                + "(" + elementsDetails + ")\n"
                + extra;
    }

    /**
     * Finds and returns a MenuElement matching the given ID
     *
     * @param id The MenuElement's id
     * @return The MenuElement that match the given id
     */
    public MenuElement getElementById(int id) {
        return elements.get(id);
    }

    /**
     * Finds and returns a MenuElement matching the given name
     *
     * @param name The MenuElement's name
     * @return The MenuElement that match the given name
     */
    public MenuElement getElementByName(String name) {
        return elements.getByName(name);
    }

    /**
     * Returns a HashMap containing all the MenuElements
     *
     * @return The HashMap that contains all this MenuInterface's Elements
     */
    public ElementMap getElements() {
        return elements;
    }

    /**
     * Simply return a new integer value to be set on some MenuElement
     *
     * @return incremented integer value
     */
    public int obtainId() {
        return ++count;
    }
}
