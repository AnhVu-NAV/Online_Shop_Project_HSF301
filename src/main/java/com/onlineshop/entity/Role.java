
package com.onlineshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "role")
public class Role {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;
}