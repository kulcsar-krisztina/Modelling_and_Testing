package com.budapestgo.mbt;

import org.graphwalker.java.annotation.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Shortest path test using vertex coverage strategy
 */
@Model(file = "com/budapestgo/mbt/BudapestGO_Model.json")
public class ShortestPathTest extends BaseModelTest {

    @BeforeEach
    public void setUp() {
        logger.info("╔════════════════════════════════════════════════════════╗");
        logger.info("║     SHORTEST PATH TEST - STARTED                       ║");
        logger.info("║     Algorithm: Vertex Coverage (Minimal Path)         ║");
        logger.info("╚════════════════════════════════════════════════════════╝");
        initializeTest();
    }

    @Test
    public void runShortestPathTest() {
        logger.info("Executing shortest path strategy (vertex coverage)...");
        
        try {
            // ✅ SHORTEST PATH: Minimal vertex/edge calls for successful ticket purchase
            // This is the shortest path to complete a successful ticket purchase
            
            v_IDLE();                           // START
            e_selectTicket();                   // Select a ticket
            v_TICKET_SELECTED();
            e_choosePayment();                  // Choose payment method
            v_PAYMENT_METHOD_SELECTED();
            e_initiatePayment();                // Initiate payment processing
            v_PAYMENT_PROCESSING();
            e_paymentSucceeds();                // Payment succeeds
            v_PAYMENT_SUCCESS();
            e_generateQR();                     // Generate QR code
            v_QR_GENERATED();
            e_validateTicket();                 // Validate ticket
            v_TICKET_ACTIVE();                  // Ticket is active
            
            logger.info("✅ Shortest path test completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Shortest path test FAILED: {}", e.getMessage(), e);
            throw new RuntimeException("Test execution failed", e);
        }
    }

    @AfterEach
    public void tearDown() {
        cleanupTest();
        logger.info("╔════════════════════════════════════════════════════════╗");
        logger.info("║     SHORTEST PATH TEST - COMPLETED                     ║");
        logger.info("║     Vertices: {}                                      ║", vertexCounter);
        logger.info("║     Edges: {}                                         ║", edgeCounter);
        logger.info("║     Test Cases: {}                                    ║", testCaseCounter);
        logger.info("╚════════════════════════════════════════════════════════╝");
    }
}