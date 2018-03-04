package dg.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the 
 * list of tires to XML
 *
 */
@XmlRootElement (name = "tires")
public class TireListWrapper {
	private List<Tire> tires;
	
	@XmlElement(name = "tires")
	public List<Tire> getTires(){
		return tires;
	}
	//@XmlElement(name = "tires")
	public void setTires(List<Tire> tires) {
		this.tires = tires;
	}
}
