package com.budapestgo.system;

import com.budapestgo.model.*;
import com.budapestgo.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main system class implementing the FSM for BudapestGO ticket purchase.
 * This class manages the state transitions and business logic.
 */
public class BudapestGoSystem {
    private static final Logger logger = LoggerFactory.getLogger(BudapestGoSystem.class);
    private static final int MAX_RETRY_COUNT = 3;

    // Current FSM state
    private TicketState currentState;
    
    // State variables (EFSM)
    private TicketType selectedTicketType;
    private PaymentMethod selectedPaymentMethod;
    private String transactionId;
    private Ticket currentTicket;
    private int retryCount;
    
    // Services
    private final PaymentService paymentService;

    /**
     * Default constructor with default payment service.
     */
    public BudapestGoSystem() {
        this(new PaymentService());
    }

    /**
     * Constructor with custom payment service (useful for testing).
     *
     * @param paymentService Custom payment service instance
     */
    public BudapestGoSystem(PaymentService paymentService) {
        this.paymentService = paymentService;
        this.currentState = TicketState.IDLE;
        this.retryCount = 0;
        logger.info("BudapestGoSystem initialized in state: {}", currentState);
    }

    // ========== STATE TRANSITIONS ==========

    /**
     * Transition: IDLE -> TICKET_SELECTED
     * User selects a ticket type.
     *
     * @param type The ticket type to select
     * @throws IllegalStateException if not in IDLE state
     * @throws IllegalArgumentException if type is null
     */
    public void selectTicketType(TicketType type) {
        logger.info("selectTicketType called with type: {}", type);
        
        if (currentState != TicketState.IDLE) {
            throw new IllegalStateException(
                String.format("Cannot select ticket in state: %s. Expected: IDLE", currentState)
            );
        }
        
        if (type == null) {
            throw new IllegalArgumentException("Ticket type cannot be null");
        }
        
        this.selectedTicketType = type;
        this.currentState = TicketState.TICKET_SELECTED;
        
        logger.info("Ticket type selected: {}. State changed to: {}", 
            type.getDisplayName(), currentState);
    }

    /**
     * Transition: TICKET_SELECTED -> PAYMENT_METHOD_SELECTED
     * User chooses a payment method.
     *
     * @param method The payment method to use
     * @throws IllegalStateException if not in TICKET_SELECTED state or no ticket selected
     * @throws IllegalArgumentException if method is null
     */
    public void choosePaymentMethod(PaymentMethod method) {
        logger.info("choosePaymentMethod called with method: {}", method);
        
        if (currentState != TicketState.TICKET_SELECTED) {
            throw new IllegalStateException(
                String.format("Cannot choose payment in state: %s. Expected: TICKET_SELECTED", 
                    currentState)
            );
        }
        
        if (selectedTicketType == null) {
            throw new IllegalStateException("No ticket type selected");
        }
        
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        
        this.selectedPaymentMethod = method;
        this.currentState = TicketState.PAYMENT_METHOD_SELECTED;
        
        logger.info("Payment method selected: {}. State changed to: {}", 
            method.getDisplayName(), currentState);
    }

    /**
     * Transition: PAYMENT_METHOD_SELECTED -> PAYMENT_PROCESSING -> PAYMENT_SUCCESS/FAILED
     * Processes the payment transaction.
     *
     * @return true if payment successful, false otherwise
     * @throws IllegalStateException if not in PAYMENT_METHOD_SELECTED state
     */
    public boolean processPayment() {
        logger.info("processPayment called");
        
        if (currentState != TicketState.PAYMENT_METHOD_SELECTED) {
            throw new IllegalStateException(
                String.format("Cannot process payment in state: %s. Expected: PAYMENT_METHOD_SELECTED", 
                    currentState)
            );
        }
        
        if (selectedPaymentMethod == null) {
            throw new IllegalStateException("No payment method selected");
        }
        
        if (selectedTicketType == null) {
            throw new IllegalStateException("No ticket type selected");
        }
        
        // Transition to PAYMENT_PROCESSING
        this.currentState = TicketState.PAYMENT_PROCESSING;
        logger.info("State changed to: {}", currentState);
        
        // Process payment
        PaymentService.PaymentResult result = paymentService.processPayment(
            selectedPaymentMethod,
            selectedTicketType.getPriceHUF()
        );
        
        if (result.isSuccess()) {
            // Transition to PAYMENT_SUCCESS
            this.transactionId = result.getTransactionId();
            this.currentState = TicketState.PAYMENT_SUCCESS;
            this.retryCount = 0; // Reset retry counter on success
            
            logger.info("Payment successful. Transaction ID: {}. State changed to: {}", 
                transactionId, currentState);
            return true;
        } else {
            // Transition to PAYMENT_FAILED
            this.currentState = TicketState.PAYMENT_FAILED;
            
            logger.warn("Payment failed: {}. State changed to: {}", 
                result.getMessage(), currentState);
            return false;
        }
    }

