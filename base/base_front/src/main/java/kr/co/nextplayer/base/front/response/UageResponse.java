package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.front.model.Uage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UageResponse {

    List<HashMap<String, Uage>> uageList;

}
