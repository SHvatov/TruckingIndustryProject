package com.ishvatov.model.entity.buisness;

import lombok.*;

import javax.persistence.*;

/**
 * Defines a basic entity, which stores the information about
 * distances between two cities.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "city_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityMapEntity {

    /**
     * String representation of the 'from'
     * column name in the table.
     */
    public static final String FROM_CITY = "from";

    /**
     * String representation of the 'to'
     * column name in the table.
     */
    public static final String TO_CITY = "to";

    /**
     * String representation of the 'distance'
     * column name in the table.
     */
    public static final String DISTANCE_FIELD = "distance";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    /**
     * Id of th source city.
     */
    @Column(name = FROM_CITY)
    private Integer cityFromId;

    /**
     * Id of the destination city.
     */
    @Column(name = TO_CITY)
    private Integer cityToId;

    /**
     * Distance between two cities.
     */
    @Column(name = DISTANCE_FIELD)
    private Double distance;


    /**
     * Equals method override.
     *
     * @param obj another object.
     * @return false, if other object is null, of other type or does not equal
     * to this, true otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CityMapEntity)) {
            return false;
        } else {
            CityMapEntity entity = (CityMapEntity) obj;
            return getId().equals(entity.getId())
                && getCityToId().equals(entity.getCityToId())
                && getCityFromId().equals(entity.getCityFromId());
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
        return getClass().getName() + "{id=" + getId();
    }
}