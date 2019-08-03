package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 * Defines a basic city entity in the data base, where
 * id - is the id of the city,
 * uniqueIdentificator - name of the city.
 */
@Entity
@Table(name = "city")
@Data
@EqualsAndHashCode(callSuper = true)
public class CityEntity extends AbstractEntity {

    /**
     * Trucks, that are located in this city.
     */
    @OneToMany(mappedBy = "truckCityEntity", fetch = FetchType.LAZY)
    private Set<TruckEntity> truckEntitySet;

    /**
     * Trucks, that are located in this city.
     */
    @OneToMany(mappedBy = "driverCityEntity", fetch = FetchType.LAZY)
    private Set<DriverEntity> driverEntitySet;

    /**
     * List of way points.
     */
    @OneToMany(mappedBy = "wayPointCityEntity", fetch = FetchType.LAZY)
    private List<WayPointEntity> wayPointEntityList;
}
