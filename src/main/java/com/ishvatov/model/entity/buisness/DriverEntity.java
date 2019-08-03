package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.DriverStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Defines basic driver entity in the database.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "driver")
@Data
@EqualsAndHashCode(callSuper = true)
public class DriverEntity extends AbstractEntity {

    /**
     * String representation of the 'name'
     * column name in the table.
     */
    public static final String NAME = "name";

    /**
     * String representation of the 'surname'
     * column name in the table.
     */
    public static final String SURNAME = "surname";

    /**
     * String representation of the 'worked_hours'
     * column name in the table.
     */
    public static final String WORKED_HOURS = "worked_hours";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String STATE = "status";

    /**
     * Name of the driver.
     */
    @Column(name = NAME)
    private String driverName;

    /**
     * Surname of the driver.
     */
    @Column(name = SURNAME)
    private String driverSurname;

    /**
     * Number of hours driver has worked
     * in this month.
     */
    @Column(name = WORKED_HOURS)
    private String driverWorkedHours;

    /**
     * Status of the driver.
     */
    @Column(name = STATE)
    @Enumerated(EnumType.STRING)
    private DriverStatusType driverStatus;

    /**
     * This driver's truck.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "truck_id")
    private TruckEntity driverTruckEntity;

    /**
     * This driver's truck.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private CityEntity driverCityEntity;

    /**
     * Orders, that are assigned to this driver.
     */
    @ManyToMany(mappedBy = "orderDriverEntityList")
    private List<OrderEntity> truckOrderEntityList;
}
