package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.TruckConditionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Describes a basic truck entity in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "truck")
@Data
@EqualsAndHashCode(callSuper = true)
public class TruckEntity extends AbstractEntity {

    /**
     * Name of the capacity column in the DB.
     */
    public static final String CAPACITY = "capacity";

    /**
     * Name of the condition column in the DB.
     */
    public static final String CONDITION = "state";

    /**
     * Name of the driver shift size column in the DB.
     */
    public static final String SHIFT = "shift_size";

    /**
     * Truck's capacity in tons.
     */
    @Column(name = CAPACITY)
    private double truckCapacity;

    /**
     * DriverEntity shift size in hours.
     */
    @Column(name = SHIFT)
    private int driverShiftSize;

    /**
     * Truck status.
     */
    @Column(name = CONDITION)
    @Enumerated(EnumType.STRING)
    private TruckConditionType truckCondition;

    /**
     * City, where truck is located.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private CityEntity truckCityEntity;

    /**
     * Drivers, who are using this truck.
     */
    @OneToMany(mappedBy = "driverTruckEntity", fetch = FetchType.LAZY)
    private Set<DriverEntity> truckDriverEntitySet;

    /**
     * Orders, that are assigned to this truck.
     */
    @ManyToMany(mappedBy = "orderTruckEntityList")
    private List<OrderEntity> truckOrderEntityList;
}
