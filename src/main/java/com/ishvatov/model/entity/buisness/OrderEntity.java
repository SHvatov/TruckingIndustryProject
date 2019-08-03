package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.OrderStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Defines a basic order in the system.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "order")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends AbstractEntity {

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String STATUS = "status";

    /**
     * Status of the order.
     */
    @Column(name = STATUS)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatus;

    /**
     * List of trucks, that are assigned to this order.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "orders_trucks",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "truck_id")
    )
    private List<TruckEntity> orderTruckEntityList;

    /**
     * List of drivers, that are assigned to this order.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "orders_drivers",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<DriverEntity> orderDriverEntityList;

    /**
     * List of way points, that form the order.
     */
    @OneToMany(mappedBy = "wayPointOrderEntity", fetch = FetchType.LAZY)
    private List<WayPointEntity> wayPointEntityList;
}
