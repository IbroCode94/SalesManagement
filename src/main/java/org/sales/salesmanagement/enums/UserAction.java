package org.sales.salesmanagement.enums;

public enum UserAction {
    USER_CREATED("User Created"),
    USER_DELETED("User Deleted"),
    USER_UPDATED("User Updated"),
    SALE_CREATED("Created Sale"),
    PRODUCT_CREATED("Product created"),
    PRODUCT_EDITED("PRODUCT_EDITED"),
    PRODUCT_DELETED("PRODUCT_DELETED");


    private final String actionName;

    UserAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
