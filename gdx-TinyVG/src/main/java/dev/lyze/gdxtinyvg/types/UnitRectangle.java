package dev.lyze.gdxtinyvg.types;

import com.badlogic.gdx.utils.LittleEndianInputStream;

import java.io.IOException;

import dev.lyze.gdxtinyvg.enums.Range;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitRectangle {
    /**
     * Horizontal distance of the left side to the origin.
     */
    private Unit x;
    /**
     * Vertical distance of the upper side to the origin.
     */
    private Unit y;
    /**
     * Horizontal extent of the rectangle.
     */
    private Unit width;
    /**
     * Vertical extent of the rectangle origin.
     */
    private Unit height;

    public UnitRectangle(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        this(new Unit(stream, range, fractionBits), new Unit(stream, range, fractionBits),
                new Unit(stream, range, fractionBits), new Unit(stream, range, fractionBits));
    }
}
