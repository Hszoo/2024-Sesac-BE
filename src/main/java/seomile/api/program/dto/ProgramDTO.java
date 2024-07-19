package seomile.api.program.dto;

import java.util.Date;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
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