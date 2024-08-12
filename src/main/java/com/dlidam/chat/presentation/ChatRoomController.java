package com.dlidam.chat.presentation;

import com.dlidam.authentication.configuration.AuthenticateUser;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.application.ChatRoomService;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.presentation.dto.request.CreateChatRoomRequestDTO;
import com.dlidam.chat.presentation.dto.response.CreateChatRoomResponseDTO;
import com.dlidam.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @PostMapping("/chat/connect")
    @Operation(summary = "사용자가 통화버튼 눌렀을 때", description = "사용자가 통화버튼 눌렀을때 user1Connect만 True api")
    public ResponseEntity<CreateChatRoomResponseDTO> createChatRoom(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid CreateChatRoomRequestDTO request
    ){
        ChatRoom chatRoom = chatRoomService.findChatRoomBySenderIdAndReceiverId(userInfo.userId(), request.getReceiverId());

        if(chatRoom == null){
            chatRoom = chatRoomService.createChatRoom(userInfo.userId(), request.getReceiverId());
        }
        CreateChatRoomResponseDTO response = new CreateChatRoomResponseDTO(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/chat/connect/accept")
    @Operation(summary = "사용자가 통화수락버튼 눌렀을 때",
            description = "사용자가 통화알림이  오고 통화수락을 눌렀을때 user2Connect True api")
    public ResponseEntity<CreateChatRoomResponseDTO> acceptCall(
            @AuthenticateUser final  AuthenticationUserInfo userInfo,
            @RequestBody @ Valid CreateChatRoomRequestDTO request
    ){
        ChatRoom chatRoom = chatRoomService.findChatRoomBySenderIdAndReceiverId(userInfo.userId(), request.getReceiverId());

        if(chatRoom.getUser2Connect() == false){
            chatRoom.setUser2ConnectTrue();
        }
        CreateChatRoomResponseDTO response = new CreateChatRoomResponseDTO(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @GetMapping("/user/chatrooms")
//    @Operation(summary = "유저의 채팅방 목록 보여주기", description = "유저의 채팅리스트 보여주기 api")
//    public ResponseEntity<List<ChatRoomSimpleDTO>> userChatRoomList(@AuthenticateUser final  AuthenticationUserInfo userInfo) {
//
//         User user = userService.findUSerById(userInfo.userId());
//        List<ChatRoom> chatRoomsList = chatRoomService.findAllChatRoomListByUser(user);
//
//        chatRoomService.setLastChatMessage(chatRoomsList);
//        List<ChatRoomSimpleDTO> chatRoomSimpleDTOList = ChatRoomSimpleDTO.toChatRoomSimpleListDTO(chatRoomsList);
//        chatMessageService.sortChatMessage(chatRoomSimpleDTOList);
//
//        List<ChatRoomSimpleDTO> response = chatRoomSimpleDTOList;
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

}
