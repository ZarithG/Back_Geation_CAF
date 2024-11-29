package com.uptc.shiftmicroservice.enums;

public enum ReservationEnum {
    ATTENDED, //Estado que indica que la reservación fue atendida.
    NOT_ATTENDED, //Estado que indica que la reservación no fue atendida.
    SCHEDULED, //Estado que indica que la reservación está programada para una fecha futura.
    CANCELED
}
