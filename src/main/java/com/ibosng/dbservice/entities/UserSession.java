package com.ibosng.dbservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_session")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "benutzer_id")
    private Benutzer benutzer;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "invalidated_on")
    private LocalDateTime invalidatedOn;

}
