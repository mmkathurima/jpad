package io.jpad;

import com.google.common.base.Preconditions;
import com.timestored.theme.Theme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.tools.Diagnostic;
import java.awt.Color;

public enum ErrorLevel {
    ERROR(Theme.CIcon.ERROR, new Color(255, 215, 215)),
    WARNING(Theme.CIcon.WARNING, new Color(255, 255, 195)),
    NOTE(Theme.CIcon.EDIT_COMMENT, null),
    OTHER(Theme.CIcon.FUNCTION_ELEMENT, null);

    private final Theme.CIcon cIcon;
    private final Color color;

    ErrorLevel(Theme.CIcon cIcon, Color color) {
        this.cIcon = Preconditions.checkNotNull(cIcon);
        this.color = color;
    }

    public static ErrorLevel get(Diagnostic.Kind kind) {
        switch (kind) {
            case ERROR:
                return ERROR;
            case MANDATORY_WARNING:
            case WARNING:
                return WARNING;
            case NOTE:
                return NOTE;
        }

        return OTHER;
    }

    @NotNull
    Theme.CIcon getcIcon() {
        return this.cIcon;
    }

    @Nullable
    Color getColor() {
        return this.color;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\ErrorLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */