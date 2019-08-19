package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.DriverStatusType;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Defines basic driver entity in the database.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity extends AbstractEntity {

    /**
     * String representation of the 'name'
     * column name in the table.
     */
    public static final String NAME_FIELD = "name";

    /**
     * String representation of the 'surname'
     * column name in the table.
     */
    public static final String SURNAME_FIELD = "surname";

    /**
     * String representation of the 'worked_hours'
     * column name in the table.
     */
    public static final String WORKED_HOURS_FIELD = "workedHours";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String LAST_UPDATED_FIELD = "lastUpdated";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String STATE_FIELD = "status";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String TRUCK_ID_FIELD = "truckId";

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
     * Name of the driver.
     */
    @Column(name = NAME_FIELD)
    private String name;

    /**
     * Surname of the driver.
     */
    @Column(name = SURNAME_FIELD)
    private String surname;

    /**
     * Number of hours driver has worked
     * in this month.
     */
    @Column(name = WORKED_HOURS_FIELD)
    private Integer workedHours;

    /**
     * Date - last time order was updated.
     */
    @Column(name = LAST_UPDATED_FIELD)
    private Timestamp lastUpdated;

    /**
     * Status of the driver.
     */
    @Column(name = STATE_FIELD)
    @Enumerated(EnumType.STRING)
    private DriverStatusType status;

    /**
     * Truck, this driver is assigned to.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TRUCK_ID_FIELD)
    private TruckEntity truck;

    /**
     * City, where this driver is located.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CITY_ID_FIELD)
    private CityEntity city;

    /**
     * Order, this driver is assigned to.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = ORDER_ID_FIELD)
    private OrderEntity order;
}
