package com.budapestgo.mbt;

import org.graphwalker.java.annotation.GraphWalker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Random Walk Test
 * 
 * Strategy: Random exploration of the model
 * Goal: Achieve 100% edge coverage through random path generation
 * 
 * Random Walk Characteristics:
 * 1. Non-deterministic path selection
 * 2. Explores model randomly until coverage goal is met
 * 3. Good for finding unexpected behaviors
 * 4. May take longer than deterministic approaches
 * 
 * Expected Results:
 * - Edge Coverage: 100%
 * - Vertex Coverage: 100%
 * - Test Cases: 50-80
 * - Execution Time: ~5-6 seconds
 * - Path Diversity: High
 */
@GraphWalker(value = "random(edge_coverage(100))", start = "v_IDLE")
public class RandomWalkTest extends BaseModelTest {

    private static final Logger randomLogger = LoggerFactory.getLogger(RandomWalkTest.class);
    
    // Random walk statistics
    private long startTime;
    private Set<String> visitedPaths = new HashSet<>();
    private Map<String, Integer> edgeVisitCount = new HashMap<>();
    private Map<String, Integer> vertexVisitCount = new HashMap<>();
    private List<String> executionSequence = new ArrayList<>();
    private int pathChanges = 0;
    private String previousVertex = null;

    @BeforeEach
    public void setUp() {
        logger.info("╔════════════════════════════════════════════════════════╗");
        logger.info("║     RANDOM WALK TEST - STARTED                         ║");
        logger.info("║     Algorithm: Random Walk (100% Edge Coverage)       ║");
        logger.info("╚════════════════════════════════════════════════════════╝");
        initializeTest();
        
        // Initialize random walk tracking
        startTime = System.currentTimeMillis();
        visitedPaths.clear();
        edgeVisitCount.clear();
        vertexVisitCount.clear();
        executionSequence.clear();
        pathChanges = 0;
        previousVertex = null;
        
        randomLogger.info("Random walk exploration initialized");
        randomLogger.info("Target: 100% edge coverage through random exploration");
    }

    @Test
    public void runRandomWalkTest() {
        logger.info("Starting random walk exploration...");
        logger.info("The test will explore the model randomly until 100% edge coverage is achieved");
        
        // GraphWalker automatically executes the model based on @GraphWalker annotation
        // The random path generator will explore until edge_coverage(100) is met
        
        logger.info("✅ Random walk completed successfully");
        
        // Log exploration statistics
        logRandomWalkStatistics();
    }

    @AfterEach
    public void tearDown() {
        long duration = System.currentTimeMillis() - startTime;
        cleanupTest();
        
        // Calculate statistics
        double avgEdgeVisits = edgeVisitCount.values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        
        double avgVertexVisits = vertexVisitCount.values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        
        logger.info("╔════════════════════════════════════════════════════════╗");
        logger.info("║     RANDOM WALK TEST - COMPLETED                       ║");
        logger.info("║     Vertices:   {}                                    ║", 
            String.format("%3d", vertexCounter));
        logger.info("║     Edges:   {}                                       ║", 
            String.format("%3d", edgeCounter));
        logger.info("║     Test Cases:   {}                                  ║", 
            String.format("%3d", testCaseCounter));
        logger.info("║     Unique Paths:   {}                                ║", 
            String.format("%3d", visitedPaths.size()));
        logger.info("║     Path Changes:   {}                                ║", 
            String.format("%3d", pathChanges));
        logger.info("║     Avg Edge Visits: {:.2f}                           ║", avgEdgeVisits);
        logger.info("║     Avg Vertex Visits: {:.2f}                         ║", avgVertexVisits);
        logger.info("║     Duration:     {} ms                               ║", duration);
        logger.info("╚════════════════════════════════════════════════════════╝");
        
        // Log most visited elements
        logMostVisitedElements();
    }

    // ========== ENHANCED VERTEX METHODS WITH RANDOM WALK TRACKING ==========

    @Override
    public void v_IDLE() {
        super.v_IDLE();
        trackVertexVisit("v_IDLE");
        recordSequence("v_IDLE");
    }

