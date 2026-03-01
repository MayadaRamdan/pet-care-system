package com.petcare.admin.security.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "staff_user_role")
public class StaffUserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", unique = true)
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "staff_user_role_permission", joinColumns = @JoinColumn(name = "role_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "permission")
  private Set<StaffUserPermission> permissions;
}
