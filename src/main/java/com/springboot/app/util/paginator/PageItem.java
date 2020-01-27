package com.springboot.app.util.paginator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {

    private int number;
    private boolean actual;

}
