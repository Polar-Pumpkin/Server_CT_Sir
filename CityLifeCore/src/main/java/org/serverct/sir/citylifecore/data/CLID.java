package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data @AllArgsConstructor class CLID{

    private String key;
    private Object father;

    public boolean equals(CLID id) {
        return this.key.equals(id.getKey());
    }

}
