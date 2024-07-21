package seomile.api.saveTravel.dto;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTravelDTO {
    private Long bookmarkId;
    private Long userId;
    private String travCode;
}