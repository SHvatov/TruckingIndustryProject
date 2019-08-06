package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Defines a basic order in the system.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends AbstractEntity {

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String STATUS = "order_status";

    /**
     * Status of the order.
     */
    @Column(name = STATUS)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatus;

    /**
     * Truck, that is assigned to this order.
     */
    @OneToMany(mappedBy = "truckOrder", fetch = FetchType.LAZY)
    private Set<TruckEntity> truckEntitySet;

    /**
     * Set of drivers, who are assigned to this order.
     */
    @OneToMany(mappedBy = "driverOrder", fetch = FetchType.LAZY)
    private Set<DriverEntity> driverEntitySet;

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "waypointOrderEntity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints;

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
