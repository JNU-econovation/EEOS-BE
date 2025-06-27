package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = Authority.NAME, indexes = {@Index(name = "idx_member_id", columnList = Authority.NAME + "_member_id")})
@Getter
public class Authority extends BaseEntity {

    public static final String NAME = "authority";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = NAME+"_member_id")
    private Long memberId;

    @Column(nullable = false, name = NAME+"_role")
    private String role;

}
