package com.airbyte.dorm.account;

import com.airbyte.dorm.account.AccountRepository;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.AccountDTO;
import com.airbyte.dorm.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService extends ParentService<Account, AccountRepository , AccountDTO> {
    public AccountService(AccountRepository repository) {
        super(repository);
    }

    @Override
    public Account updateModelFromDto(Account model, AccountDTO dto) {
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        return model;
    }

    @Override
    public Account convertDTO(AccountDTO dto) {
        Account account = new Account();
        account.setName(dto.getName());
        return account;
    }

    @Override
    public List<Account> getWithSearch(AccountDTO search) {
        return null;
    }
}
