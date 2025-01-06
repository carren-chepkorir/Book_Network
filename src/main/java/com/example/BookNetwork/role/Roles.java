package com.example.BookNetwork.role;

import com.example.BookNetwork.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
 @EntityListeners(AuditingEntityListener.class)
public class Roles {
    @Id
    @GeneratedValue
  private BigDecimal id;
   @Column(unique = true)
  private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
//mapped by should be exact same name as defined in users table
  private List<User> users;

    @CreatedDate()
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    //means when new record is created ,this is not initialized and it can be null.Only updatable
    private LocalDateTime lastModifiedDate;
}
