package com.ishvatov.model.entity;

import javax.persistence.*;

/**
 * Describes a basic JPA instance of a truck object in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "project_truck")
public class TruckEntity {

    /**
     * Name of the registration number column in the DB.
     */
    public static final String REGISTRATION_NUMBER = "truckRegistrationNumber";

    /**
     * Name of the capacity column in the DB.
     */
    public static final String CAPACITY = "truckCapacity";

    /**
     * Name of the condition column in the DB.
     */
    public static final String CONDITION = "truckCondition";

    /**
     * Name of the city column in the DB.
     */
    public static final String CITY = "truckCurrentCity";

    /**
     * Name of the driver shift size column in the DB.
     */
    public static final String SHIFT = "driverShiftSize";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truckId", unique = true)
    private int truckId;

    /**
     * Unique registration number of the truck.
     */
    @Column(name = REGISTRATION_NUMBER, unique = true)
    private String truckRegistrationNumber;

    /**
     * Truck's capacity in tons.
     */
    @Column(name = CAPACITY)
    private double truckCapacity;

    /**
     * Driver shift size in hours.
     */
    @Column(name = SHIFT)
    private int driverShiftSize;

    /**
     * Truck status.
     * Bit value: 0 - OK, 1 - NOT OK.
     */
    @Column(name = CONDITION)
    private byte truckCondition;

    /**
     * Current location of the truck.
     */
    @Column(name = CITY)
    private String truckCurrentCity;

    /**
     * Default class constructor.
     */
    public TruckEntity() {
    }

    // default getter / setter methods implementation
    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public String getTruckRegistrationNumber() {
        return truckRegistrationNumber;
    }

    public void setTruckRegistrationNumber(String truckRegistrationNumber) {
        this.truckRegistrationNumber = truckRegistrationNumber;
    }

    public double getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(double truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public byte getTruckCondition() {
        return truckCondition;
    }

    public void setTruckCondition(byte truckCondition) {
        this.truckCondition = truckCondition;
    }

    public int getDriverShiftSize() {
        return driverShiftSize;
    }

    public void setDriverShiftSize(int driverShiftSize) {
        this.driverShiftSize = driverShiftSize;
    }

    public String getTruckCurrentCity() {
        return truckCurrentCity;
    }

    public void setTruckCurrentCity(String truckCurrentCity) {
        this.truckCurrentCity = truckCurrentCity;
    }
}
