package com.budapestgo.model;

/**
 * Enum representing the FSM states of the ticket purchase system.
 * These states correspond to the GraphWalker model vertices.
 */
public enum TicketState {
    /**
     * Initial state - no ticket selected
     */
    IDLE("Idle", "No active purchase"),
    
    /**
     * Ticket type has been selected
     */
    TICKET_SELECTED("Ticket Selected", "User selected a ticket type"),
    
    /**
     * Payment method has been selected
     */
    PAYMENT_METHOD_SELECTED("Payment Method Selected", "User selected payment method"),
    
    /**
     * Payment is being processed
     */
    PAYMENT_PROCESSING("Payment Processing", "Payment transaction in progress"),
    
    /**
     * Payment completed successfully
     */
    PAYMENT_SUCCESS("Payment Success", "Payment completed successfully"),
    
    /**
     * Payment failed
     */
    PAYMENT_FAILED("Payment Failed", "Payment transaction failed"),
    
    /**
     * QR code has been generated
     */
    QR_GENERATED("QR Generated", "QR code created for ticket"),
    
    /**
     * Ticket is active and valid
     */
    TICKET_ACTIVE("Ticket Active", "Ticket validated and active"),
    
    /**
     * Ticket has expired
     */
    TICKET_EXPIRED("Ticket Expired", "Ticket validity period ended"),
    
    /**
     * System error state
     */
    ERROR("Error", "System error occurred");

    private final String displayName;
    private final String description;

    /**
     * Constructor for TicketState enum.
     *
     * @param displayName Human-readable state name
     * @param description Description of the state
     */
    TicketState(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the display name.
     *
     * @return human-readable state name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description.
     *
     * @return state description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}