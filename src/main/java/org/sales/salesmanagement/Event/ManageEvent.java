package org.sales.salesmanagement.Event;

import lombok.Getter;
import lombok.Setter;
import org.sales.salesmanagement.enums.UserAction;

import org.sales.salesmanagement.models.Customers;
import org.springframework.context.ApplicationEvent;;

@Getter
@Setter

public class ManageEvent extends ApplicationEvent {
    private Customers customers;
    private UserAction userAction;

    public ManageEvent(Object source, Customers customers, UserAction userAction) {
        super(source);
        this.customers = customers;
        this.userAction = userAction;
    }

}
