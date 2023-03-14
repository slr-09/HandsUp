package com.back.handsUp.utils;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.fcmToken.FcmToken;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.repository.fcm.FcmTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final FcmTokenRepository fcmTokenRepository;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/handsup-9c9e7/messages:send";

    private final ObjectMapper objectMapper;


    /**
     * 알림 전송 메서드
     * 알림 전송 필요 시 sendMessageTo(상대방 토큰, 알림 제목, 알림 내용) 호출해 사용
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL).post(requestBody)
                .addHeader(
                        HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8").build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }



    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(
                        FcmMessage.Message.builder()
                                .token(targetToken).notification(
                                        FcmMessage.Notification.builder().title(title).body(body).image(null).build())
                                .build())
                .validate_only(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/handsup-9c9e7-firebase-adminsdk-ow0l5-6bb4cd6c8c.json";

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath)
                .getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

    //토큰 엔티티 등록
    public void saveToken(String fcmToken, User user) throws BaseException {
        FcmToken fcmTokenEntity = FcmToken.builder()
                .fcmToken(fcmToken)
                .user(user).build();
        try {
            fcmTokenRepository.save(fcmTokenEntity);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }

    //토큰 엔티티 삭제
    public void deleteToken(User user) throws BaseException {
        Optional<FcmToken> optionalFcmTokenEntity = fcmTokenRepository.findFcmTokenByUser(user);
        if (optionalFcmTokenEntity.isEmpty()) {
            return;
        }
        FcmToken fcmTokenEntity = optionalFcmTokenEntity.get();
        try {
            fcmTokenRepository.delete(fcmTokenEntity);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_DELETE_ERROR);
        }
    }

    //front 에서 보낸 fcm 토큰과 서버의 Fcm 토큰이 맞는 지 확인
    public boolean checkToken(String fcmToken, User user) throws BaseException {
        Optional<FcmToken> optionalFcmTokenEntity = fcmTokenRepository.findFcmTokenByUser(user);
        if (optionalFcmTokenEntity.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_FCMTOKEN);
        }
        FcmToken fcmTokenEntity = optionalFcmTokenEntity.get();

        return Objects.equals(fcmTokenEntity.getFcmToken(), fcmToken);
    }

    //토큰이 없으면 save, 있지만 다르면 수정, 있고 같으면 아무일 없음.
    public void overWriteToken(String fcmToken, User user) throws BaseException {
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findFcmTokenByUser(user);
        if (optionalFcmToken.isEmpty()) {
            saveToken(fcmToken, user);
        } else if (!checkToken(fcmToken, user)) {
            optionalFcmToken.get().updateToken(fcmToken);
        }
    }
}
