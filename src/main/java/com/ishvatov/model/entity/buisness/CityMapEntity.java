package com.ishvatov.model.entity.buisness;

import lombok.Data;

import javax.persistence.*;

/**
 * Defines a basic entity, which stores the information about
 * distances between two cities.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "city_map")
@Data
public class CityMapEntity {

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String FROM_CITY = "from";

    /**
     * String representation of the 'status'
     * column name in the table.
     */
    public static final String TO_CITY = "to";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    /**
     * Id of th source city.
     */
    @Column(name = FROM_CITY)
    private int cityFromId;

    /**
     * Id of the destination city.
     */
    @Column(name = TO_CITY)
    private int cityToId;
}
