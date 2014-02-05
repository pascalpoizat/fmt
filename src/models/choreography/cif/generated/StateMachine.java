
package models.choreography.cif.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stateMachine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stateMachine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element name="initial" type="{http://convecs.inria.fr}initialState"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;choice>
 *             &lt;element name="interaction" type="{http://convecs.inria.fr}interactionState"/>
 *             &lt;element name="internalAction" type="{http://convecs.inria.fr}internalActionState"/>
 *             &lt;element name="subsetJoin" type="{http://convecs.inria.fr}subsetJoinState"/>
 *             &lt;element name="allJoin" type="{http://convecs.inria.fr}allJoinState"/>
 *             &lt;element name="simpleJoin" type="{http://convecs.inria.fr}simpleJoinState"/>
 *             &lt;element name="subsetSelect" type="{http://convecs.inria.fr}subsetSelectState"/>
 *             &lt;element name="allSelect" type="{http://convecs.inria.fr}allSelectState"/>
 *             &lt;element name="choice" type="{http://convecs.inria.fr}choiceState"/>
 *             &lt;element name="dominatedChoice" type="{http://convecs.inria.fr}dominatedChoiceState"/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="final" type="{http://convecs.inria.fr}finalState"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stateMachine", namespace = "http://convecs.inria.fr", propOrder = {
    "initial",
    "interactionOrInternalActionOrSubsetJoin",
    "_final"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
public class StateMachine {

    @XmlElement(namespace = "http://convecs.inria.fr", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected InitialState initial;
    @XmlElements({
        @XmlElement(name = "interaction", namespace = "http://convecs.inria.fr", type = InteractionState.class),
        @XmlElement(name = "internalAction", namespace = "http://convecs.inria.fr", type = InternalActionState.class),
        @XmlElement(name = "subsetJoin", namespace = "http://convecs.inria.fr", type = SubsetJoinState.class),
        @XmlElement(name = "allJoin", namespace = "http://convecs.inria.fr", type = AllJoinState.class),
        @XmlElement(name = "simpleJoin", namespace = "http://convecs.inria.fr", type = SimpleJoinState.class),
        @XmlElement(name = "subsetSelect", namespace = "http://convecs.inria.fr", type = SubsetSelectState.class),
        @XmlElement(name = "allSelect", namespace = "http://convecs.inria.fr", type = AllSelectState.class),
        @XmlElement(name = "choice", namespace = "http://convecs.inria.fr", type = ChoiceState.class),
        @XmlElement(name = "dominatedChoice", namespace = "http://convecs.inria.fr", type = DominatedChoiceState.class)
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected List<BaseState> interactionOrInternalActionOrSubsetJoin;
    @XmlElement(name = "final", namespace = "http://convecs.inria.fr")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    protected List<FinalState> _final;

    /**
     * Gets the value of the initial property.
     * 
     * @return
     *     possible object is
     *     {@link InitialState }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public InitialState getInitial() {
        return initial;
    }

    /**
     * Sets the value of the initial property.
     * 
     * @param value
     *     allowed object is
     *     {@link InitialState }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public void setInitial(InitialState value) {
        this.initial = value;
    }

    /**
     * Gets the value of the interactionOrInternalActionOrSubsetJoin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interactionOrInternalActionOrSubsetJoin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInteractionOrInternalActionOrSubsetJoin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InteractionState }
     * {@link InternalActionState }
     * {@link SubsetJoinState }
     * {@link AllJoinState }
     * {@link SimpleJoinState }
     * {@link SubsetSelectState }
     * {@link AllSelectState }
     * {@link ChoiceState }
     * {@link DominatedChoiceState }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public List<BaseState> getInteractionOrInternalActionOrSubsetJoin() {
        if (interactionOrInternalActionOrSubsetJoin == null) {
            interactionOrInternalActionOrSubsetJoin = new ArrayList<BaseState>();
        }
        return this.interactionOrInternalActionOrSubsetJoin;
    }

    /**
     * Gets the value of the final property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the final property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FinalState }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2014-01-10T09:17:50+01:00", comments = "JAXB RI v2.2.4-2")
    public List<FinalState> getFinal() {
        if (_final == null) {
            _final = new ArrayList<FinalState>();
        }
        return this._final;
    }

}