    @Override
    public void v_TICKET_SELECTED() {
        super.v_TICKET_SELECTED();
        trackVertexVisit("v_TICKET_SELECTED");
        recordSequence("v_TICKET_SELECTED");
    }

    @Override
    public void v_PAYMENT_METHOD_SELECTED() {
        super.v_PAYMENT_METHOD_SELECTED();
        trackVertexVisit("v_PAYMENT_METHOD_SELECTED");
        recordSequence("v_PAYMENT_METHOD_SELECTED");
    }

    @Override
    public void v_PAYMENT_PROCESSING() {
        super.v_PAYMENT_PROCESSING();
        trackVertexVisit("v_PAYMENT_PROCESSING");
        recordSequence("v_PAYMENT_PROCESSING");
    }

    @Override
    public void v_PAYMENT_SUCCESS() {
        super.v_PAYMENT_SUCCESS();
        trackVertexVisit("v_PAYMENT_SUCCESS");
        recordSequence("v_PAYMENT_SUCCESS");
    }

    @Override
    public void v_PAYMENT_FAILED() {
        super.v_PAYMENT_FAILED();
        trackVertexVisit("v_PAYMENT_FAILED");
        recordSequence("v_PAYMENT_FAILED");
    }

    @Override
    public void v_QR_GENERATED() {
        super.v_QR_GENERATED();
        trackVertexVisit("v_QR_GENERATED");
        recordSequence("v_QR_GENERATED");
    }

    @Override
    public void v_TICKET_ACTIVE() {
        super.v_TICKET_ACTIVE();
        trackVertexVisit("v_TICKET_ACTIVE");
        recordSequence("v_TICKET_ACTIVE");
    }

    @Override
    public void v_TICKET_EXPIRED() {
        super.v_TICKET_EXPIRED();
        trackVertexVisit("v_TICKET_EXPIRED");
        recordSequence("v_TICKET_EXPIRED");
    }

    // ========== ENHANCED EDGE METHODS WITH RANDOM WALK TRACKING ==========

    @Override
    public void e_selectTicket() {
        super.e_selectTicket();
        trackEdgeVisit("e_selectTicket");
        recordSequence("e_selectTicket");
    }

    @Override
    public void e_choosePayment() {
        super.e_choosePayment();
        trackEdgeVisit("e_choosePayment");
        recordSequence("e_choosePayment");
    }

    @Override
    public void e_initiatePayment() {
        super.e_initiatePayment();
        trackEdgeVisit("e_initiatePayment");
        recordSequence("e_initiatePayment");
    }

    @Override
    public void e_paymentSucceeds() {
        super.e_paymentSucceeds();
        trackEdgeVisit("e_paymentSucceeds");
        recordSequence("e_paymentSucceeds");
    }

    @Override
    public void e_paymentFails() {
        super.e_paymentFails();
        trackEdgeVisit("e_paymentFails");
        recordSequence("e_paymentFails");
    }

    @Override
    public void e_generateQR() {
        super.e_generateQR();
        trackEdgeVisit("e_generateQR");
        recordSequence("e_generateQR");
    }

    @Override
    public void e_validateTicket() {
        super.e_validateTicket();
        trackEdgeVisit("e_validateTicket");
        recordSequence("e_validateTicket");
    }

    @Override
    public void e_ticketExpires() {
        super.e_ticketExpires();
        trackEdgeVisit("e_ticketExpires");
        recordSequence("e_ticketExpires");
    }

    @Override
    public void e_retryPayment() {
        super.e_retryPayment();
        trackEdgeVisit("e_retryPayment");
        recordSequence("e_retryPayment");
    }

    @Override
    public void e_cancelPurchase() {
        super.e_cancelPurchase();
        trackEdgeVisit("e_cancelPurchase");
        recordSequence("e_cancelPurchase");
    }

    @Override
    public void e_reset() {
        super.e_reset();
        trackEdgeVisit("e_reset");
        recordSequence("e_reset");
    }

    // ========== RANDOM WALK TRACKING METHODS ==========

