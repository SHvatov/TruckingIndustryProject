package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.TruckStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
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
    public static final String CAPACITY_FIELD = "capacity";

    /**
     * Name of the condition column in the DB.
     */
    public static final String STATUS_FIELD = "status";

    /**
     * Name of the driver shift size column in the DB.
     */
    public static final String SHIFT_FIELD = "shiftSize";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String CITY_ID_FIELD = "cityId";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String ORDER_ID_FIELD = "orderId";

    /**
     * Truck's capacity in tons.
     */
    @Column(name = CAPACITY_FIELD)
    private Double capacity;

    /**
     * DriverEntity shift size in hours.
     */
    @Column(name = SHIFT_FIELD)
    private Integer shiftSize;

    /**
     * Truck status.
     */
    @Column(name = STATUS_FIELD)
    @Enumerated(EnumType.STRING)
    private TruckStatusType status;

    /**
     * Set of drivers, who are assigned to this truck.
     */
    @OneToMany(mappedBy = "truck", fetch = FetchType.LAZY)
    private Set<DriverEntity> assignedDrivers = new HashSet<>();

    /**
     * City, where this truck is located.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CITY_ID_FIELD)
    private CityEntity city;

    /**
     * Order, assigned to this truck.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = ORDER_ID_FIELD)
    private OrderEntity order;

    /**
     * Adds driver to the set of the assigned to this truck ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            assignedDrivers.add(e);
            e.setTruck(this);
        });
    }

    /**
     * Removes driver from the set of the the assigned to this truck ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            assignedDrivers.remove(e);
            e.setTruck(null);
        });
    }
}
