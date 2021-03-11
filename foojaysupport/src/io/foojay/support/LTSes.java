package io.foojay.support;

import io.foojay.api.discoclient.pkg.TermOfSupport;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LTSes {

    @NonNull
    public static String text(@NonNull Integer value, @NonNull TermOfSupport support) {
        final String ui;
        switch (support) {
            case LTS:
                ui = "LTS";
                break;
            case MTS:
                ui = "MTS";
                break;
            case STS:
                ui = "STS";
                break;
            case NONE:
            case NOT_FOUND:
            default:
                ui = "";
                break;
        }
        return String.format("%s (%s)", value, ui);
    }

    static int latest(Map<Integer, TermOfSupport> lts) {
        return lts.entrySet().stream()
                .filter(e -> e.getValue()==TermOfSupport.LTS)
                .mapToInt(e -> e.getKey())
                .max().getAsInt();
    }

}
