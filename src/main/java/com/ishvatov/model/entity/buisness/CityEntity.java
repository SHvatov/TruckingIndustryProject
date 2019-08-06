package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Defines a basic city entity in the data base, where
 * id - is the id of the city,
 * userUniqueIdentificator - name of the city.
 */
@Entity
@Table(name = "city")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityEntity extends AbstractEntity {

    /**
     * Set of trucks, that are located in the city.
     */
    @OneToMany(mappedBy = "truckCity", fetch = FetchType.LAZY)
    private Set<TruckEntity> locatedTrucks;

    /**
     * Set of trucks, that are located in the city.
     */
    @OneToMany(mappedBy = "driverCurrentCity", fetch = FetchType.LAZY)
    private Set<DriverEntity> locatedDrivers;

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "waypointCityEntity", fetch = FetchType.LAZY)
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
        if (!(obj instanceof CityEntity)) {
            return false;
        } else {
            CityEntity entity = (CityEntity) obj;
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
