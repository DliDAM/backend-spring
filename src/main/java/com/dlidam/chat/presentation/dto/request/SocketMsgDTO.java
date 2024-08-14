package com.dlidam.chat.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMsgDTO {
    String message;

    @Override
    public String toString(){
        return message;
    }
}

