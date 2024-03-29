package org.sales.salesmanagement.enums;

public enum RegistrationState {
    COMPLETED,
    DELETED
    ;
    public boolean isBefore(RegistrationState otherState) {
        return this.ordinal() < otherState.ordinal();
    }
}

