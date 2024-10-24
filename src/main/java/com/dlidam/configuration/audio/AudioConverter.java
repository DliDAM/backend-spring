package com.dlidam.configuration.audio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;


@Slf4j
@Component
@RequiredArgsConstructor
public class AudioConverter {

    private static final int SAMPLE_RATE = 24000;

    public byte[] convertToWav(byte[] rawData) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // WAV 헤더 작성
            writeWavHeader(outputStream, rawData.length);

            // 오디오 데이터 작성
            outputStream.write(rawData);

            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error converting audio data", e);
            throw new RuntimeException("Failed to convert audio data", e);
        }
    }

    private void writeWavHeader(ByteArrayOutputStream outputStream, int audioDataLength) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(44);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int SAMPLE_RATE = 24000;

        // RIFF 헤더
        buffer.put("RIFF".getBytes());
        buffer.putInt(36 + audioDataLength);  // 전체 파일 크기 - 8
        buffer.put("WAVE".getBytes());

        // fmt 청크
        buffer.put("fmt ".getBytes());
        buffer.putInt(16);                   // fmt 청크 크기
        buffer.putShort((short) 3);          // 포맷 (3 = IEEE float)
        buffer.putShort((short) 1);          // 채널 수 (모노)
        buffer.putInt(SAMPLE_RATE);          // 샘플레이트
        buffer.putInt(SAMPLE_RATE * 4);      // 바이트레이트 (24000 * 4 bytes per sample)
        buffer.putShort((short) 4);          // 블록 얼라인 (채널 수 * bytes per sample)
        buffer.putShort((short) 32);         // 비트 per 샘플

        // data 청크
        buffer.put("data".getBytes());
        buffer.putInt(audioDataLength);

        outputStream.write(buffer.array());
    }

    // 디버깅을 위한 메서드
    private void logWavHeader(byte[] wavData) {
        if (wavData.length < 44) {
            log.warn("WAV data too short");
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(wavData).order(ByteOrder.LITTLE_ENDIAN);
        log.info("WAV Header Analysis:");
        log.info("RIFF header: {}", new String(wavData, 0, 4));
        log.info("File size: {}", buffer.getInt(4));
        log.info("WAVE marker: {}", new String(wavData, 8, 4));
        log.info("Format length: {}", buffer.getInt(16));
        log.info("Format type: {}", buffer.getShort(20));
        log.info("Channels: {}", buffer.getShort(22));
        log.info("Sample rate: {}", buffer.getInt(24));
        log.info("Byte rate: {}", buffer.getInt(28));
        log.info("Block align: {}", buffer.getShort(32));
        log.info("Bits per sample: {}", buffer.getShort(34));
        log.info("Data marker: {}", new String(wavData, 36, 4));
        log.info("Data length: {}", buffer.getInt(40));
    }
}
