package com.kangmin.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kangmin.app.model.Account.ID_LENGTH;

@Data
@Entity
@Table(name = "tb_category")
public class Category implements Serializable {

    private static final int NAME_LENGTH = 50;

    private static final int DESCRIPTION_LENGTH = 100;

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = ID_LENGTH)
    private String accountId;

    @Column(length = NAME_LENGTH)
    private String name;

    @Column(length = DESCRIPTION_LENGTH)
    private String description;

    @OneToMany(mappedBy = "category")
    private final List<Blog> blogs = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Category() {

    }

    public Category(
        final String name,
        final String description,
        final String accountId
    ) {
        this.name = name;
        this.description = description;
        this.accountId = accountId;
        final Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
    }

    @Override
    public String toString() {
        return "Category[id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}
