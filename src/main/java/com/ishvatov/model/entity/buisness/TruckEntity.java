package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.TruckConditionType;
import lombok.*;

import javax.persistence.*;
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
    private Set<DriverEntity> truckDriversSet;

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
     * Equals method override.
     *
     * @param obj another object.
     * @return false, if other object is null, of other type or does not equal
     * to this, true otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DriverEntity)) {
            return false;
        } else {
            DriverEntity entity = (DriverEntity) obj;
            return getId().equals(entity.getId()) && getUniqueIdentificator().equals(entity.getUniqueIdentificator());
        }
    }

    /**
     * HashCode method implementation.
     *
     * @return hash code of the object.
     */
    @Override
    public int hashCode() {
        return getId().hashCode() + getUniqueIdentificator().hashCode();
    }

    /**
     * To string method implementation.
     *
     * @return string representation of the entity.
     */
    @Override
    public String toString() {
        return getClass().getName() + "{id=" + getId() + "; UID=" + getUniqueIdentificator();
    }
}
