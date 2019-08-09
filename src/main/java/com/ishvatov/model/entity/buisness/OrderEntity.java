package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
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
    @OneToOne(mappedBy = "truckOrder", fetch = FetchType.LAZY)
    private TruckEntity truckEntity;

    /**
     * Set of drivers, who are assigned to this order.
     */
    @OneToMany(mappedBy = "driverOrder", fetch = FetchType.LAZY)
    private Set<DriverEntity> driverEntitySet = new HashSet<>();

    /**
     * Set of waypoints, that are located in the order.
     */
    @OneToMany(mappedBy = "waypointOrderEntity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints = new HashSet<>();

    /**
     * Adds driver to the set of the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        driverEntitySet.add(driverEntity);
        driverEntity.setDriverOrder(this);
    }

    /**
     * Removes driver from the set of the the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        driverEntitySet.remove(driverEntity);
        driverEntity.setDriverOrder(null);
    }

    /**
     * Adds waypoint to the set of the assigned to this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.add(wayPointEntity);
        wayPointEntity.setWaypointOrderEntity(this);
    }

    /**
     * Removes waypoint from the set of the located in this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.remove(wayPointEntity);
        wayPointEntity.setWaypointOrderEntity(null);
    }
}
