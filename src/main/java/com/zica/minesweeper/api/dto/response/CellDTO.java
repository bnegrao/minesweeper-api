package com.zica.minesweeper.api.dto.response;

import io.swagger.annotations.ApiModel;

@ApiModel (description = "Cell in a Game. The Cell can be closed or opened. If closed, the 'properties' property will be null")
public class CellDTO {

    private CellPropertiesDTO properties;

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @return CellDTO.Properties if the Cell is opened, or null if it is closed
     */
    public CellPropertiesDTO getProperties() {
        return properties;
    }

    public void setProperties(CellPropertiesDTO properties) {
        this.properties = properties;
    }

}
