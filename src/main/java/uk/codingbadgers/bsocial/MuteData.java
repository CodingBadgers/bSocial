package uk.codingbadgers.bsocial;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author james
 */
@Data
@AllArgsConstructor
public class MuteData {
    
    private long endtime;
    private String admin;
    private String reason;
    
}
