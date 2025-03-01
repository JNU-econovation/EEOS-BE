// package com.blackcompany.eeos.penalty.application.dto;
//
// import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
// import com.blackcompany.eeos.penalty.application.model.WeightPolicyModel;
// import org.springframework.stereotype.Component;
//
// import java.util.List;
//
// @Component
// public class WeightPolicyConverter implements AbstractDtoConverter<WeightPolicyApplicationDto,
// List<WeightPolicyModel>> {
//
//    public List<WeightPolicyModel> from(WeightPolicyApplicationDto dto) {
//        return dto.getPolicies().stream()
//                .map(this::from)
//                .toList();
//    }
//
//    private WeightPolicyModel from(WeightPolicyApplicationDto.Policy policy) {
//        return WeightPolicyModel.builder()
//                .weightType(policy.getType())
//                .signType(policy.getSign())
//                .score(policy.getPoint())
//                .build();
//    }
//
//    public WeightPolicyApplicationDto.Policy to(WeightPolicyModel model) {
//        return new WeightPolicyApplicationDto.Policy(
//                model.getWeightType(),
//                model.getSignType(),
//                model.getScore()
//        );
//    }
//
//    public WeightPolicyApplicationDto to(List<WeightPolicyModel> models) {
//        return WeightPolicyApplicationDto.builder()
//                .policies(models.stream()
//                        .map(this::to)
//                        .toList())
//                .build();
//    }
// }
