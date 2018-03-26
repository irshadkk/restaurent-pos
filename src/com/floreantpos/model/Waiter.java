package com.floreantpos.model;

import java.io.Serializable;

public class Waiter implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;
    private java.lang.Integer autoId;
    protected java.lang.String firstName;
    protected java.lang.String lastName;
    protected java.lang.String dob;
    private int hashCode = Integer.MIN_VALUE;
    /*[CONSTRUCTOR MARKER BEGIN]*/

    public Waiter() {

    }

    /**
     * Return the unique identifier of this class
     *
     * @hibernate.id generator-class="identity" column="AUTO_ID"
     */
    public java.lang.Integer getAutoId() {
        return autoId;
    }

    /**
     * Set the unique identifier of this class
     *
     * @param autoId the new ID
     */
    public void setAutoId(java.lang.Integer autoId) {
        this.autoId = autoId;
        this.hashCode = Integer.MIN_VALUE;
    }
    
    /**
     * Return the value associated with the column: FIRST_NAME
     */
    public java.lang.String getFirstName() {
        return firstName;
    }

    /**
     * Set the value related to the column: FIRST_NAME
     *
     * @param firstName the FIRST_NAME value
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    /**
     * Return the value associated with the column: LAST_NAME
     */
    public java.lang.String getLastName() {
        return lastName;
    }

    /**
     * Set the value related to the column: LAST_NAME
     *
     * @param lastName the LAST_NAME value
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public int compareTo(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
