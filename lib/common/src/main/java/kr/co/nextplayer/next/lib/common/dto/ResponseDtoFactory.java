package kr.co.nextplayer.next.lib.common.dto;

public class ResponseDtoFactory {
    public static <T> ResponseDto<PageDataDto<T>> createPageDataResponse(T data, int totalCount) {
        var pageDataDto = PageDataDto.<T>builder()
                .dataList(data)
                .totalCount(totalCount)
                .build();

        var responseDto = ResponseDto.<PageDataDto<T>>builder()
                .data(pageDataDto)
                .build();

        return responseDto;
    }

    public static <T> ResponseDto<T> createResponse(T data) {
        var responseDto = ResponseDto.<T>builder()
            .data(data)
            .build();

        return responseDto;
    }
}
