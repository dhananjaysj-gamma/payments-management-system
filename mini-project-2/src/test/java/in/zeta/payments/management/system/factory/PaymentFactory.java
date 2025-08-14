package in.zeta.payments.management.system.factory;

import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.enums.Category;
import in.zeta.payments.management.system.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentFactory {


    public static final Long PAYMENT_ID = 1000001L;
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(1000);
    public static final Category CATEGORY_SALARY_PAYMENT = Category.SALARY_PAYMENT;
    public static final Category CATEGORY_VENDOR_PAYMENT = Category.VENDOR_PAYMENT;
    public static final Category CATEGORY_INVOICE_PAYMENT = Category.INVOICE_PAYMENT;
    public static final PaymentType PAYMENT_TYPE_INCOMING= PaymentType.INCOMING;
    public static final PaymentType PAYMENT_TYPE_OUTGOING= PaymentType.OUTGOING;
    public static final LocalDate PAYMENT_DATE = LocalDate.now();


    public static Payment getPayment() {
        Payment payment = new Payment();
        payment.setAmount(AMOUNT);
        payment.setCategory(CATEGORY_SALARY_PAYMENT);
        payment.setPaymentType(PAYMENT_TYPE_INCOMING);
        payment.setPaymentDate(PAYMENT_DATE);
        return payment;
    }
}