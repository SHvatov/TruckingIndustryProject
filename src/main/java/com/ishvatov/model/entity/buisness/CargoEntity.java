package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.enum_types.CargoStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
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
    public static final String MASS_FIELD = "mass";

    /**
     * String representation of the 'cargo_status'
     * column name in the table.
     */
    public static final String STATE_FIELD = "status";

    /**
     * String representation of the 'name'
     * column name in the table.
     */
    public static final String NAME_FIELD = "name";

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
    @Column(name = NAME_FIELD)
    private String name;

    /**
     * Mass of the cargo.
     */
    @Column(name = MASS_FIELD)
    private Double mass;

    /**
     * Status of the cargo.
     */
    @Column(name = STATE_FIELD)
    @Enumerated(EnumType.STRING)
    private CargoStatusType status;

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints = new HashSet<>();

    /**
     * Adds a waypoint to the set of the waypoints assigned to this cargo.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            assignedWaypoints.add(e);
            e.setCargo(this);
        });
    }

    /**
     * Removes waypoint from the set of the waypoints assigned to this cargo.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            assignedWaypoints.remove(e);
            e.setCargo(null);
        });
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
