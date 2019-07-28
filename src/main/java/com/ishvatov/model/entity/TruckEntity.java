package com.ishvatov.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Describes a basic JPA instance of a truck object in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "project_truck")
@NoArgsConstructor
@EqualsAndHashCode
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
    @Getter
    @Setter
    private int truckId;

    /**
     * Unique registration number of the truck.
     */
    @Column(name = REGISTRATION_NUMBER, unique = true)
    @Getter
    @Setter
    private String truckRegistrationNumber;

    /**
     * Truck's capacity in tons.
     */
    @Column(name = CAPACITY)
    @Getter
    @Setter
    private double truckCapacity;

    /**
     * Driver shift size in hours.
     */
    @Column(name = SHIFT)
    @Getter
    @Setter
    private int driverShiftSize;

    /**
     * Truck status.
     * Bit value: 0 - OK, 1 - NOT OK.
     */
    @Column(name = CONDITION)
    @Getter
    @Setter
    private byte truckCondition;

    /**
     * Current location of the truck.
     */
    @Column(name = CITY)
    @Getter
    @Setter
    private String truckCurrentCity;
}
