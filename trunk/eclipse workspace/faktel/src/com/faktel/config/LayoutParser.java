package com.faktel.config;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.faktel.mvc.View;

/**
 * This is the class, which parses the layout configuration. It returns a box, containing all the boxes and views
 * and a mapping between the names and the views, which can be used for visualizing grids into the views.
 * 
 * An example XML looks like this:
 * <layout>
 *   <hbox>
 *     <view class="com.faktel.features.SimpleGrid" name="table1" width="300" height="200" />
 *     <view class="com.faktel.features.SimpleGrid" name="table2" width="200" height="200" />
 *     <vbox>
 *       <view class="com.faktel.features.PieChartView" name="pie1" width="100" height="100" />
 *       <view class="com.faktel.features.PieChartView" name="pie2" width="200" height="200"/>
 *     </vbox>
 *   </hbox>
 *   <hbox>
 *     <view class="com.faktel.features.SimpleGrid" name="total_table" />
 *   </hbox>
 * </layout>
 *   
 * 
 * @author valentinmihov
 */
public class LayoutParser extends DefaultHandler {
	private static final String HORIZONTAL_BOX = "horizontal_box";
	private static final String HBOX = "hbox";
	private static final String VBOX = "vbox";
	private static final String VERTICAL_BOX = "vertical_box";
	private static final String LAYOUT = "layout";
	
	/**
	 * The parent handler, to which we return the logic after we are finished with the layout
	 * configuration
	 */
	private DefaultHandler m_parent;
	private Box m_layout;
	private Box m_currentBox;
	private Map<String, View> m_viewMapping = new HashMap<String, View>();
	
	public LayoutParser(DefaultHandler parent) {
		m_parent = parent;
		m_layout = new Box(BoxLayout.Y_AXIS);
		m_currentBox = m_layout;
	}
	
	public Box getLayout() {
		return m_layout;
	}
	
	public Map<String, View> getViewMapping() {
		return m_viewMapping;
	}
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (name.equalsIgnoreCase(HORIZONTAL_BOX) || name.equalsIgnoreCase(HBOX)) {
			pushBox(new Box(BoxLayout.X_AXIS));
		} else if (name.equalsIgnoreCase(VERTICAL_BOX) || name.equalsIgnoreCase(VBOX)) {
			pushBox(new Box(BoxLayout.Y_AXIS));
		} else if (name.equalsIgnoreCase("view")) {
			try {
				createView(attributes);
			} catch (Exception e) {
				throw new SAXException("Error while processing view with attributes " + attributes, e);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (name.equalsIgnoreCase(LAYOUT)) {
			
			// Return the control to the parent element
			m_parent.endElement(uri, localName, name);
			return;
		}
		
		if (name.equalsIgnoreCase(HBOX) || 
		    name.equalsIgnoreCase(HORIZONTAL_BOX) || 
		    name.equalsIgnoreCase(VBOX) ||
		    name.equalsIgnoreCase(VERTICAL_BOX)) {
			popBox();
		}
	}
	
	/**
	 * Creates a view from given arguments. Instantiates the view through reflection. There are two mandatory arguments here:
	 * name and class. The names defines the name of the view in the app and the class describes the class of the view.
	 * 
	 * Other arguments are:
	 *   * width and height - the size of the view
	 * 
	 * @param attributes
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private View createView(Attributes attributes) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<View> viewClass = (Class<View>) Class.forName(attributes.getValue("class"));
		
		if (viewClass == null) {
			return null;
		}
		
		String name = attributes.getValue("name");
		
		View viewInstance = viewClass.getConstructor(String.class).newInstance(name);
		
		m_currentBox.add(viewInstance);
		m_viewMapping.put(name, viewInstance);
		
		if (attributes.getValue("width") != null) {
			viewInstance.getSize().width = Integer.parseInt(attributes.getValue("width")); 
		}
		
		if (attributes.getValue("height") != null) {
			viewInstance.getSize().height = Integer.parseInt(attributes.getValue("height")); 
		}
		
		return viewInstance;
	}

	private void pushBox(Box box) {
		m_currentBox.add(box);
		m_currentBox = box;
	}
	
	private void popBox() {
		m_currentBox = (Box) m_currentBox.getParent();
	}
}
