package in.zeta.payments.management.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import in.zeta.payments.management.system.enums.Category;
import in.zeta.payments.management.system.enums.PaymentStatus;
import in.zeta.payments.management.system.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private Long paymentId;
    private PaymentType paymentType;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private PaymentStatus status;
    private Category category;
}
