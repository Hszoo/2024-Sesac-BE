package seomile.api.program.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    private String proName;
    private String proLocation;
    private Date startDate;
    private Date endDate;
    private String organization;
    private String address;
    private String thumbnail;
    private String url;
}
