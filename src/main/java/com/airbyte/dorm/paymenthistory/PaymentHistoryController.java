package com.airbyte.dorm.paymenthistory;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.PaymentHistoryDTO;
import com.airbyte.dorm.dto.PaymentHistoryQueryDTO;
import com.airbyte.dorm.model.PaymentHistory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/paymentHistory")
@CrossOrigin("*")
public class PaymentHistoryController extends ParentController<PaymentHistory, PaymentHistoryService, PaymentHistoryDTO> {
    public PaymentHistoryController(PaymentHistoryService service) {
        super(service);
    }

    @PostMapping("/filter")
    public List<PaymentHistory> paymentHistoryList(HttpServletResponse response, Authentication authentication, @RequestBody PaymentHistoryQueryDTO dto) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return service.getAllBySpecificQueryByTimePeriod(dto);
    }

}
