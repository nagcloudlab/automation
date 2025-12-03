package com.npci.level10;

import com.npci.level10.basics.*;
import com.npci.level10.intermediate.*;
import com.npci.level10.terminal.*;
import com.npci.level10.collectors.*;
import com.npci.level10.advanced.*;

/**
 * Level 10: Demo - Streams API
 */
public class Level10Demo {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     LEVEL 10: STREAMS API                            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Demo 1: Stream Basics
        System.out.println("\n▶ DEMO 1: Stream Basics");
        System.out.println("─────────────────────────────────────────────\n");
        demo1_StreamBasics();

        // Demo 2: Intermediate Operations
        System.out.println("\n▶ DEMO 2: Intermediate Operations");
        System.out.println("─────────────────────────────────────────────\n");
        demo2_IntermediateOperations();

        // Demo 3: Terminal Operations
        System.out.println("\n▶ DEMO 3: Terminal Operations");
        System.out.println("─────────────────────────────────────────────\n");
        demo3_TerminalOperations();

        // Demo 4: Collectors
        System.out.println("\n▶ DEMO 4: Collectors");
        System.out.println("─────────────────────────────────────────────\n");
        demo4_Collectors();

        // Demo 5: Advanced Topics
        System.out.println("\n▶ DEMO 5: Advanced Topics");
        System.out.println("─────────────────────────────────────────────\n");
        demo5_AdvancedTopics();

        // Demo 6: Banking Analytics
        System.out.println("\n▶ DEMO 6: Banking Analytics");
        System.out.println("─────────────────────────────────────────────\n");
        demo6_BankingAnalytics();

