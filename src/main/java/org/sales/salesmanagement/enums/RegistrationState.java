package org.sales.salesmanagement.enums;

public enum RegistrationState {
    COMPLETED,
    DELETED
    ;

    // Helper method to check if a given state is before another state
    public boolean isBefore(RegistrationState otherState) {
        return this.ordinal() < otherState.ordinal();
    }
}

