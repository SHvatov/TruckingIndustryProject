package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.DriverStatusType;
import lombok.*;

import javax.persistence.*;

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
    public static final String STATE = "driver_status";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String TRUCK_ID = "truck_id";

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
     * Truck, this driver is assigned to.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
        CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = TRUCK_ID)
    private TruckEntity driverTruckEntity;

    /**
     * City, where this driver is located.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
        CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = CITY_ID)
    private CityEntity driverCurrentCity;

    /**
     * Order, this driver is assigned to.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
        CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = ORDER_ID)
    private OrderEntity driverOrder;


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
