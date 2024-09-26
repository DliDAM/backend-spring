package com.dlidam.friend.presentation;

import com.dlidam.authentication.configuration.AuthenticateUser;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.friend.application.FriendService;
import com.dlidam.friend.application.dto.*;
import com.dlidam.friend.presentation.dto.request.FriendIdRequest;
import com.dlidam.friend.presentation.dto.response.AddSuccessResponse;
import com.dlidam.friend.presentation.dto.response.FriendResponse;
import com.dlidam.friend.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "아이디로 사용자 검색")
    @GetMapping("/search")
    public ResponseEntity<UserResponse> getFriend(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            final FriendIdRequest request
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 아아디로 친구 검색 요청이 들어왔습니다", userInfo.userId());
        final UserDto userDto = friendService.getFriend(FriendOperationDto.of(request, userInfo.userId()));
        final UserResponse response = UserResponse.from(userDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이디로 친구 추가")
    @PostMapping("/add")
    public ResponseEntity<AddSuccessResponse> addFriend(
//            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final FriendIdRequest request
    ){
        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 친구 추가 요청이 들어왔습니다.", userInfo.userId());
        final AddSuccessDto addSuccessDto = friendService.addFriend(FriendOperationDto.of(request, userInfo.userId()));
        final AddSuccessResponse response = AddSuccessResponse.from(addSuccessDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친구 목록 조회")
    @GetMapping("/all")
    public ResponseEntity<List<FriendResponse>> getFriends(
//            @AuthenticateUser final AuthenticationUserInfo userInfo
    ){
        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 친구 목록 조회 요청이 들어왔습니다.", userInfo.userId());
        final List<FriendDto> friendDtos = friendService.getFriends(userInfo.userId());
        final List<FriendResponse> response = friendDtos.stream()
                .map(FriendResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    // todo: 친구 즐겨 찾기에 추가 ( POST: /friend/close )

    @Operation(summary = "친구 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriend(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final FriendIdRequest request
    ){
//        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);
        log.info("userId = {}의 친구 삭제 요청이 들어왔습니다.", userInfo.userId());
        friendService.deleteFriend(FriendOperationDto.of(request, userInfo.userId()));
        return ResponseEntity.noContent().build();
    }
}
