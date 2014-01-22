
package verchor.models.cif;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subsetSelectState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subsetSelectState">
 *   &lt;complexContent>
 *     &lt;extension base="{http://convecs.inria.fr}selectionState">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="default" type="{http://convecs.inria.fr}id"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subsetSelectState", namespace = "http://convecs.inria.fr", propOrder = {
    "_default"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
public class SubsetSelectState
    extends SelectionState
{

    @XmlElement(name = "default", namespace = "http://convecs.inria.fr")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected String _default;

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public String getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public void setDefault(String value) {
        this._default = value;
    }

}
