package ru.cohenrol.authserver.datasource.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String password;

    public ServiceEntity(String serviceName, String password) {
        this.serviceName = serviceName;
        this.password = password;
    }
}