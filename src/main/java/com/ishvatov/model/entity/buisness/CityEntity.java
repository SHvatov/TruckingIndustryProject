package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
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
    private Set<TruckEntity> locatedTrucks = new HashSet<>();

    /**
     * Set of trucks, that are located in the city.
     */
    @OneToMany(mappedBy = "driverCurrentCity", fetch = FetchType.LAZY)
    private Set<DriverEntity> locatedDrivers = new HashSet<>();

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "waypointCityEntity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> assignedWaypoints = new HashSet<>();

    /**
     * Adds truck to the set of the located in this city ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void addTruck(TruckEntity truckEntity) {
        if (truckEntity == null) return;
        locatedTrucks.add(truckEntity);
        truckEntity.setTruckCity(this);
    }

    /**
     * Removes truck from the set of the located in this city ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void removeTruck(TruckEntity truckEntity) {
        if (truckEntity == null) return;
        locatedTrucks.remove(truckEntity);
        truckEntity.setTruckCity(null);
    }

    /**
     * Adds driver to the set of the located in this city ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        locatedDrivers.add(driverEntity);
        driverEntity.setDriverCurrentCity(this);
    }

    /**
     * Removes driver from the set of the located in this city ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        if (driverEntity == null) return;
        locatedDrivers.remove(driverEntity);
        driverEntity.setDriverCurrentCity(null);
    }

    /**
     * Adds waypoint to the set of the located in this city ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.add(wayPointEntity);
        wayPointEntity.setWaypointCityEntity(this);
    }

    /**
     * Removes waypoint from the set of the located in this city ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        if (wayPointEntity == null) return;
        assignedWaypoints.remove(wayPointEntity);
        wayPointEntity.setWaypointCityEntity(null);
    }
}
