package kr.co.nextplayer.base.front.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Uage {

    private String uage;

    private String area_id;
    private String area_name;
    private int interest_flag;
}