        // Demo 7: Summary
        System.out.println("\n▶ DEMO 7: Streams API Summary");
        System.out.println("─────────────────────────────────────────────\n");
        demo7_Summary();
    }

    static void demo1_StreamBasics() {
        StreamBasicsDemo.demonstrateStreamCreation();
        StreamBasicsDemo.demonstrateStreamPipeline();
        StreamBasicsDemo.demonstrateCommonPatterns();
    }

    static void demo2_IntermediateOperations() {
        IntermediateOperationsDemo.demonstrateFilter();
        IntermediateOperationsDemo.demonstrateMap();
        IntermediateOperationsDemo.demonstrateFlatMap();
        IntermediateOperationsDemo.demonstratePrimitiveMapping();
        IntermediateOperationsDemo.demonstrateSorted();
        IntermediateOperationsDemo.demonstrateDistinctLimitSkip();
        IntermediateOperationsDemo.demonstratePeek();
        IntermediateOperationsDemo.demonstrateTakeDropWhile();
    }

    static void demo3_TerminalOperations() {
        TerminalOperationsDemo.demonstrateReduce();
        TerminalOperationsDemo.demonstrateCollect();
        TerminalOperationsDemo.demonstrateFind();
        TerminalOperationsDemo.demonstrateMatch();
        TerminalOperationsDemo.demonstrateAggregation();
        TerminalOperationsDemo.demonstrateForEach();
        TerminalOperationsDemo.demonstrateToArray();
    }

    static void demo4_Collectors() {
        CollectorsDemo.demonstrateGroupingBy();
        CollectorsDemo.demonstratePartitioningBy();
        CollectorsDemo.demonstrateCollectingAndThen();
        CollectorsDemo.demonstrateReducingCollector();
        CollectorsDemo.demonstrateTeeing();
        CollectorsDemo.demonstrateCustomCollector();
    }

    static void demo5_AdvancedTopics() {
        AdvancedStreamsDemo.demonstrateParallelStreams();
        AdvancedStreamsDemo.demonstrateOptional();
        AdvancedStreamsDemo.demonstrateInfiniteStreams();
        AdvancedStreamsDemo.demonstratePerformanceTips();
    }

    static void demo6_BankingAnalytics() {
        BankingAnalytics analytics = new BankingAnalytics();
        analytics.displayDashboard();
    }

    static void demo7_Summary() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    STREAMS API SUMMARY                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                    ║");
        System.out.println("║  STREAM PIPELINE:                                                  ║");
        System.out.println("║    Source → Intermediate Operations → Terminal Operation          ║");
        System.out.println("║                                                                    ║");
        System.out.println("║  SOURCES:                                                          ║");
        System.out.println("║    Collection.stream(), Arrays.stream(), Stream.of()              ║");
        System.out.println("║    Stream.generate(), Stream.iterate(), IntStream.range()         ║");
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              INTERMEDIATE OPERATIONS (Lazy)                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Operation    │ Description                                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ filter()     │ Select elements matching predicate                 ║");
        System.out.println("║ map()        │ Transform elements                                 ║");
        System.out.println("║ flatMap()    │ Flatten nested streams                             ║");
        System.out.println("║ sorted()     │ Sort elements                                      ║");
        System.out.println("║ distinct()   │ Remove duplicates                                  ║");
        System.out.println("║ limit()      │ Take first N elements                              ║");
        System.out.println("║ skip()       │ Skip first N elements                              ║");
        System.out.println("║ peek()       │ Debug/inspect elements                             ║");
        System.out.println("║ takeWhile()  │ Take while predicate true (Java 9+)                ║");
        System.out.println("║ dropWhile()  │ Drop while predicate true (Java 9+)                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              TERMINAL OPERATIONS                                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Operation    │ Description                                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ collect()    │ Collect to collection/map                          ║");
        System.out.println("║ reduce()     │ Reduce to single value                             ║");
        System.out.println("║ forEach()    │ Iterate and perform action                         ║");
        System.out.println("║ count()      │ Count elements                                     ║");
        System.out.println("║ min()/max()  │ Find min/max element                               ║");
        System.out.println("║ findFirst()  │ Find first matching element                        ║");
        System.out.println("║ findAny()    │ Find any matching element                          ║");
        System.out.println("║ anyMatch()   │ True if any element matches                        ║");
        System.out.println("║ allMatch()   │ True if all elements match                         ║");
        System.out.println("║ noneMatch()  │ True if no element matches                         ║");
        System.out.println("║ toArray()    │ Convert to array                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      KEY COLLECTORS                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ toList(), toSet(), toMap()       │ Collect to collection          ║");
        System.out.println("║ joining()                        │ Concatenate strings            ║");
        System.out.println("║ counting()                       │ Count elements                 ║");
        System.out.println("║ summingDouble/Int/Long()         │ Sum values                     ║");
        System.out.println("║ averagingDouble/Int/Long()       │ Calculate average              ║");
        System.out.println("║ summarizingDouble/Int/Long()     │ Full statistics                ║");
        System.out.println("║ groupingBy()                     │ Group by classifier            ║");
        System.out.println("║ partitioningBy()                 │ Split into true/false          ║");
        System.out.println("║ mapping()                        │ Map before collecting          ║");
        System.out.println("║ reducing()                       │ Custom reduction               ║");
        System.out.println("║ teeing()                         │ Combine two collectors         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BANKING USE CASES                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Use Case                        │ Stream Pattern                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Total deposits                  │ mapToDouble().sum()             ║");
        System.out.println("║ Active accounts                 │ filter(isActive).collect()      ║");
        System.out.println("║ Accounts by branch              │ groupingBy(getBranch)           ║");
        System.out.println("║ Top N by balance                │ sorted().limit(n)               ║");
        System.out.println("║ Average transaction             │ mapToDouble().average()         ║");
        System.out.println("║ Transaction by channel          │ groupingBy(getChannel)          ║");
        System.out.println("║ Success/Failed split            │ partitioningBy(isSuccess)       ║");
        System.out.println("║ High value transactions         │ filter(amt > threshold)         ║");
        System.out.println("║ Daily transaction volume        │ groupingBy(date, summing)       ║");
        System.out.println("║ Customer summary                │ flatMap + groupingBy            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BEST PRACTICES                                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ 1. Keep streams short and readable                                 ║");
        System.out.println("║ 2. Use method references when possible                             ║");
        System.out.println("║ 3. Filter early to reduce elements                                 ║");
        System.out.println("║ 4. Use primitive streams for performance                           ║");
        System.out.println("║ 5. Avoid side effects in stream operations                         ║");
        System.out.println("║ 6. Use parallel streams only for large data                        ║");
        System.out.println("║ 7. Handle Optional properly (orElse, ifPresent)                    ║");
        System.out.println("║ 8. Close streams from I/O sources                                  ║");
        System.out.println("║ 9. Don't reuse streams after terminal operation                    ║");
        System.out.println("║ 10. Use appropriate collector for the task                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n══════════════════════════════════════════════════════════════════════");
        System.out.println("   CONGRATULATIONS! You have completed Level 10: Streams API!");
        System.out.println("   This completes the Java OOP Training Program.");
        System.out.println("══════════════════════════════════════════════════════════════════════");
    }
}