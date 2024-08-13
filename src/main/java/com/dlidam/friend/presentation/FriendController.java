package com.dlidam.friend.presentation;

import com.dlidam.authentication.configuration.AuthenticateUser;
import com.dlidam.authentication.domain.dto.AuthenticationUserInfo;
import com.dlidam.friend.application.FriendService;
import com.dlidam.friend.application.dto.AddFriendDto;
import com.dlidam.friend.application.dto.FriendDto;
import com.dlidam.friend.presentation.dto.request.FriendAddRequest;
import com.dlidam.friend.presentation.dto.request.FriendSearchCondition;
import com.dlidam.friend.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "아이디로 친구 검색")
    @GetMapping("/search")
    public ResponseEntity<UserResponse> getFriend(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final FriendSearchCondition friendSearchCondition
    ){
        log.info("userId = {}의 아아디로 친구 검색 요청이 들어왔습니다", userInfo.userId());
        final FriendDto friendDto = friendService.getFriend(friendSearchCondition);
        final UserResponse response = UserResponse.from(friendDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이디로 친구 추가")
    @PostMapping("/add")
    public ResponseEntity<Void> addFriend(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final FriendAddRequest request
    ){
        log.info("userId = {}의 친구 추가 요청이 들어왔습니다.", userInfo.userId());
        friendService.addFriend(AddFriendDto.of(request, userInfo.userId()));
        return ResponseEntity.noContent().build();
    }

    // todo: 친구 목록 조회 ( GET: /friend/all )
    // todo: 친구 즐겨 찾기에 추가 ( POST: /friend/close )
    // todo: 친구 삭제 ( DELETE: /friend/delete )
}
