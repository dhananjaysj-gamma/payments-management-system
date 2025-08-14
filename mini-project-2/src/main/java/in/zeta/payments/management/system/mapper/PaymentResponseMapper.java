package in.zeta.payments.management.system.mapper;

import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponseMapper {
    public static PaymentResponse mapToPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentID())
                .paymentType(payment.getPaymentType())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .category(payment.getCategory())
                .build();

    }
}
