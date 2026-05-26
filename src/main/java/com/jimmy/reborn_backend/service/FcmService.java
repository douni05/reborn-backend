package com.jimmy.reborn_backend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    public void sendNotification(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            log.warn("FCM 토큰이 없어서 알림을 보낼 수 없어요");
            return;
        }
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
            log.info("FCM 알림 전송 성공!");
        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: {}", e.getMessage());
        }
    }
}