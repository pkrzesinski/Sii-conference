package com.project.siiproject.feature.lecture.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LECTURES")
public class Lecture {

    @Id
    @Column(name = "id")
    private Long id;
}
