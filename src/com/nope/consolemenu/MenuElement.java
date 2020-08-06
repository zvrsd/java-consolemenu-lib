package com.nope.consolemenu;

/**
 * An Element to display on screen that belongs to a MenuInterface<br>
 * This can be selected by the user, and if it does have one or more children,
 * then they will be displayed by default<br>
 * If it has a MethodExecutor instance associated to it, then its
 * MethodExecutor.execute() will be invoked instead<br>
 * <br>
 * By default, a MenuElement is its own parent
 *
 * @author zvr
 */
public class MenuElement {

    // Move them to MenuInterface if possible
    final static int MAX_CHILDS = 32;
    final static boolean INDENT_CHILDS = true;
    final static int CHILD_INDENT_SPACE = 4;

    private static MenuInterface menuInterface;

    private int id;                     // Own ID
    private int parentId;               // Parent's ID
    private int displayOffset;
    private int[] children;             // Childs IDs
    private int childrenQty;            // Childs qty
    private String name;                // Name for display
    private MethodExecutor executor;    // Method to execute when selected

    public MenuElement() {
        this("?", false, null);
    }

    public MenuElement(String name) {
        this(name, false, null);
    }

    public MenuElement(String name, boolean methodOnly) {
        this(name, methodOnly, null);
    }

    public MenuElement(String name, MethodExecutor f) {
        this(name, false, f);
    }

    public MenuElement(String name, boolean methodOnly, MethodExecutor executor) {

        id = menuInterface.obtainId();
        this.name = name;
        children = new int[MAX_CHILDS];
        parentId = id;
        childrenQty = 0;
        displayOffset = 0;
        this.executor = executor;
        init();
    }

    // Prevents leaks ? 
    private void init() {
        menuInterface.getElements().put(id, this); // Leak
    }

    /**
     * Update this MenuElement's children
     *
     */
    private void updateChildren() {
        MenuElement element;

        // Get current element's children and update them
        for (int i = 0; i < childrenQty; i++) {

            element = menuInterface.getElements().get(children[i]);
            element.displayOffset = displayOffset + 1;
            element.updateChildren();
        }
    }

    /**
     * Adds one or more MenuElements into this one as children<br>
     *
     * @param elements The MenuElements to add
     * @return The MenuElement that invoked this method (this)
     */
    public MenuElement add(MenuElement... elements) {
        MenuElement element;

        for (MenuElement element1 : elements) {
            element = element1;
            element.parentId = id;
            children[childrenQty] = element.id;
            childrenQty++;
        }
        updateChildren();
        return this;
    }

    /**
     * Format this into a String
     *
     * @return The current MenuElement in String format
     */
    public String getDetails() {
        String details = "";
        //Gets each child's ID to be shown near his parent
        for (int i = 0; i < childrenQty; i++) {
            details += children[i] + " ";
        }
        return "{" + displayOffset + "}[" + id + "][" + name + "] (" + childrenQty + ") (" + details + ")";
    }

    /**
     * Executes the associated method if there is one
     */
    public void execute() {

        if (executor != null) {
            executor.execute();
        }
    }

    /**
     * Associates a MethodExecutor to this element<br>
     * Whenever this element will be selected executor.execute() will be invoked
     *
     * @param executor An instance of MethodExecutor class that will get its
     * execute() method invoked
     */
    public void setMethodExecutor(MethodExecutor executor) {
        this.executor = executor;
    }

    public MethodExecutor getMethodExecutor() {
        return executor;
    }

    /**
     * Checks if the current element is a child of parentElement
     *
     * @param parentElement The element to compare with
     * @return True if the current element is a child of parentElement
     */
    public boolean isChildOf(MenuElement parentElement) {

        for (int i = 0; i < parentElement.childrenQty; i++) {

            if (id == parentElement.children[i]) {
                return true;
            }
        }
        return false;
    }

    public static void setMenuInterface(MenuInterface menu) {
        menuInterface = menu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int[] getChildren() {
        return children;
    }

    public void setChildren(int[] children) {
        this.children = children;
    }

    public int getChildrenQty() {
        return childrenQty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplayOffset() {
        return displayOffset;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name;
    }
}
