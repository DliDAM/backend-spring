package com.dlidam.friend.presentation.dto.response;

import com.dlidam.friend.application.dto.AddSuccessDto;

public record AddSuccessResponse(
        boolean isSuccess,
        String message
) {


    public static AddSuccessResponse from(AddSuccessDto addSuccessDto) {
        if(addSuccessDto.isSuccess()) {
            return new AddSuccessResponse(true, "친구 추가가 성공적으로 수행되었습니다.");
        }
        return new AddSuccessResponse(false, "이미 친구인 사용자 입니다.");
    }
}
