package com.jimmy.reborn_backend.global;

import com.jimmy.reborn_backend.domain.entity.DisposalGuide;
import com.jimmy.reborn_backend.domain.repository.DisposalGuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final DisposalGuideRepository disposalGuideRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (disposalGuideRepository.count() > 0) return;

        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("denim").categoryIcon("👖")
                .dischargeMethod("헌 옷 수거함에 배출하세요. 오염이 심한 경우 종량제 봉투에 담아 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("플라스틱").categoryIcon("♻️")
                .dischargeMethod("내용물을 비우고 라벨 제거 후 플라스틱 수거함에 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("유리").categoryIcon("🍶")
                .dischargeMethod("내용물을 비우고 뚜껑 제거 후 유리 수거함에 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("금속").categoryIcon("🥫")
                .dischargeMethod("내용물을 비우고 찌그러뜨린 후 캔 수거함에 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("종이").categoryIcon("📦")
                .dischargeMethod("이물질 제거 후 납작하게 눌러 종이 수거함에 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("나무").categoryIcon("🪵")
                .dischargeMethod("50cm 이하로 잘라 묶어서 대형 폐기물로 신고 후 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("전자제품").categoryIcon("📱")
                .dischargeMethod("주민센터나 대형마트 폐전자제품 수거함에 배출하세요.")
                .build());
        disposalGuideRepository.save(DisposalGuide.builder()
                .materialType("default").categoryIcon("🗑️")
                .dischargeMethod("재질 확인 후 해당 분리배출 방법에 따라 배출하세요.")
                .build());
    }
}