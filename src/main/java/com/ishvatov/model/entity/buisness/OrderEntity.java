package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * Set of waypoints, that are located in the order.
     */
    @OneToMany(mappedBy = "waypointOrderEntity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints;

    /**
     * Adds truck to the set of the assigned to this order ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void addTruck(TruckEntity truckEntity) {
        truckEntitySet.add(truckEntity);
        truckEntity.setTruckOrder(this);
    }

    /**
     * Removes truck from the set of the located in this order ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void removeTruck(TruckEntity truckEntity) {
        truckEntitySet.remove(truckEntity);
        truckEntity.setTruckOrder(null);
    }

    /**
     * Adds driver to the set of the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        driverEntitySet.add(driverEntity);
        driverEntity.setDriverOrder(this);
    }

    /**
     * Removes driver from the set of the the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        driverEntitySet.remove(driverEntity);
        driverEntity.setDriverOrder(null);
    }

    /**
     * Adds waypoint to the set of the assigned to this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        assignedWaypoints.add(wayPointEntity);
        wayPointEntity.setWaypointOrderEntity(this);
    }

    /**
     * Removes waypoint from the set of the located in this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        assignedWaypoints.remove(wayPointEntity);
        wayPointEntity.setWaypointOrderEntity(null);
    }

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
