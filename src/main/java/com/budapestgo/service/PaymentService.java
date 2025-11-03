package com.budapestgo.service;

import com.budapestgo.model.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * Service for simulating payment processing.
 * In a real system, this would integrate with actual payment gateways.
 */
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final Random random;
    private static final double DEFAULT_SUCCESS_RATE = 0.8; // 80% success rate
    private final double successRate;

    /**
     * Default constructor with 80% success rate.
     */
    public PaymentService() {
        this(DEFAULT_SUCCESS_RATE);
    }

    /**
     * Constructor with custom success rate (useful for testing).
     *
     * @param successRate Payment success probability (0.0 to 1.0)
     */
    public PaymentService(double successRate) {
        if (successRate < 0.0 || successRate > 1.0) {
            throw new IllegalArgumentException(
                "Success rate must be between 0.0 and 1.0"
            );
        }
        this.random = new Random();
        this.successRate = successRate;
    }

    /**
     * Processes a payment transaction.
     *
     * @param method Payment method to use
     * @param amountHUF Amount to charge in HUF
     * @return PaymentResult containing success status and details
     */
    public PaymentResult processPayment(PaymentMethod method, int amountHUF) {
        logger.info("Processing payment: {} HUF via {}", amountHUF, method.getDisplayName());
        
        // Validate inputs
        if (method == null) {
            logger.error("Payment method is null");
            return new PaymentResult(
                false, 
                null, 
                "Invalid payment method",
                PaymentErrorType.INVALID_METHOD
            );
        }
        
        if (amountHUF <= 0) {
            logger.error("Invalid amount: {}", amountHUF);
            return new PaymentResult(
                false, 
                null, 
                "Invalid amount",
                PaymentErrorType.INVALID_AMOUNT
            );
        }

        // Simulate payment processing delay
        try {
            Thread.sleep(100 + random.nextInt(200)); // 100-300ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment processing interrupted", e);
            return new PaymentResult(
                false, 
                null, 
                "Payment processing interrupted",
                PaymentErrorType.NETWORK_ERROR
            );
        }

        // Simulate payment success/failure
        boolean success = random.nextDouble() < successRate;
        
        if (success) {
            String transactionId = generateTransactionId();
            logger.info("Payment successful. Transaction ID: {}", transactionId);
            return new PaymentResult(
                true, 
                transactionId, 
                "Payment completed successfully",
                null
            );
        } else {
            PaymentErrorType errorType = generateRandomError();
            String errorMessage = getErrorMessage(errorType);
            logger.warn("Payment failed: {}", errorMessage);
            return new PaymentResult(
                false, 
                null, 
                errorMessage,
                errorType
            );
        }
    }

    /**
     * Generates a unique transaction ID.
     *
     * @return transaction ID string
     */
    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Generates a random payment error type.
     *
     * @return random PaymentErrorType
     */
    private PaymentErrorType generateRandomError() {
        PaymentErrorType[] errors = PaymentErrorType.values();
        return errors[random.nextInt(errors.length)];
    }

    /**
     * Gets error message for a specific error type.
     *
     * @param errorType The type of payment error
     * @return error message string
     */
    private String getErrorMessage(PaymentErrorType errorType) {
        switch (errorType) {
            case INSUFFICIENT_FUNDS:
                return "Insufficient funds in account";
            case NETWORK_ERROR:
                return "Network connection error";
            case TIMEOUT:
                return "Payment request timeout";
            case INVALID_CARD:
                return "Invalid card details";
            case DECLINED:
                return "Payment declined by bank";
            default:
                return "Unknown payment error";
        }
    }

    /**
     * Enum representing different types of payment errors.
     */
    public enum PaymentErrorType {
        INSUFFICIENT_FUNDS,
        NETWORK_ERROR,
        TIMEOUT,
        INVALID_CARD,
        DECLINED,
        INVALID_METHOD,
        INVALID_AMOUNT
    }

    /**
     * Class representing the result of a payment transaction.
     */
    public static class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;
        private final PaymentErrorType errorType;

        /**
         * Constructor for PaymentResult.
         *
         * @param success Whether payment was successful
         * @param transactionId Transaction ID (null if failed)
         * @param message Result message
         * @param errorType Type of error (null if successful)
         */
        public PaymentResult(boolean success, String transactionId, 
                           String message, PaymentErrorType errorType) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
            this.errorType = errorType;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMessage() {
            return message;
        }

        public PaymentErrorType getErrorType() {
            return errorType;
        }

        @Override
        public String toString() {
            return String.format("PaymentResult{success=%s, transactionId='%s', message='%s'}",
                success, transactionId, message);
        }
    }
}