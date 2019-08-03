package com.ishvatov.model.entity.buisness;

import com.ishvatov.model.entity.enum_types.CargoStatusType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Defines a basic entity in the database, which stores the
 * information about cargo unit.
 *
 * @author Sergey Khvatov
 */
@Entity
@Table(name = "cargo")
@Data
public class CargoEntity {

    /**
     * String representation of the 'mass'
     * column name in the table.
     */
    public static final String MASS = "mass";

    /**
     * String representation of the 'name'
     * column name in the table.
     */
    public static final String NAME = "name";

    /**
     * String representation of the 'state'
     * column name in the table.
     */
    public static final String STATE = "state";

    /**
     * Id of the cargo in the db.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    /**
     * Name of the cargo.
     */
    @Column(name = NAME)
    private String cargoName;

    /**
     * Mass of the cargo.
     */
    @Column(name = MASS)
    private double cargoMass;

    /**
     * Status of the cargo.
     */
    @Column(name = STATE)
    @Enumerated(EnumType.STRING)
    private CargoStatusType cargoStatus;

    /**
     * List of way points.
     */
    @OneToMany(mappedBy = "wayPointCargoEntity", fetch = FetchType.LAZY)
    private List<WayPointEntity> wayPointEntityList;
}
