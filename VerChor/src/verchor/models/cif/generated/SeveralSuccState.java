
package verchor.models.cif.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for severalSuccState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="severalSuccState">
 *   &lt;complexContent>
 *     &lt;extension base="{http://convecs.inria.fr}baseState">
 *       &lt;sequence>
 *         &lt;element name="successors">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://convecs.inria.fr}successorList">
 *               &lt;minLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "severalSuccState", namespace = "http://convecs.inria.fr", propOrder = {
    "successors"
})
@XmlSeeAlso({
    SelectionState.class
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
public class SeveralSuccState
    extends BaseState
{

    @XmlList
    @XmlElement(namespace = "http://convecs.inria.fr", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected List<String> successors;

    /**
     * Gets the value of the successors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the successors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuccessors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public List<String> getSuccessors() {
        if (successors == null) {
            successors = new ArrayList<String>();
        }
        return this.successors;
    }

}
