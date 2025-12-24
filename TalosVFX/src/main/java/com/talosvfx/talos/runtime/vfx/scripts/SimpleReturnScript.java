
package com.talosvfx.talos.runtime.vfx.scripts;

import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public abstract class SimpleReturnScript {

    public abstract void evaulate(NumericalValue i1, NumericalValue i2, NumericalValue i3, NumericalValue i4, NumericalValue i5,
                                  NumericalValue o1, NumericalValue o2, NumericalValue o3, NumericalValue o4, NumericalValue o5);
}
