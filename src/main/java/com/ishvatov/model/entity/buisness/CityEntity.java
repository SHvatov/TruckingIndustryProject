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
import java.util.Optional;
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
    @OneToMany(mappedBy = "driverCity", fetch = FetchType.LAZY)
    private Set<DriverEntity> locatedDrivers = new HashSet<>();

    /**
     * Set of waypoints, that are located in the city.
     */
    @OneToMany(mappedBy = "waypointCity", fetch = FetchType.LAZY)
    private Set<WayPointEntity> locatedWaypoints = new HashSet<>();

    /**
     * Adds truck to the set of the located in this city ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void addTruck(TruckEntity truckEntity) {
        Optional.ofNullable(truckEntity).ifPresent(e -> {
            locatedTrucks.add(e);
            e.setTruckCity(this);
        });
    }

    /**
     * Removes truck from the set of the located in this city ones.
     *
     * @param truckEntity {@link TruckEntity} entity.
     */
    public void removeTruck(TruckEntity truckEntity) {
        Optional.ofNullable(truckEntity).ifPresent(e -> {
            locatedTrucks.remove(e);
            e.setTruckCity(null);
        });
    }

    /**
     * Adds driver to the set of the located in this city ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void addDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            locatedDrivers.add(e);
            e.setDriverCity(this);
        });
    }

    /**
     * Removes driver from the set of the located in this city ones.
     *
     * @param driverEntity {@link DriverEntity} entity.
     */
    public void removeDriver(DriverEntity driverEntity) {
        Optional.ofNullable(driverEntity).ifPresent(e -> {
            locatedDrivers.remove(e);
            e.setDriverCity(null);
        });
    }

    /**
     * Adds waypoint to the set of the located in this city ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void addWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            locatedWaypoints.add(e);
            e.setWaypointCity(this);
        });
    }

    /**
     * Removes waypoint from the set of the located in this city ones.
     *
     * @param wayPointEntity {@link WayPointEntity} entity.
     */
    public void removeWayPoint(WayPointEntity wayPointEntity) {
        Optional.ofNullable(wayPointEntity).ifPresent(e -> {
            locatedWaypoints.remove(e);
            e.setWaypointCity(null);
        });
    }
}
