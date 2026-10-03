package com.ridelink.ridemanagement.domain;

public enum RideStatus {
    REQUESTED,
    ASSIGNED,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(RideStatus nextStatus) {
        if (this == nextStatus) {
            return false;
        }

        switch (this) {
            case REQUESTED:
                return nextStatus == ASSIGNED || nextStatus == CANCELLED;
            case ASSIGNED:
                return nextStatus == ACCEPTED || nextStatus == CANCELLED;
            case ACCEPTED:
                return nextStatus == IN_PROGRESS || nextStatus == CANCELLED;
            case IN_PROGRESS:
                return nextStatus == COMPLETED || nextStatus == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
