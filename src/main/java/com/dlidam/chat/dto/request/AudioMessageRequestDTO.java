package com.dlidam.chat.dto.request;

import com.dlidam.user.domain.User;
import com.dlidam.user.domain.VoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AudioMessageRequestDTO {

    private final Long chatRoomId;
    private final String senderId;
    private final VoiceType voiceType;
    private final String message;

    public static AudioMessageRequestDTO from(final ChatMessageRequestDTO chatMessageRequestDTO, final User user) {
        return new AudioMessageRequestDTO(
                chatMessageRequestDTO.getChatRoomId(),
                chatMessageRequestDTO.getSenderId(),
                user.getVoiceType(),
                chatMessageRequestDTO.getMessage()
        );
    }
}
