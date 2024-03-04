package de.whosfritz.railinsights.data;

import lombok.Getter;

public enum PrognoseStateEnum {

    ON_TIME("Pünktlich"),
    DELAYED("Verspätet"),
    LOW_TRANSFER_TIME("Geringe Umsteigezeit"),
    OK_TRANSFER_TIME("Ausreichende Umsteigezeit"),
    PROBABLY_NOT_REACHABLE("Umstieg wahrscheinlich nicht erreichbar");

    @Getter
    private final String state;

    PrognoseStateEnum(String state) {
        this.state = state;
    }

}
