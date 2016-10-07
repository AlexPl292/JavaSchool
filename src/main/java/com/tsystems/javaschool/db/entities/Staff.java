package com.tsystems.javaschool.db.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by alex on 10.09.16.
 * <p>
 * Entity for access staff table
 */
@Entity
@Table(name = "Staff", schema = "eCare")
public class Staff extends User {

}
