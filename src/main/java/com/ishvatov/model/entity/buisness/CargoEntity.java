package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.enum_types.CargoStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a basic entity in the database, which stores the
 * information about cargo unit.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "cargo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargoEntity {

    /**
     * String representation of the 'mass'
     * column name in the table.
     */
    public static final String MASS = "mass";

    /**
     * String representation of the 'state'
     * column name in the table.
     */
    public static final String STATE = "cargo_status";

    /**
     * String representation of the 'name'
     * column name in the table.
     */
    public static final String NAME = "name";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    /**
     * Name of the cargo.
     */
    @Column(name = NAME)
    private String cargoName;

    /**
     * Mass of the cargo.
     */
    @Column(name = MASS)
    private Double cargoMass;

    /**
     * Status of the cargo.
     */
    @Column(name = STATE)
    @Enumerated(EnumType.STRING)
    private CargoStatusType cargoStatus;

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "waypointCargoEntity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints = new HashSet<>();

    /**
     * Adds a waypoint to the set of the waypoints assigned to this cargo.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.add(wayPointEntity);
        wayPointEntity.setWaypointCargoEntity(this);
    }

    /**
     * Removes waypoint from the set of the waypoints assigned to this cargo.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.remove(wayPointEntity);
        wayPointEntity.setWaypointCargoEntity(null);
    }

    /**
     * Equals method override.
     *
     * @param obj another object.
     * @return false, if other object is null, of other type or does not equal
     * to this, true otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CargoEntity)) {
            return false;
        } else {
            CargoEntity entity = (CargoEntity) obj;
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
