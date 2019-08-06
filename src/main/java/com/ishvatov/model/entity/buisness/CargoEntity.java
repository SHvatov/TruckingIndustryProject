package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.enum_types.CargoStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
public class CargoEntity extends AbstractEntity {

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
    private Set<WayPointEntity> assignedWaypoints;

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