    /**
     * Transition: PAYMENT_METHOD_SELECTED -> PAYMENT_PROCESSING
     * Initiates payment processing without completing it yet.
     *
     * @throws IllegalStateException if not in PAYMENT_METHOD_SELECTED state
     */
    public void initiatePaymentProcessing() {
        logger.info("initiatePaymentProcessing called");
        
        if (currentState != TicketState.PAYMENT_METHOD_SELECTED) {
            throw new IllegalStateException(
                String.format("Cannot initiate payment in state: %s. Expected: PAYMENT_METHOD_SELECTED", 
                    currentState)
            );
        }
        
        if (selectedPaymentMethod == null) {
            throw new IllegalStateException("No payment method selected");
        }
        
        if (selectedTicketType == null) {
            throw new IllegalStateException("No ticket type selected");
        }
        
        // Transition to PAYMENT_PROCESSING
        this.currentState = TicketState.PAYMENT_PROCESSING;
        logger.info("State changed to: {}", currentState);
    }

    /**
     * Complete payment with success.
     * Transition: PAYMENT_PROCESSING -> PAYMENT_SUCCESS
     *
     * @throws IllegalStateException if not in PAYMENT_PROCESSING state
     */
    public void completePaymentWithSuccess() {
        logger.info("completePaymentWithSuccess called");
        
        if (currentState != TicketState.PAYMENT_PROCESSING) {
            throw new IllegalStateException(
                String.format("Cannot complete payment in state: %s. Expected: PAYMENT_PROCESSING", 
                    currentState)
            );
        }
        
        // Process payment through service
        PaymentService.PaymentResult result = paymentService.processPayment(
            selectedPaymentMethod,
            selectedTicketType.getPriceHUF()
        );
        
        if (result.isSuccess()) {
            this.transactionId = result.getTransactionId();
            this.currentState = TicketState.PAYMENT_SUCCESS;
            this.retryCount = 0; // Reset retry counter on success
            
            logger.info("Payment successful. Transaction ID: {}. State changed to: {}", 
                transactionId, currentState);
        } else {
            throw new RuntimeException("Payment service returned failure when expecting success: " + 
                result.getMessage());
        }
    }

    /**
     * Complete payment with failure.
     * Transition: PAYMENT_PROCESSING -> PAYMENT_FAILED
     *
     * @throws IllegalStateException if not in PAYMENT_PROCESSING state
     */
    public void completePaymentWithFailure() {
        logger.info("completePaymentWithFailure called");
        
        if (currentState != TicketState.PAYMENT_PROCESSING) {
            throw new IllegalStateException(
                String.format("Cannot fail payment in state: %s. Expected: PAYMENT_PROCESSING", 
                    currentState)
            );
        }
        
        // Transition to PAYMENT_FAILED
        this.currentState = TicketState.PAYMENT_FAILED;
        this.retryCount++;
        
        logger.warn("Payment failed. Retry count: {}. State changed to: {}", 
            retryCount, currentState);
    }

    /**
     * Transition: PAYMENT_FAILED -> PAYMENT_METHOD_SELECTED (retry)
     * Allows user to retry payment after failure.
     *
     * @throws IllegalStateException if not in PAYMENT_FAILED state or max retries exceeded
     */
    public void retryPayment() {
        logger.info("retryPayment called (attempt {})", retryCount + 1);
        
        if (currentState != TicketState.PAYMENT_FAILED) {
            throw new IllegalStateException(
                String.format("Cannot retry payment in state: %s. Expected: PAYMENT_FAILED", 
                    currentState)
            );
        }
        
        if (retryCount >= MAX_RETRY_COUNT) {
            logger.error("Maximum retry attempts ({}) exceeded", MAX_RETRY_COUNT);
            // Automatically reset system after max retries
            resetSystem();
            throw new IllegalStateException(
                String.format("Maximum retry attempts (%d) exceeded. Purchase cancelled.", 
                    MAX_RETRY_COUNT)
            );
        }
        
        this.retryCount++;
        this.currentState = TicketState.PAYMENT_METHOD_SELECTED;
        
        logger.info("Retry count: {}. State changed to: {}", retryCount, currentState);
    }

    /**
     * Transition: PAYMENT_SUCCESS -> QR_GENERATED
     * Generates QR code for the purchased ticket.
     *
     * @throws IllegalStateException if not in PAYMENT_SUCCESS state or no transaction ID
     */
    public void generateQRCode() {
        logger.info("generateQRCode called");
        
        if (currentState != TicketState.PAYMENT_SUCCESS) {
            throw new IllegalStateException(
                String.format("Cannot generate QR in state: %s. Expected: PAYMENT_SUCCESS", 
                    currentState)
            );
        }
        
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalStateException("No transaction ID available");
        }
        
