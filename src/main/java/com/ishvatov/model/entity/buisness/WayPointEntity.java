package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.enum_types.CargoActionType;
import lombok.Data;

import javax.persistence.*;

/**
 * Defines a basic entity in the database, that stores the information
 * about one way point in the order.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "order_waypoints")
@Data
public class WayPointEntity {

    /**
     * String representation of the 'action'
     * column name in the table.
     */
    public static final String ACTION = "action";

    /**
     * String representation of the 'order_id'
     * column name in the table.
     */
    public static final String ORDER_ID = "order_id";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    /**
     * Defines whether cargo is being loaded or
     * unloaded in this city.
     */
    @Column(name = ACTION)
    private CargoActionType cargoAction;

    /**
     * Defines the order entity.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity wayPointOrderEntity;

    /**
     * Way point location.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private CityEntity wayPointCityEntity;

    /**
     * Way point cargo.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "cargo_id")
    private CargoEntity wayPointCargoEntity;
}