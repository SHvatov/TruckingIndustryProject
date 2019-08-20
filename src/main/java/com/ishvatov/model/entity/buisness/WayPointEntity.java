package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.enum_types.CargoActionType;
import com.ishvatov.model.entity.enum_types.WayPointStatusType;
import lombok.*;

import javax.persistence.*;

/**
 * Defines a basic entity in the database, that stores the information
 * about one way point in the order.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "waypoint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WayPointEntity {

    /**
     * String representation of the 'action'
     * column name in the table.
     */
    public static final String ACTION_FIELD = "action";

    /**
     * String representation of the 'action'
     * column name in the table.
     */
    public static final String STATUS_FIELD = "status";

    /**
     * String representation of the 'order_id'
     * column name in the table.
     */
    public static final String ORDER_ID_FIELD = "orderId";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String CITY_ID_FIELD = "cityId";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String CARGO_ID_FIELD = "cargoId";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    /**
     * Defines whether cargo is being loaded or
     * unloaded in this city.
     */
    @Column(name = ACTION_FIELD)
    private CargoActionType action;

    /**
     * Defines the status of the waypoint (completed or not).
     */
    @Column(name = STATUS_FIELD)
    private WayPointStatusType status;

    /**
     * Order, this waypoint is assigned to.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = ORDER_ID_FIELD)
    private OrderEntity order;

    /**
     * Cargo, that is assigned to this waypoint.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CARGO_ID_FIELD)
    private CargoEntity cargo;

    /**
     * City, that is assigned to this waypoint.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CITY_ID_FIELD)
    private CityEntity city;

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
            return getId().equals(entity.getId());
        }
    }

    /**
     * HashCode method implementation.
     *
     * @return hash code of the object.
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * To string method implementation.
     *
     * @return string representation of the entity.
     */
    @Override
    public String toString() {
        return getClass().getName() + "{id=" + getId() + "}";
    }
}