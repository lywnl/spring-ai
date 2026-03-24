package com.lyw.agents.entity;

public record RoutingResponse(
        /**
         * The reasoning behind the route selection, explaining why this particular
         * route was chosen based on the input analysis.
         */
        String reasoning,

        /**
         * The selected route name that will handle the input based on the
         * classification analysis.
         */
        String selection) {
}
