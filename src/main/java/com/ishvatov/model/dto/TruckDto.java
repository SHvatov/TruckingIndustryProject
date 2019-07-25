package com.ishvatov.model.dto;

/**
 * Defines a basic DTO, which represents a truck.
 *
 * @author Sergey Khvatov
 */
public class TruckDto {

    /**
     * Unique registration number of the truck.
     */
    private String truckRegistrationNumber;

    /**
     * Truck's capacity in tons.
     */
    private double truckCapacity;

    /**
     * Driver shift size in hours.
     */
    private int driverShiftSize;

    /**
     * Truck status.
     * Bit value: 0 - OK, 1 - NOT OK.
     */
    private byte truckCondition;

    /**
     * Current location of the truck.
     */
    private String truckCurrentCity;

    /**
     * Default class constructor.
     */
    public TruckDto() {
    }

    // default getter / setter methods implementation
    public String getTruckRegistrationNumber() {
        return truckRegistrationNumber;
    }

    public void setTruckRegistrationNumber(String truckRegistrationNumber) {
        this.truckRegistrationNumber = truckRegistrationNumber;
    }

    public int getDriverShiftSize() {
        return driverShiftSize;
    }

    public void setDriverShiftSize(int driverShiftSize) {
        this.driverShiftSize = driverShiftSize;
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

    public String getTruckCurrentCity() {
        return truckCurrentCity;
    }

    public void setTruckCurrentCity(String truckCurrentCity) {
        this.truckCurrentCity = truckCurrentCity;
    }
}
