package com.budapestgo.model;

/**
 * Enum representing different ticket types available in BudapestGO system.
 * Each ticket type has a specific validity period and price.
 */
public enum TicketType {
    /**
     * Single ticket - valid for 80 minutes, costs 350 HUF
     */
    SINGLE(80, 350, "Single Ticket"),
    
    /**
     * Day pass - valid for 24 hours (1440 minutes), costs 1650 HUF
     */
    DAY_PASS(1440, 1650, "Day Pass"),
    
    /**
     * Weekly pass - valid for 7 days (10080 minutes), costs 4950 HUF
     */
    WEEKLY(10080, 4950, "Weekly Pass"),
    
    /**
     * Monthly pass - valid for 30 days (43200 minutes), costs 9500 HUF
     */
    MONTHLY(43200, 9500, "Monthly Pass");

    private final int validityMinutes;
    private final int priceHUF;
    private final String displayName;

    /**
     * Constructor for TicketType enum.
     *
     * @param validityMinutes Duration of ticket validity in minutes
     * @param priceHUF Price of the ticket in Hungarian Forints
     * @param displayName Human-readable name of the ticket type
     */
    TicketType(int validityMinutes, int priceHUF, String displayName) {
        this.validityMinutes = validityMinutes;
        this.priceHUF = priceHUF;
        this.displayName = displayName;
    }

    /**
     * Get the validity period in minutes.
     *
     * @return validity duration in minutes
     */
    public int getValidityMinutes() {
        return validityMinutes;
    }

    /**
     * Get the price in HUF.
     *
     * @return price in Hungarian Forints
     */
    public int getPriceHUF() {
        return priceHUF;
    }

    /**
     * Get the display name.
     *
     * @return human-readable ticket type name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the validity period in milliseconds (for timestamp calculations).
     *
     * @return validity duration in milliseconds
     */
    public long getValidityMillis() {
        return validityMinutes * 60L * 1000L;
    }

    /**
     * Get a random ticket type (used in testing).
     *
     * @return randomly selected TicketType
     */
    public static TicketType random() {
        TicketType[] values = values();
        return values[(int) (Math.random() * values.length)];
    }

    @Override
    public String toString() {
        return String.format("%s (%d min, %d HUF)", displayName, validityMinutes, priceHUF);
    }
}