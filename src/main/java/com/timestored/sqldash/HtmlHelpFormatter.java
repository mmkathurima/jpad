package com.timestored.sqldash;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;

import java.util.*;

class HtmlHelpFormatter
        implements HelpFormatter {
    public String format(Map<String, ? extends OptionDescriptor> options) {
        StringBuilder sb = new StringBuilder();

        if (!options.isEmpty()) {

            Comparator<OptionDescriptor> comparator = new Comparator<OptionDescriptor>() {
                public int compare(OptionDescriptor first, OptionDescriptor second) {
                    return first.options().iterator().next().compareTo(second.options().iterator().next());
                }
            };
            TreeSet<OptionDescriptor> sorted = new TreeSet<OptionDescriptor>(comparator);

            Set<String> seenCommands = Sets.newHashSet();

            for (OptionDescriptor od : options.values()) {
                String k = this.getOptionDisplay(od);
                if (!seenCommands.contains(k)) {
                    seenCommands.add(k);
                    sorted.add(od);
                }
            }

            sb.append("<table>\r\n<tr><th>Option");
            if (this.hasRequiredOption(sorted)) {
                sb.append(" (<span class='required'>*</span> = required)");
            }
            sb.append("</th><th>Description</th></tr>");
            for (OptionDescriptor od : sorted) {
                sb.append("\r\n\t<tr>");
                sb.append("<td style='white-space:nowrap;'><code>");
                sb.append(this.getOptionDisplay(od));
                sb.append("</code></td><td>");

                List<?> defVals = od.defaultValues();
                if (defVals.isEmpty()) {
                    sb.append(od.description());
                } else {
                    String def = (defVals.size() == 1) ? defVals.get(0).toString() : defVals.toString();

                    sb.append(od.description() + " (default: " + def + ")");
                }
                sb.append("</td></tr>");
            }
            sb.append("\r\n</table>");
        }

        return sb.toString();
    }

    private boolean hasRequiredOption(Collection<? extends OptionDescriptor> options) {
        for (OptionDescriptor each : options) {
            if (each.isRequired()) {
                return true;
            }
        }
        return false;
    }

    private String getOptionDisplay(OptionDescriptor descriptor) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(descriptor.isRequired() ? "<span class='required'>*</span>" : "");

        for (Iterator<String> i = descriptor.options().iterator(); i.hasNext(); ) {
            String option = i.next();
            buffer.append((option.length() > 1) ? "--" : "-");
            buffer.append(option);

            if (i.hasNext()) {
                buffer.append(", ");
            }
        }

        this.maybeAppendOptionInfo(buffer, descriptor);
        return buffer.toString();
    }

    private void maybeAppendOptionInfo(StringBuilder buffer, OptionDescriptor descriptor) {
        String indicator = this.extractTypeIndicator(descriptor);
        String description = descriptor.argumentDescription();
        if (indicator != null || !Strings.isNullOrEmpty(description))
            this.appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument());
    }

    private String extractTypeIndicator(OptionDescriptor descriptor) {
        String indicator = descriptor.argumentTypeIndicator();

        if (!Strings.isNullOrEmpty(indicator) && !String.class.getName().equals(indicator)) {
            return indicator;
        }

        return null;
    }

    private void appendOptionHelp(StringBuilder buffer, String typeIndicator, String description, boolean required) {
        if (required) {
            this.appendTypeIndicator(buffer, typeIndicator, description, "&lt;", "&gt;");
        } else {
            this.appendTypeIndicator(buffer, typeIndicator, description, "[", "]");
        }
    }

    private void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description, String start, String end) {
        buffer.append(' ').append(start);
        if (typeIndicator != null) {
            buffer.append(typeIndicator);
        }
        if (!Strings.isNullOrEmpty(description)) {
            if (typeIndicator != null) {
                buffer.append(": ");
            }
            buffer.append(description);
        }

        buffer.append(end);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\HtmlHelpFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */