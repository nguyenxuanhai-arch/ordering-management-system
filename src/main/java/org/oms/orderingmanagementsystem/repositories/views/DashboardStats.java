package org.oms.orderingmanagementsystem.repositories.views;

public interface DashboardStats {
    long getUserCount();
    long getOrderCount();
    double getOrderSum();
}