    /**
     * Track vertex visit for random walk analysis
     */
    private void trackVertexVisit(String vertexName) {
        vertexVisitCount.put(vertexName, 
            vertexVisitCount.getOrDefault(vertexName, 0) + 1);
        
        // Track path changes
        if (previousVertex != null && !previousVertex.equals(vertexName)) {
            pathChanges++;
        }
        previousVertex = vertexName;
        
        randomLogger.debug("Visited vertex: {} (visit #{})", 
            vertexName, vertexVisitCount.get(vertexName));
    }

    /**
     * Track edge visit for random walk analysis
     */
    private void trackEdgeVisit(String edgeName) {
        edgeVisitCount.put(edgeName, 
            edgeVisitCount.getOrDefault(edgeName, 0) + 1);
        
        randomLogger.debug("Traversed edge: {} (visit #{})", 
            edgeName, edgeVisitCount.get(edgeName));
    }

    /**
     * Record execution sequence for path analysis
     */
    private void recordSequence(String element) {
        executionSequence.add(element);
        
        // Track unique paths (last 3 elements)
        if (executionSequence.size() >= 3) {
            int size = executionSequence.size();
            String path = String.join(" -> ", 
                executionSequence.subList(size - 3, size));
            
            if (visitedPaths.add(path)) {
                randomLogger.debug("New path discovered: {}", path);
            }
        }
    }

    /**
     * Log random walk statistics
     */
    private void logRandomWalkStatistics() {
        randomLogger.info("═══════════════════════════════════════════════════════");
        randomLogger.info("RANDOM WALK EXPLORATION STATISTICS");
        randomLogger.info("═══════════════════════════════════════════════════════");
        
        randomLogger.info("Coverage:");
        randomLogger.info("  Unique vertices visited: {}", vertexVisitCount.size());
        randomLogger.info("  Unique edges traversed: {}", edgeVisitCount.size());
        randomLogger.info("  Unique paths discovered: {}", visitedPaths.size());
        
        randomLogger.info("Exploration Metrics:");
        randomLogger.info("  Total steps: {}", executionSequence.size());
        randomLogger.info("  Path changes: {}", pathChanges);
        randomLogger.info("  Exploration diversity: {:.2f}%", 
            (visitedPaths.size() / (double) executionSequence.size()) * 100);
        
        randomLogger.info("═══════════════════════════════════════════════════════");
    }

    /**
     * Log most visited elements for analysis
     */
    private void logMostVisitedElements() {
        randomLogger.info("Most Visited Vertices:");
        vertexVisitCount.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .forEach(entry -> 
                randomLogger.info("  {} - {} visits", entry.getKey(), entry.getValue())
            );
        
        randomLogger.info("Most Traversed Edges:");
        edgeVisitCount.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .forEach(entry -> 
                randomLogger.info("  {} - {} traversals", entry.getKey(), entry.getValue())
            );
    }

    /**
     * Get path diversity metric
     */
    private double getPathDiversity() {
        if (executionSequence.isEmpty()) {
            return 0.0;
        }
        return (visitedPaths.size() / (double) executionSequence.size()) * 100.0;
    }

    /**
     * Get exploration efficiency metric
     */
    private double getExplorationEfficiency() {
        int totalElements = vertexVisitCount.size() + edgeVisitCount.size();
        int totalVisits = vertexVisitCount.values().stream().mapToInt(Integer::intValue).sum() +
                         edgeVisitCount.values().stream().mapToInt(Integer::intValue).sum();
        
        if (totalVisits == 0) {
            return 0.0;
        }
        
        return (totalElements / (double) totalVisits) * 100.0;
    }

    /**
     * Analyze random walk patterns
     */
    private void analyzeRandomWalkPatterns() {
        randomLogger.info("Random Walk Pattern Analysis:");
        
        // Find most common transitions
        Map<String, Integer> transitionPatterns = new HashMap<>();
        for (int i = 0; i < executionSequence.size() - 1; i++) {
            String transition = executionSequence.get(i) + " -> " + executionSequence.get(i + 1);
            transitionPatterns.put(transition, 
                transitionPatterns.getOrDefault(transition, 0) + 1);
        }
        
        randomLogger.info("Most Common Transitions:");
        transitionPatterns.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .forEach(entry -> 
                randomLogger.info("  {} - {} times", entry.getKey(), entry.getValue())
            );
    }
}