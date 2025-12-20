package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.AliasIdentifier;
import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.GroupIdentifier;
import com.kotcrab.vis.usl.lang.Identifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;

import java.util.ArrayList;

/**
 * Converts list of blocks created by {@link Parser} into json string
 */
public class USLJsonWriter {
    private final ArrayList<StyleBlock> styleBlocks;

    private final StringBuilder out;

    public USLJsonWriter(ArrayList<StyleBlock> mergedStyleBlocks) {
        out = new StringBuilder();
        styleBlocks = mergedStyleBlocks;
    }

    public String getJson() {
        out.append("{\n");

        for (int i = 0; i < styleBlocks.size(); i++) {
            StyleBlock block = styleBlocks.get(i);
            out.append(block.fullName).append(": {\n");

            ArrayList<StyleIdentifier> styles = block.styles;
            for (int j = 0; j < styles.size(); j++) {
                StyleIdentifier style = styles.get(j);
                if (style.metaStyle) continue;

                out.append("\t").append(style.name).append(": ");
                if (style.content.size() == 1 && style.content.get(0) instanceof AliasIdentifier) {
                    writeIdentifiers(style.content);
                } else {
                    out.append("{");
                    writeIdentifiers(style.content);
                    out.append(" }");
                }

                if (j == styles.size() - 1) {
                    out.append("\n");
                } else {
                    out.append(",\n");
                }
            }

            if (i == styleBlocks.size() - 1) {
                out.append("}\n");
            } else {
                out.append("},\n");
            }
        }

        out.append("\n}");
        return out.toString();
    }

    private void writeIdentifiers(ArrayList<Identifier> content) {
        for (int i = 0; i < content.size(); i++) {
            Identifier id = content.get(i);
            if (id instanceof BasicIdentifier bid) {
                if (bid.content.equals("NULL")) continue;
                out.append(bid.name).append(": ").append(bid.content);
            } else if (id instanceof GroupIdentifier gid) {
                out.append(gid.name).append(": {");
                writeIdentifiers(gid.content);
                out.append("}");
            } else if (id instanceof AliasIdentifier) {
                out.append(id.name);
            }

            if (i != content.size() - 1) {
                out.append(", ");
            }
        }
    }
}
