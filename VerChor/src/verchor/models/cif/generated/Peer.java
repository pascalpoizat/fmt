
package verchor.models.cif.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for peer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="peer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peerID" type="{http://convecs.inria.fr}id"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peer", namespace = "http://convecs.inria.fr", propOrder = {
    "peerID"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
public class Peer {

    @XmlElement(namespace = "http://convecs.inria.fr", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected String peerID;

    /**
     * Gets the value of the peerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public String getPeerID() {
        return peerID;
    }

    /**
     * Sets the value of the peerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public void setPeerID(String value) {
        this.peerID = value;
    }

}
