//package com.airbyte.dorm.insurance;
/*
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.InsuranceDTO;
import com.airbyte.dorm.human.HumanService;
import com.airbyte.dorm.model.Insurance;
import com.airbyte.dorm.model.PaymentHistory;
import com.airbyte.dorm.paymentHistory.PaymentHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsuranceService extends ParentService <Insurance , InsuranceRepository , InsuranceDTO> {

    private final HumanService humanService;
    private final PaymentHistoryService paymentHistoryService;

    public InsuranceService(InsuranceRepository repository, HumanService humanService, PaymentHistoryService paymentHistoryService) {
        super(repository);
        this.humanService = humanService;
        this.paymentHistoryService = paymentHistoryService;
    }

    @Override
    public Insurance updateModelFromDto(Insurance model, InsuranceDTO dto) {
        model.setEmployer(dto.getEmployer() != null ? dto.getEmployer() : model.getEmployer());
        model.setInsuranceCode(dto.getInsuranceCode() != null ? dto.getInsuranceCode() : model.getInsuranceCode());
        model.setInsurerName(dto.getInsurerName() != null ? dto.getInsurerName() : model.getInsurerName());
        model.setInsurerOrganization(dto.getInsurerOrganization() != null ? dto.getInsurerOrganization() : model.getInsurerOrganization());
//        model.setHuman(dto.getHumanId() != null ? humanService.getOne(dto.getHumanId()) : model.getHuman());
        if (dto.getPaymentHistoryList() != null) {
            List<PaymentHistory> result = new ArrayList<>();
            for (String paymentHistory : dto.getPaymentHistoryList()) {
                result.add(paymentHistoryService.getOne(paymentHistory));
            }
            model.setPaymentHistoryList(result);
        }
        else {
            model.setPaymentHistoryList(null);
        }
        return model;
    }

    @Override
    public Insurance convertDTO(InsuranceDTO dto) {
        Insurance insurance = new Insurance();
        insurance.setInsurerOrganization(dto.getInsurerOrganization());
        insurance.setInsurerName(dto.getInsurerName());
        insurance.setInsuranceCode(dto.getInsuranceCode());
//        insurance.setHuman(dto.getHumanId() != null ? humanService.getOne(dto.getHumanId()) : null);
        if (dto.getPaymentHistoryList() != null) {
            List<PaymentHistory> result = new ArrayList<>();
            for (String paymentHistory : dto.getPaymentHistoryList()) {
                result.add(paymentHistoryService.getOne(paymentHistory));
            }
            insurance.setPaymentHistoryList(result);
        }
        else {
            insurance.setPaymentHistoryList(null);
        }
        return insurance;
    }

    @Override
    public List<Insurance> getWithSearch(InsuranceDTO search) {
        return null;
    }
}*/
