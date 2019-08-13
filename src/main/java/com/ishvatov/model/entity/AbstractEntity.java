package com.ishvatov.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Abstract super class for all the entities in the project.
 *
 * @author Sergey Khvatov
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

    /**
     * To implement find method,
     * all unique columns in the DB for all instances
     * are named as unique key.
     */
    public static final String UNIQUE_KEY_COLUMN_NAME = "uniqueIdentificator";

    /**
     * Unique id of the truck in the DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    /**
     * Unique identificator of the object.
     */
    @Column(name = UNIQUE_KEY_COLUMN_NAME, unique = true)
    private String uniqueIdentificator;

    /**
     * Basic implementation of the hashCode
     * method. Unique identificator is unique for
     * each entity in the entity group.
     *
     * @return hashcode of the UID.
     */
    @Override
    public int hashCode() {
        return uniqueIdentificator.hashCode();
    }

    /**
     * Basic implementation of the equals
     * method. Unique identificator is unique for
     * each entity in the entity group.
     *
     * @return true, if objects are of the same type
     * and have the same UID.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            return this.uniqueIdentificator.equals(((AbstractEntity) obj).uniqueIdentificator);
        }
    }

    /**
     * To string method implementation.
     *
     * @return string representation of the entity.
     */
    @Override
    public String toString() {
        return getClass().getName() + "{id=" +
            (getId() == null ? "#" : getId())
            + "; UID=" + getUniqueIdentificator() + "}";
    }
}
