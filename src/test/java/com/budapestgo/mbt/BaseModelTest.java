package com.budapestgo.mbt;

import com.budapestgo.model.*;
import com.budapestgo.system.BudapestGoSystem;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base class for all GraphWalker model-based tests.
 * Contains common implementation of vertices and edges.
 */
@Model(file = "com/budapestgo/mbt/BudapestGO_Model.json")
public abstract class BaseModelTest extends ExecutionContext implements TicketPurchaseModel {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseModelTest.class);
    
    protected BudapestGoSystem sut;
    protected Random random;
    protected int testCaseCounter = 0;
    protected int edgeCounter = 0;
    protected int vertexCounter = 0;

    /**
     * Initialize the test - called before test execution
     */
    protected void initializeTest() {
        sut = new BudapestGoSystem();
        random = new Random();
        testCaseCounter = 0;
        edgeCounter = 0;
        vertexCounter = 0;
        logger.info("=== Test Initialized ===");
    }

    /**
     * Cleanup after test - called after test execution
     */
    protected void cleanupTest() {
        logger.info("=== Test Completed ===");
        logger.info("Total vertices visited: {}", vertexCounter);
        logger.info("Total edges executed: {}", edgeCounter);
        logger.info("Total test cases: {}", testCaseCounter);
    }

    // ========== VERTEX IMPLEMENTATIONS ==========

    @Override
    public void v_IDLE() {
        vertexCounter++;
        testCaseCounter++;
        logger.info("[VERTEX {}] v_IDLE", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.IDLE, sut.getCurrentState(), 
            "System should be in IDLE state");
        assertNull(sut.getSelectedTicketType(), 
            "No ticket type should be selected in IDLE state");
        assertNull(sut.getSelectedPaymentMethod(), 
            "No payment method should be selected in IDLE state");
        assertNull(sut.getCurrentTicket(), 
            "No ticket should exist in IDLE state");
        assertEquals(0, sut.getRetryCount(), 
            "Retry count should be 0 in IDLE state");
    }

    @Override
    public void v_TICKET_SELECTED() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_TICKET_SELECTED", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.TICKET_SELECTED, sut.getCurrentState(),
            "System should be in TICKET_SELECTED state");
        assertNotNull(sut.getSelectedTicketType(),
            "Ticket type should be selected");
        assertNull(sut.getSelectedPaymentMethod(),
            "Payment method should not be selected yet");
    }

    @Override
    public void v_PAYMENT_METHOD_SELECTED() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_PAYMENT_METHOD_SELECTED", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.PAYMENT_METHOD_SELECTED, sut.getCurrentState(),
            "System should be in PAYMENT_METHOD_SELECTED state");
        assertNotNull(sut.getSelectedTicketType(),
            "Ticket type should be selected");
        assertNotNull(sut.getSelectedPaymentMethod(),
            "Payment method should be selected");
    }

    @Override
    public void v_PAYMENT_PROCESSING() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_PAYMENT_PROCESSING", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.PAYMENT_PROCESSING, sut.getCurrentState(),
            "System should be in PAYMENT_PROCESSING state");
        assertNotNull(sut.getSelectedTicketType(),
            "Ticket type should be selected");
        assertNotNull(sut.getSelectedPaymentMethod(),
            "Payment method should be selected");
    }

    @Override
    public void v_PAYMENT_SUCCESS() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_PAYMENT_SUCCESS", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.PAYMENT_SUCCESS, sut.getCurrentState(),
            "System should be in PAYMENT_SUCCESS state");
        assertNotNull(sut.getTransactionId(),
            "Transaction ID should be generated after successful payment");
        assertTrue(sut.getTransactionId().startsWith("TX-"),
            "Transaction ID should have correct format");
    }

    @Override
    public void v_PAYMENT_FAILED() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_PAYMENT_FAILED", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.PAYMENT_FAILED, sut.getCurrentState(),
            "System should be in PAYMENT_FAILED state");
        assertNull(sut.getTransactionId(),
            "Transaction ID should not exist after failed payment");
        assertTrue(sut.getRetryCount() >= 0 && sut.getRetryCount() <= 3,
            "Retry count should be between 0 and 3");
    }

    @Override
    public void v_QR_GENERATED() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_QR_GENERATED", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.QR_GENERATED, sut.getCurrentState(),
            "System should be in QR_GENERATED state");
        assertNotNull(sut.getCurrentTicket(),
            "Ticket should exist after QR generation");
        assertNotNull(sut.getCurrentTicket().getQrCode(),
            "QR code should be generated");
        assertTrue(sut.getCurrentTicket().getQrCode().startsWith("QR-"),
            "QR code should have correct format");
        assertFalse(sut.getCurrentTicket().isActive(),
            "Ticket should not be active yet");
    }

    @Override
    public void v_TICKET_ACTIVE() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_TICKET_ACTIVE", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.TICKET_ACTIVE, sut.getCurrentState(),
            "System should be in TICKET_ACTIVE state");
        assertNotNull(sut.getCurrentTicket(),
            "Ticket should exist");
        assertTrue(sut.getCurrentTicket().isActive(),
            "Ticket should be active");
        assertNotNull(sut.getCurrentTicket().getValidationTime(),
            "Validation time should be set");
        assertNotNull(sut.getCurrentTicket().getExpiryTime(),
            "Expiry time should be set");
    }

    @Override
    public void v_TICKET_EXPIRED() {
        vertexCounter++;
        logger.info("[VERTEX {}] v_TICKET_EXPIRED", vertexCounter);
        
        // Assertions
        assertEquals(TicketState.TICKET_EXPIRED, sut.getCurrentState(),
            "System should be in TICKET_EXPIRED state");
        assertNotNull(sut.getCurrentTicket(),
            "Ticket should still exist");
        assertFalse(sut.getCurrentTicket().isActive(),
            "Ticket should not be active after expiration");
    }

    // ========== EDGE IMPLEMENTATIONS ==========

    @Override
    public void e_selectTicket() {
        edgeCounter++;
        logger.info("[EDGE {}] e_selectTicket", edgeCounter);
        
        TicketType type = TicketType.random();
        sut.selectTicketType(type);
        
        logger.info("  -> Selected ticket type: {}", type.getDisplayName());
    }

    @Override
    public void e_choosePayment() {
        edgeCounter++;
        logger.info("[EDGE {}] e_choosePayment", edgeCounter);
        
        PaymentMethod method = PaymentMethod.random();
        sut.choosePaymentMethod(method);
        
        logger.info("  -> Selected payment method: {}", method.getDisplayName());
    }

    @Override
    public void e_initiatePayment() {
        edgeCounter++;
        logger.info("[EDGE {}] e_initiatePayment", edgeCounter);
        
        // Only transition to PAYMENT_PROCESSING state
        sut.initiatePaymentProcessing();
        
        logger.info("  -> Payment processing initiated");
    }

    @Override
    public void e_paymentSucceeds() {
        edgeCounter++;
        logger.info("[EDGE {}] e_paymentSucceeds", edgeCounter);
        
        // Complete payment with success
        sut.completePaymentWithSuccess();
        
        logger.info("  -> Payment completed successfully. Transaction ID: {}", 
            sut.getTransactionId());
    }

    @Override
    public void e_paymentFails() {
        edgeCounter++;
        logger.info("[EDGE {}] e_paymentFails", edgeCounter);
        
        // Complete payment with failure
        sut.completePaymentWithFailure();
        
        logger.info("  -> Payment failed. Retry count: {}", sut.getRetryCount());
    }

    @Override
    public void e_generateQR() {
        edgeCounter++;
        logger.info("[EDGE {}] e_generateQR", edgeCounter);
        
        sut.generateQRCode();
        
        logger.info("  -> QR code generated: {}", sut.getCurrentTicket().getQrCode());
    }

    @Override
    public void e_validateTicket() {
        edgeCounter++;
        logger.info("[EDGE {}] e_validateTicket", edgeCounter);
        
        sut.validateTicket();
        
        logger.info("  -> Ticket validated. Expires at: {}", 
            sut.getCurrentTicket().getExpiryTime());
    }

    @Override
    public void e_ticketExpires() {
        edgeCounter++;
        logger.info("[EDGE {}] e_ticketExpires", edgeCounter);
        
        sut.expireTicket();
        
        logger.info("  -> Ticket expired");
    }

    @Override
    public void e_retryPayment() {
        edgeCounter++;
        logger.info("[EDGE {}] e_retryPayment", edgeCounter);
        
        int previousRetryCount = sut.getRetryCount();
        sut.retryPayment();
        
        logger.info("  -> Retry count: {} -> {}", previousRetryCount, sut.getRetryCount());
    }

    @Override
    public void e_cancelPurchase() {
        edgeCounter++;
        logger.info("[EDGE {}] e_cancelPurchase", edgeCounter);
        
        sut.cancelPurchase();
        
        logger.info("  -> Purchase cancelled, system reset");
    }

    @Override
    public void e_reset() {
        edgeCounter++;
        logger.info("[EDGE {}] e_reset", edgeCounter);
        
        sut.reset();
        
        logger.info("  -> System reset after ticket expiration");
    }

    // ========== HELPER METHODS ==========

    /**
     * Get a random ticket type for testing
     */
    protected TicketType getRandomTicketType() {
        return TicketType.random();
    }

    /**
     * Get a random payment method for testing
     */
    protected PaymentMethod getRandomPaymentMethod() {
        return PaymentMethod.random();
    }

    /**
     * Simulate payment success (80% probability)
     */
    protected boolean simulatePaymentSuccess() {
        return random.nextDouble() < 0.8;
    }

    /**
     * Generate a transaction ID
     */
    protected String generateTransactionID() {
        return "TX-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Generate a QR code
     */
    protected String generateQRCode(String transactionID) {
        return "QR-" + java.util.UUID.randomUUID().toString().substring(0, 8) + "-" + transactionID;
    }

    /**
     * Calculate expiry time based on ticket type
     */
    protected long calculateExpiry(TicketType ticketType) {
        return System.currentTimeMillis() + ticketType.getValidityMillis();
    }
}