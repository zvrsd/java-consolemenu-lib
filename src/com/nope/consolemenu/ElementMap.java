package com.nope.consolemenu;

import java.util.*;

/**
 * A Map that uses MenuElement id as key and MenuElement as value
 * It allows to get a MenuElement from itself by using the MenuElement's name
 * 
 * @author zvr
 */
public class ElementMap extends HashMap<Integer,MenuElement>{
	
	public MenuElement getByName(String name){
		
        for(MenuElement element : values()){
            if(element.getName().equals(name)){
                return element;
            }
        }
        return null;
	}
}
