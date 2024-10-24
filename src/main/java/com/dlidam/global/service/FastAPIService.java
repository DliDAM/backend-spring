package com.dlidam.global.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FastAPIService {

    @Transactional
    public String sendToFastServer(MultipartFile voiceFile, Long userId) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 헤더 설정: multipart/form-data 형식으로 전송
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultiValueMap을 사용해 multipart/form-data 형식의 데이터를 준비
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(voiceFile.getBytes()) {
                @Override
                public String getFilename() {
                    return voiceFile.getOriginalFilename();  // 파일 이름 제공
                }
            });
            // userId 추가 (문자열로 변환)
            body.add("senderId", userId.toString());

            // 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // FastAPI 서버로 요청을 전송할 URL
            String url = "http://43.200.250.247:8000/receive_audio_file";

            // 서버에 POST 요청을 보내고 응답을 문자열로 받음
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // 상태코드 확인 후, 응답 본문 반환
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("FastAPI 서버로부터 응답을 받지 못했습니다. 상태 코드: " + response.getStatusCode());
            }

        } catch (IOException e) {
            throw new RuntimeException("FastAPI 서버로 음성 파일을 보내는 중 에러 발생", e);
        }
    }
}
