package dg.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Helper class to wrap a list of DailyS11s. This is used for saving the
 * list of DailyS11s to XML.
 * 

 */
@XmlRootElement(name = "dailyS11List") //tag <dailyS11List> in XML
public class DGListWrapper {
	private List<DailyS11> dailyS11List;

    @XmlElement(name = "dailyS11") //tag <dailyS11> for each dailyS11 in the dailyS11List
    
    public List<DailyS11> getDailyS11List() {
        return dailyS11List;
    }

    public void setDailyS11List(List<DailyS11> dailyS11List) {
        this.dailyS11List = dailyS11List;
    }
}
