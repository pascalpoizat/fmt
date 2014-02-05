
package fr.lip6.move.models.cif.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for joinState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="joinState">
 *   &lt;complexContent>
 *     &lt;extension base="{http://convecs.inria.fr}oneSuccState">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "joinState", namespace = "http://convecs.inria.fr")
@XmlSeeAlso({
    AllJoinState.class,
    SimpleJoinState.class,
    SubsetJoinState.class
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
public class JoinState
    extends OneSuccState
{


}
