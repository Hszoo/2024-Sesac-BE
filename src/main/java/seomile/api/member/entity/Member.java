package seomile.api.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 식별자

    private String memberPassword;
}
