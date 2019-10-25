package org.bees.optimizer.knapsack.graph;

import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.external.RouteDto;

import java.util.List;

public class ShortPath {
    public static List<String> getShortestPath(List<RouteDto> routesDto, int source, int dest) {
        int[][] intArray = ModelConverter.convertRoutes(routesDto);
        Graph graph = new Graph();
        for (int i = 1; i < intArray.length; i++) {
            for (int j = 1; j < intArray[i].length; j++) {
                graph.addEdge(i + "", j + "", intArray[i][j]);
            }
        }
        return graph.shortestPath(source + "", dest + "");

    }
}