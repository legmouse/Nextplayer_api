package kr.co.nextplayer.base.front.response.board;

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
public class ColumnListResponse {

    int totalCount;
    List<HashMap<String, Object>> menuList;
    List<ColumnListDto> columnList;

}
