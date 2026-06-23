package com.example.subscription.util;

import java.util.Set;

/**
 * Validates sort column names against a known allowlist to prevent
 * JPA from receiving arbitrary field names that cause a 500 error.
 */
public final class SortValidator {

    private SortValidator() {}

    public static final Set<String> SUBSCRIPTION_SORT_FIELDS = Set.of(
            "id", "customerId", "productId", "status", "startDate", "expiryDate", "createdAt", "updatedAt"
    );

    public static void validateSubscriptionSortField(String sortBy) {
        if (!SUBSCRIPTION_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field '" + sortBy + "'. Allowed fields: " + SUBSCRIPTION_SORT_FIELDS);
        }
    }
}
