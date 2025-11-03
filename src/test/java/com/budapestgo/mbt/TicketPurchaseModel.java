package com.budapestgo.mbt;

import org.graphwalker.java.annotation.GraphWalker;

/**
 * GraphWalker interface for the BudapestGO Ticket Purchase Model.
 * This interface defines all vertices (states) and edges (transitions)
 * that must be implemented by the test class.
 */
@GraphWalker(value = "random(edge_coverage(100))")
public interface TicketPurchaseModel {

    // ========== VERTICES (States) ==========
    
    /**
     * Initial state - system is idle, ready for new purchase
     */
    void v_IDLE();
    
    /**
     * User has selected a ticket type
     */
    void v_TICKET_SELECTED();
    
    /**
     * User has chosen a payment method
     */
    void v_PAYMENT_METHOD_SELECTED();
    
    /**
     * Payment is being processed
     */
    void v_PAYMENT_PROCESSING();
    
    /**
     * Payment completed successfully
     */
    void v_PAYMENT_SUCCESS();
    
    /**
     * Payment failed
     */
    void v_PAYMENT_FAILED();
    
    /**
     * QR code has been generated for the ticket
     */
    void v_QR_GENERATED();
    
    /**
     * Ticket is active and valid for use
     */
    void v_TICKET_ACTIVE();
    
    /**
     * Ticket has expired
     */
    void v_TICKET_EXPIRED();

    // ========== EDGES (Transitions) ==========
    
    /**
     * Transition: IDLE -> TICKET_SELECTED
     * User selects a ticket type
     */
    void e_selectTicket();
    
    /**
     * Transition: TICKET_SELECTED -> PAYMENT_METHOD_SELECTED
     * User chooses payment method
     */
    void e_choosePayment();
    
    /**
     * Transition: PAYMENT_METHOD_SELECTED -> PAYMENT_PROCESSING
     * System initiates payment processing
     */
    void e_initiatePayment();
    
    /**
     * Transition: PAYMENT_PROCESSING -> PAYMENT_SUCCESS
     * Payment processing succeeds
     */
    void e_paymentSucceeds();
    
    /**
     * Transition: PAYMENT_PROCESSING -> PAYMENT_FAILED
     * Payment processing fails
     */
    void e_paymentFails();
    
    /**
     * Transition: PAYMENT_SUCCESS -> QR_GENERATED
     * Generate QR code for the ticket
     */
    void e_generateQR();
    
    /**
     * Transition: QR_GENERATED -> TICKET_ACTIVE
     * Validate and activate the ticket
     */
    void e_validateTicket();
    
    /**
     * Transition: TICKET_ACTIVE -> TICKET_EXPIRED
     * Ticket expires after validity period
     */
    void e_ticketExpires();
    
    /**
     * Transition: PAYMENT_FAILED -> PAYMENT_METHOD_SELECTED
     * User retries payment after failure
     */
    void e_retryPayment();
    
    /**
     * Transition: TICKET_SELECTED/PAYMENT_METHOD_SELECTED/PAYMENT_FAILED -> IDLE
     * User cancels the purchase process
     */
    void e_cancelPurchase();
    
    /**
     * Transition: TICKET_EXPIRED -> IDLE
     * System resets after ticket expiration
     */
    void e_reset();
}