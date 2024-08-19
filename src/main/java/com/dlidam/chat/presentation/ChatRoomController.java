package com.dlidam.chat.presentation;

import com.dlidam.authentication.configuration.AuthenticateUser;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.chat.application.ChatMessageService;
import com.dlidam.chat.application.ChatRoomService;
import com.dlidam.chat.domain.ChatRoom;
import com.dlidam.chat.dto.request.CreateChatRoomRequestDTO;
import com.dlidam.chat.dto.response.ChatRoomWithLastMessageResponseDTO;
import com.dlidam.chat.dto.response.ChatRoomResponseDTO;
import com.dlidam.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @PostMapping("/connect")
    @Operation(summary = "사용자 통화 요청 API", description = "해당 API 요청 시 채팅방 생성 및 상대방에게 알림")
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateChatRoomRequestDTO request
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 통화 요청이 들어왔습니다.", userInfo.userId());
        final ChatRoom chatRoom = chatRoomService.getSenderChatRoom(userInfo.userId(), request.getFriendId());
        // todo: 상대방에게 알림 표시
        final ChatRoomResponseDTO response = chatRoomService.getSenderMessages(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/connect/accept")
    @Operation(summary = "사용자 통화 수락 API", description = "해당 API 요청 시 채팅방 입장 및 상대방에게 알림")
    public ResponseEntity<ChatRoomResponseDTO> acceptCall(
            @AuthenticateUser final  AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateChatRoomRequestDTO request
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(2L);
        log.info("userId = {}의 통화 수락 요청이 들어왔습니다.", userInfo.userId());
        final ChatRoom chatRoom = chatRoomService.getReceiverChatRoom(userInfo.userId(), request.getFriendId());
        // todo: 상대방에게 알림 표시
        final ChatRoomResponseDTO response = chatRoomService.getReceiverMessages(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @Operation(summary = "사용자 채팅방 목록 조회 API")
    public ResponseEntity<List<ChatRoomWithLastMessageResponseDTO>> getChatRooms(
            @AuthenticateUser final AuthenticationUserInfo userInfo
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 채팅방 목록 요청이 들어왔습니다.", userInfo.userId());
        final List<ChatRoomWithLastMessageResponseDTO> responses = chatRoomService.getAllChatRooms(userInfo.userId());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    // todo: 사용자 채팅방 채팅 내역 조회 API ( GET: /chat/{chatRoomId} )

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

//    @PostMapping("/send")
//    @Operation(summary = "사용자 채팅 입력 API")
//    public ResponseEntity<MessageResponse> send

}
