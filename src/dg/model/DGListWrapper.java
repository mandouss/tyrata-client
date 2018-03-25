package dg.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 * 

 */
@XmlRootElement(name = "dailyS11List")
public class DGListWrapper {
	private List<DailyS11> dailyS11List;

    @XmlElement(name = "dailyS11")
    
    public List<DailyS11> getDailyS11List() {
        return dailyS11List;
    }

    public void setDailyS11List(List<DailyS11> dailyS11List) {
        this.dailyS11List = dailyS11List;
    }
}
