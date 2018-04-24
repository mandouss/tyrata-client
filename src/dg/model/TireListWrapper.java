package dg.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of Tires. This is used for saving the 
 * list of Tires to XML
 *
 */
@XmlRootElement (name = "tires") //tag <tires> in XML, list of tires
public class TireListWrapper {
	private List<Tire> tires;
	
	@XmlElement(name = "tire") //tag <tire> in XML
	public List<Tire> getTires(){
		return tires;
	}
	//@XmlElement(name = "tires")
	public void setTires(List<Tire> tires) {
		this.tires = tires;
	}
}
