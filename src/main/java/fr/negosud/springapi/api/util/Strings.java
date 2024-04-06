package fr.negosud.springapi.api.util;

import java.text.Normalizer;

public final class Strings {

    public static String getCodeFromName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s", "_")
                .toUpperCase();
    }
}
