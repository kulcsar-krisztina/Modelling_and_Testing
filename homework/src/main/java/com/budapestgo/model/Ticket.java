package com.budapestgo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a ticket in the BudapestGO system.
 * Contains all information about a purchased ticket including QR code,
 * validity period, and activation status.
 */
public class Ticket {
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String ticketId;
    private final TicketType type;
    private final String qrCode;
    private final LocalDateTime purchaseTime;
    private final String transactionId;
    
    private LocalDateTime validationTime;
    private LocalDateTime expiryTime;
    private boolean isActive;

    /**
     * Constructor for creating a new ticket.
     *
     * @param type The type of ticket (SINGLE, DAY_PASS, etc.)
     * @param transactionId The transaction ID from successful payment
     */
    public Ticket(TicketType type, String transactionId) {
        if (type == null) {
            throw new IllegalArgumentException("Ticket type cannot be null");
        }
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }

        this.ticketId = UUID.randomUUID().toString();
        this.type = type;
        this.transactionId = transactionId;
        this.qrCode = generateQRCode(transactionId);
        this.purchaseTime = LocalDateTime.now();
        this.isActive = false;
    }

    /**
     * Generates a QR code string for the ticket.
     *
     * @param transactionId The transaction ID to include in QR code
     * @return QR code string in format "QR-{ticketId}-{transactionId}"
     */
    private String generateQRCode(String transactionId) {
        String shortTicketId = ticketId.substring(0, 8);
        String shortTransactionId = transactionId.substring(0, 
            Math.min(8, transactionId.length()));
        return String.format("QR-%s-%s", shortTicketId, shortTransactionId);
    }

    /**
     * Validates (activates) the ticket.
     * Sets the validation time and calculates expiry time based on ticket type.
     *
     * @throws IllegalStateException if ticket is already validated
     */
    public void validate() {
        if (isActive) {
            throw new IllegalStateException(
                "Ticket is already validated. Ticket ID: " + ticketId
            );
        }
        
        this.validationTime = LocalDateTime.now();
        this.expiryTime = validationTime.plusMinutes(type.getValidityMinutes());
        this.isActive = true;
    }

    /**
     * Checks if the ticket has expired.
     *
     * @return true if ticket is expired, false otherwise
     */
    public boolean isExpired() {
        if (!isActive || expiryTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiryTime);
    }

    /**
     * Expires (deactivates) the ticket.
     *
     * @throws IllegalStateException if ticket is not active
     */
    public void expire() {
        if (!isActive) {
            throw new IllegalStateException(
                "Cannot expire inactive ticket. Ticket ID: " + ticketId
            );
        }
        this.isActive = false;
    }

    /**
     * Gets the remaining validity time in minutes.
     *
     * @return remaining minutes, or 0 if expired/not active
     */
    public long getRemainingMinutes() {
        if (!isActive || expiryTime == null) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiryTime)) {
            return 0;
        }
        
        return java.time.Duration.between(now, expiryTime).toMinutes();
    }

    // Getters

    public String getTicketId() {
        return ticketId;
    }

    public TicketType getType() {
        return type;
    }

    public String getQrCode() {
        return qrCode;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getValidationTime() {
        return validationTime;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket{");
        sb.append("id='").append(ticketId.substring(0, 8)).append("...'");
        sb.append(", type=").append(type.getDisplayName());
        sb.append(", qrCode='").append(qrCode).append("'");
        sb.append(", purchased=").append(purchaseTime.format(FORMATTER));
        
        if (isActive) {
            sb.append(", active=true");
            sb.append(", remaining=").append(getRemainingMinutes()).append(" min");
        } else {
            sb.append(", active=false");
        }
        
        sb.append("}");
        return sb.toString();
    }
}