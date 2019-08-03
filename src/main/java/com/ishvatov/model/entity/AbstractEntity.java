package com.ishvatov.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Abstract super class for all the entities in the project.
 *
 * @author Sergey Khvatov
 */
@MappedSuperclass
@Data
public abstract class AbstractEntity {

    /**
     * To implement findByUniqueKey method,
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
    private int id;

    /**
     * Unique identificator of the object.
     */
    @Column(name = UNIQUE_KEY_COLUMN_NAME, unique = true)
    private String uniqueIdentificator;
}
