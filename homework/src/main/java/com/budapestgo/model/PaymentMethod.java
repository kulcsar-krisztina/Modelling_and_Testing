package com.budapestgo.model;

/**
 * Enum representing available payment methods in BudapestGO system.
 */
public enum PaymentMethod {
    /**
     * Credit or Debit Card payment (Visa, Mastercard)
     */
    CARD("Credit/Debit Card", "card"),
    
    /**
     * Google Pay (available on Android devices)
     */
    GOOGLE_PAY("Google Pay", "google_pay"),
    
    /**
     * Apple Pay (available on iOS devices)
     */
    APPLE_PAY("Apple Pay", "apple_pay");

    private final String displayName;
    private final String identifier;

    /**
     * Constructor for PaymentMethod enum.
     *
     * @param displayName Human-readable payment method name
     * @param identifier Technical identifier for the payment method
     */
    PaymentMethod(String displayName, String identifier) {
        this.displayName = displayName;
        this.identifier = identifier;
    }

    /**
     * Get the display name.
     *
     * @return human-readable payment method name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the technical identifier.
     *
     * @return payment method identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get a random payment method (used in testing).
     *
     * @return randomly selected PaymentMethod
     */
    public static PaymentMethod random() {
        PaymentMethod[] values = values();
        return values[(int) (Math.random() * values.length)];
    }

    @Override
    public String toString() {
        return displayName;
    }
}