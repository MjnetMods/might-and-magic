package org.mjli.mam.foundation.registration;

import com.tterrag.registrate.AbstractRegistrate;

public class MamRegistrate extends AbstractRegistrate<MamRegistrate> {

    protected MamRegistrate(String modid) {
        super(modid);
    }

    public static MamRegistrate create(String modid) {
        return new MamRegistrate(modid);
    }
}