        if (selectedTicketType == null) {
            throw new IllegalStateException("No ticket type selected");
        }
        
        // Create ticket with QR code
        this.currentTicket = new Ticket(selectedTicketType, transactionId);
        this.currentState = TicketState.QR_GENERATED;
        
        logger.info("QR code generated: {}. State changed to: {}", 
            currentTicket.getQrCode(), currentState);
    }

    /**
     * Transition: QR_GENERATED -> TICKET_ACTIVE
     * Validates (activates) the ticket.
     *
     * @throws IllegalStateException if not in QR_GENERATED state or no ticket available
     */
    public void validateTicket() {
        logger.info("validateTicket called");
        
        if (currentState != TicketState.QR_GENERATED) {
            throw new IllegalStateException(
                String.format("Cannot validate ticket in state: %s. Expected: QR_GENERATED", 
                    currentState)
            );
        }
        
        if (currentTicket == null) {
            throw new IllegalStateException("No ticket available");
        }
        
        // Validate the ticket
        currentTicket.validate();
        this.currentState = TicketState.TICKET_ACTIVE;
        
        logger.info("Ticket validated. Expires at: {}. State changed to: {}", 
            currentTicket.getExpiryTime(), currentState);
    }

    /**
     * Transition: TICKET_ACTIVE -> TICKET_EXPIRED
     * Expires the active ticket.
     *
     * @throws IllegalStateException if not in TICKET_ACTIVE state or no active ticket
     */
    public void expireTicket() {
        logger.info("expireTicket called");
        
        if (currentState != TicketState.TICKET_ACTIVE) {
            throw new IllegalStateException(
                String.format("Cannot expire ticket in state: %s. Expected: TICKET_ACTIVE", 
                    currentState)
            );
        }
        
        if (currentTicket == null || !currentTicket.isActive()) {
            throw new IllegalStateException("No active ticket to expire");
        }
        
        // Expire the ticket
        currentTicket.expire();
        this.currentState = TicketState.TICKET_EXPIRED;
        
        logger.info("Ticket expired. State changed to: {}", currentState);
    }

    /**
     * Transition: TICKET_SELECTED/PAYMENT_METHOD_SELECTED/PAYMENT_FAILED -> IDLE
     * Cancels the purchase process.
     *
     * @throws IllegalStateException if cancellation not allowed in current state
     */
    public void cancelPurchase() {
        logger.info("cancelPurchase called from state: {}", currentState);
        
        if (currentState != TicketState.TICKET_SELECTED &&
            currentState != TicketState.PAYMENT_METHOD_SELECTED &&
            currentState != TicketState.PAYMENT_FAILED) {
            throw new IllegalStateException(
                String.format("Cannot cancel purchase in state: %s", currentState)
            );
        }
        
        resetSystem();
        logger.info("Purchase cancelled. System reset.");
    }

    /**
     * Transition: TICKET_EXPIRED -> IDLE
     * Resets the system after ticket expiration.
     *
     * @throws IllegalStateException if not in TICKET_EXPIRED state
     */
    public void reset() {
        logger.info("reset called from state: {}", currentState);
        
        if (currentState != TicketState.TICKET_EXPIRED) {
            throw new IllegalStateException(
                String.format("Cannot reset in state: %s. Expected: TICKET_EXPIRED", 
                    currentState)
            );
        }
        
        resetSystem();
        logger.info("System reset after ticket expiration.");
    }

    /**
     * Internal method to reset all system variables to initial state.
     */
    private void resetSystem() {
        this.selectedTicketType = null;
        this.selectedPaymentMethod = null;
        this.transactionId = null;
        this.currentTicket = null;
        this.retryCount = 0;
        this.currentState = TicketState.IDLE;
        
        logger.debug("System variables reset. State: {}", currentState);
    }

    // ========== GETTERS (for testing) ==========

    public TicketState getCurrentState() {
        return currentState;
    }

    public TicketType getSelectedTicketType() {
        return selectedTicketType;
    }

    public PaymentMethod getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Checks if the system is in a specific state.
     *
     * @param state The state to check
     * @return true if system is in the specified state
     */
    public boolean isInState(TicketState state) {
        return this.currentState == state;
    }

    @Override
    public String toString() {
        return String.format("BudapestGoSystem{state=%s, ticketType=%s, paymentMethod=%s, " +
                "transactionId='%s', hasTicket=%s, retryCount=%d}",
            currentState,
            selectedTicketType != null ? selectedTicketType.getDisplayName() : "null",
            selectedPaymentMethod != null ? selectedPaymentMethod.getDisplayName() : "null",
            transactionId,
            currentTicket != null,
            retryCount
        );
    }
}