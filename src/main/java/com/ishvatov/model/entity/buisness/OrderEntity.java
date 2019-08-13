package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
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
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String START_DATE = "order_start";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String END_DATE = "order_end";

    /**
     * Status of the order.
     */
    @Column(name = STATUS)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatus;

    /**
     * Date - start of the order.
     */
    @Column(name = START_DATE)
    private Timestamp orderStart;

    /**
     * Date - end of the order.
     */
    @Column(name = END_DATE)
    private Timestamp orderEnd;

    /**
     * Truck, that is assigned to this order.
     */
    @OneToOne(mappedBy = "truckOrder", fetch = FetchType.LAZY)
    private TruckEntity assignedTruck;

    /**
     * Set of drivers, who are assigned to this order.
     */
    @OneToMany(mappedBy = "driverOrder", fetch = FetchType.LAZY)
    private Set<DriverEntity> assignedDrivers = new HashSet<>();

    /**
     * Set of waypoints, that are located in the order.
     */
    @OneToMany(mappedBy = "waypointOrder", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints = new HashSet<>();

    /**
     * Adds driver to the set of the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            assignedDrivers.add(e);
            e.setDriverOrder(this);
        });
    }

    /**
     * Removes driver from the set of the the assigned to this order ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            assignedDrivers.remove(e);
            e.setDriverOrder(null);
        });
    }

    /**
     * Adds waypoint to the set of the assigned to this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            assignedWaypoints.add(e);
            e.setWaypointOrder(this);
        });
    }

    /**
     * Removes waypoint from the set of the located in this order ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            assignedWaypoints.remove(e);
            e.setWaypointOrder(null);
        });
    }
}
