package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.TruckConditionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes a basic truck entity in the data base.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "truck")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TruckEntity extends AbstractEntity {

    /**
     * Name of the capacity column in the DB.
     */
    public static final String CAPACITY = "capacity";

    /**
     * Name of the condition column in the DB.
     */
    public static final String CONDITION = "truck_status";

    /**
     * Name of the driver shift size column in the DB.
     */
    public static final String SHIFT = "shift_size";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String CITY_ID = "city_id";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String ORDER_ID = "order_id";

    /**
     * Truck's capacity in tons.
     */
    @Column(name = CAPACITY)
    private Double truckCapacity;

    /**
     * DriverEntity shift size in hours.
     */
    @Column(name = SHIFT)
    private Integer truckDriverShiftSize;

    /**
     * Truck status.
     */
    @Column(name = CONDITION)
    @Enumerated(EnumType.STRING)
    private TruckConditionType truckCondition;

    /**
     * Set of drivers, who are assigned to this truck.
     */
    @OneToMany(mappedBy = "driverTruckEntity", fetch = FetchType.LAZY)
    private Set<DriverEntity> truckDriversSet = new HashSet<>();

    /**
     * City, where this truck is located.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
        CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = CITY_ID)
    private CityEntity truckCity;

    /**
     * Order, assigned to this truck.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
        CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = ORDER_ID)
    private OrderEntity truckOrder;

    /**
     * Adds driver to the set of the assigned to this truck ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        truckDriversSet.add(driverEntity);
        driverEntity.setDriverTruckEntity(this);
    }

    /**
     * Removes driver from the set of the the assigned to this truck ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        truckDriversSet.remove(driverEntity);
        driverEntity.setDriverTruckEntity(null);
    }
}
