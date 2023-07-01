package tecsor.andrei.dissertation.requester.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RiskDTO {
    private int score;
    private String status;
}
